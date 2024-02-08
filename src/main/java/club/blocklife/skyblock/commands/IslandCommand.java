package club.blocklife.skyblock.commands;

import club.blocklife.skyblock.utils.CU;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class IslandCommand implements CommandExecutor, TabCompleter {
    private final JavaPlugin plugin;
    private final Map<UUID, Long> cooldownMap = new HashMap<>();
    private final HashMap<UUID, UUID> invites = new HashMap<>();
    private final HashMap<UUID, Long> inviteCooldowns = new HashMap<>();
    private static final long INVITE_COOLDOWN = 180000; // 3 分钟

    public IslandCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void startCooldown(Player player) {
        cooldownMap.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public void stopCooldown(Player player) {
        cooldownMap.remove(player.getUniqueId());
    }
    private boolean isPlayerInOtherIsland(UUID targetUUID) {
        File teamDir = new File(plugin.getDataFolder(), "team");
        File[] teamFiles = teamDir.listFiles();
        if (teamFiles != null) {
            for (File teamFile : teamFiles) {
                FileConfiguration teamConfig = YamlConfiguration.loadConfiguration(teamFile);
                String master = teamConfig.getString("master");
                List<String> members = teamConfig.getStringList("members");
                if (master.equals(targetUUID.toString()) || members.contains(targetUUID.toString())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void createTeam(Player master, Player member) {
        File teamDir = new File(plugin.getDataFolder(), "team");
        if (!teamDir.exists()) {
            teamDir.mkdir();
        }
        File teamFile = new File(teamDir, master.getUniqueId().toString() + ".yml");
        FileConfiguration teamConfig = YamlConfiguration.loadConfiguration(teamFile);
        teamConfig.set("master", master.getUniqueId().toString()); // 设置 master 字段
        List<String> members;
        if (teamFile.exists()) {
            members = teamConfig.getStringList("members");
        } else {
            members = new ArrayList<>();
        }
        members.add(member.getUniqueId().toString());
        teamConfig.set("members", members);
        try {
            teamConfig.save(teamFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        inviteCooldowns.remove(master.getUniqueId()); // 移除邀请者冷却时间

        // 读取配置文件中的master和members ，并向他们发送消息
        String masterId = teamConfig.getString("master");
        List<String> memberIds = teamConfig.getStringList("members");
        for (String memberId : memberIds) {
            Player teamMember = Bukkit.getPlayer(UUID.fromString(memberId));
            if (teamMember != null) {
                teamMember.sendMessage(CU.t(member.getName() + " &b已经加入您的岛屿")); // 使用玩家的名称
            }
        }
        Player teamMaster = Bukkit.getPlayer(UUID.fromString(masterId));
        if (teamMaster != null) {
            teamMaster.sendMessage(CU.t(member.getName() + " &b已经加入您的岛屿")); // 使用玩家的名称
        }
    }

    private void kickMember(Player master, Player member) {
        File teamFile = new File(new File(plugin.getDataFolder(), "team"), master.getUniqueId().toString() + ".yml");
        if (!teamFile.exists()) {
            master.sendMessage(CU.t("&c你并不拥有岛屿"));
            return;
        }
        FileConfiguration teamConfig = YamlConfiguration.loadConfiguration(teamFile);
        String teamMaster = teamConfig.getString("master");
        if (!master.getUniqueId().toString().equals(teamMaster)) {
            master.sendMessage(CU.t("&c只有岛主才能踢出成员"));
            return;
        }
        if (member.getUniqueId().toString().equals(teamMaster)) {
            master.sendMessage(CU.t("&c你不能踢出Master"));
            return;
        }
        List<String> members = teamConfig.getStringList("members");
        if (!members.contains(member.getUniqueId().toString())) {
            master.sendMessage(CU.t("&c他不是你的岛屿成员"));
            return;
        }
        members.remove(member.getUniqueId().toString());
        teamConfig.set("members", members);
        try {
            teamConfig.save(teamFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File playerFile = new File(new File(plugin.getDataFolder(), "playerlocation"), member.getUniqueId().toString() + ".yml");
        if (playerFile.exists()) {
            playerFile.delete();
        }

        member.kickPlayer("你被踢出了岛屿\n请重进服务器");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CU.t("&c你必须是一个玩家才能使用此命令"));
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            long lastDamageTime = cooldownMap.getOrDefault(player.getUniqueId(), 0L);
            long remainingTime = 5 - (System.currentTimeMillis() - lastDamageTime) / 1000;
            if (remainingTime > 0) {
                player.sendMessage(CU.t("&c你正在处于战斗状态，请等待&6" + remainingTime + "秒&c后再使用此命令"));
                return true;
            }

            File playerFile = new File(new File(plugin.getDataFolder(), "playerlocation"), player.getUniqueId().toString() + ".yml");
            if (!playerFile.exists()) {
                player.sendMessage("你并不拥有岛屿");
                return true;
            }

            FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
            double x = playerConfig.getDouble("location.x");
            double y = playerConfig.getDouble("location.y");
            double z = playerConfig.getDouble("location.z");
            player.teleport(new Location(player.getWorld(), x, y, z));

            return true;
        } else if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                player.sendMessage(CU.t("&c找不到指定的玩家"));
                return true;
            }

            if (args[0].equalsIgnoreCase("invite")) {
                if (player.equals(target)) {
                    player.sendMessage(CU.t("&c你不能邀请自己"));
                    return true;
                }
                if (isPlayerInOtherIsland(target.getUniqueId())) {
                    player.sendMessage(CU.t("&c玩家已有岛屿"));
                    return true;
                }

                File teamFile = new File(new File(plugin.getDataFolder(), "team"), player.getUniqueId().toString() + ".yml");
                if (teamFile.exists()) {
                    FileConfiguration teamConfig = YamlConfiguration.loadConfiguration(teamFile);
                    List<String> members = teamConfig.getStringList("members");
                    if (members.contains(target.getUniqueId().toString())) {
                        player.sendMessage(CU.t("&c他已经是你的岛屿成员了"));
                        return true;
                    }
                }

                long lastInviteTime = inviteCooldowns.getOrDefault(player.getUniqueId(), 0L);
                long remainingTime = (INVITE_COOLDOWN - (System.currentTimeMillis() - lastInviteTime)) / 1000;
                if (remainingTime > 0) {
                    player.sendMessage(CU.t("&c你还需要等待 " + remainingTime + " 秒才能再次发送邀请"));
                    return true;
                }

                inviteCooldowns.put(player.getUniqueId(), System.currentTimeMillis());

                player.sendMessage(CU.t("&b你邀请了&6 " + target.getName() + " &b加入你的岛屿"));
                invites.put(target.getUniqueId(), player.getUniqueId());
                target.sendMessage(CU.t("&b你&c被&6 " + player.getName() + " &b邀请加入他的岛屿"));
            } else if (args[0].equalsIgnoreCase("accept")) {
                UUID inviterUUID = invites.get(player.getUniqueId());
                if (inviterUUID == null) {
                    player.sendMessage(CU.t("&c你没有收到任何邀请"));
                    return true;
                }

                Player inviter = Bukkit.getPlayer(inviterUUID);
                if (inviter == null) {
                    player.sendMessage(CU.t("&c邀请人已经离线"));
                    return true;
                }

                createTeam(inviter, player); // 在这里调用 createTeam 方法

                File inviterFile = new File(new File(plugin.getDataFolder(), "playerlocation"), inviterUUID.toString() + ".yml");
                FileConfiguration inviterConfig = YamlConfiguration.loadConfiguration(inviterFile);
                double x = inviterConfig.getDouble("location.x");
                double y = inviterConfig.getDouble("location.y");
                double z = inviterConfig.getDouble("location.z");

                File targetFile = new File(new File(plugin.getDataFolder(), "playerlocation"), player.getUniqueId().toString() + ".yml");
                FileConfiguration targetConfig = YamlConfiguration.loadConfiguration(targetFile);
                targetConfig.set("location.x", x);
                targetConfig.set("location.y", y);
                targetConfig.set("location.z", z);
                try {
                    targetConfig.save(targetFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                player.teleport(new Location(inviter.getWorld(), x, y, z));
                player.setBedSpawnLocation(new Location(inviter.getWorld(), x, y, z), true);
                player.sendMessage(CU.t("&b你已经加入了&6 " + inviter.getName() + " &b的岛屿"));
            } else if (args[0].equalsIgnoreCase("kick")) {
                kickMember(player, target);
            }

            return true;
        } else {
            player.sendMessage(CU.t("&c命令格式错误"));
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }

        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("invite");
            completions.add("accept");
            completions.add("kick");
        } else if (args.length == 2) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                completions.add(player.getName());
            }
        }
        return completions;
    }
}