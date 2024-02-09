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
                List<String> members = teamConfig.getStringList("members");
                if (members.contains(targetUUID.toString())) {
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
        if (!members.contains(member.getUniqueId().toString())) { // 检查被邀请者的 UUID 是否已经在 members 列表中
            members.add(member.getUniqueId().toString()); // 如果不在，就添加
        }
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

        //退出岛屿 (/is leave) (/is leave confirm)
        if (args.length == 1 && args[0].equalsIgnoreCase("leave")) {
            player.sendMessage(CU.t("&c如果你确定要退出岛屿，请再输入一遍 /is leave confirm"));
            return true;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("transfer")) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                player.sendMessage(CU.t("&c找不到指定的玩家"));
                return true;
            }

            File teamFile = new File(new File(plugin.getDataFolder(), "team"), player.getUniqueId().toString() + ".yml");
            if (!teamFile.exists()) {
                player.sendMessage(CU.t("&c你并不拥有岛屿"));
                return true;
            }
            FileConfiguration teamConfig = YamlConfiguration.loadConfiguration(teamFile);
            String teamMaster = teamConfig.getString("master");
            if (!player.getUniqueId().toString().equals(teamMaster)) {
                player.sendMessage(CU.t("&c只有岛主才能转让岛主"));
                return true;
            }
            List<String> members = teamConfig.getStringList("members");
            if (!members.contains(target.getUniqueId().toString())) {
                player.sendMessage(CU.t("&c他不是你的岛屿成员"));
                return true;
            }

            player.sendMessage(CU.t("&c如果你确定转让岛主，请再输入一遍 /is transfer " + target.getName() + " confirm"));
        } else if (args.length == 3 && args[0].equalsIgnoreCase("transfer") && args[2].equalsIgnoreCase("confirm")) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                player.sendMessage(CU.t("&c找不到指定的玩家"));
                return true;
            }

            File oldTeamFile = new File(new File(plugin.getDataFolder(), "team"), player.getUniqueId().toString() + ".yml");
            if (!oldTeamFile.exists()) {
                player.sendMessage(CU.t("&c你并不拥有岛屿"));
                return true;
            }
            FileConfiguration teamConfig = YamlConfiguration.loadConfiguration(oldTeamFile);
            String teamMaster = teamConfig.getString("master");
            if (!player.getUniqueId().toString().equals(teamMaster)) {
                player.sendMessage(CU.t("&c只有岛主才能转让岛主"));
                return true;
            }
            List<String> members = teamConfig.getStringList("members");
            if (!members.contains(target.getUniqueId().toString())) {
                player.sendMessage(CU.t("&c他不是你的岛屿成员"));
                return true;
            }

            members.remove(target.getUniqueId().toString());
            members.add(player.getUniqueId().toString());
            teamConfig.set("master", target.getUniqueId().toString());
            teamConfig.set("members", members);

            File newTeamFile = new File(new File(plugin.getDataFolder(), "team"), target.getUniqueId().toString() + ".yml"); // 创建新的岛屿配置文件
            if (newTeamFile.exists()) {
                newTeamFile.delete();
            }
            oldTeamFile.renameTo(newTeamFile);

            try {
                teamConfig.save(newTeamFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 给岛屿上的所有成员发送消息
            for (String memberId : members) {
                Player teamMember = Bukkit.getPlayer(UUID.fromString(memberId));
                if (teamMember != null) {
                    teamMember.sendMessage(CU.t("岛主已转让给 " + target.getName()));
                }
            }
            Player newMaster = Bukkit.getPlayer(UUID.fromString(teamConfig.getString("master")));
            if (newMaster != null) {
                newMaster.sendMessage(CU.t("岛主已转让给 " + target.getName()));
            }
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("leave") && args[1].equalsIgnoreCase("confirm")) {
            File teamDir = new File(plugin.getDataFolder(), "team");
            File[] teamFiles = teamDir.listFiles();
            if (teamFiles != null) {
                for (File teamFile : teamFiles) {
                    FileConfiguration teamConfig = YamlConfiguration.loadConfiguration(teamFile);
                    String master = teamConfig.getString("master");
                    List<String> members = teamConfig.getStringList("members");
                    if (master.equals(player.getUniqueId().toString())) {
                        if (members.isEmpty()) {
                            // 如果没有其他成员，直接删除岛屿文件
                            teamFile.delete();
                            player.sendMessage(CU.t("&b你已经退出了岛屿"));
                        } else {
                            player.sendMessage(CU.t("&c你需要先转让岛屿才能退出岛屿"));
                        }
                        return true;
                    } else if (members.contains(player.getUniqueId().toString())) {
                        members.remove(player.getUniqueId().toString());
                        teamConfig.set("members", members);
                        try {
                            teamConfig.save(teamFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // 删除玩家在playerlocation文件夹中的文件
                        File playerLocationFile = new File(new File(plugin.getDataFolder(), "playerlocation"), player.getUniqueId().toString() + ".yml");
                        if (playerLocationFile.exists()) {
                            playerLocationFile.delete();
                        }

                        // 给岛主发送消息
                        Player teamMaster = Bukkit.getPlayer(UUID.fromString(master));
                        if (teamMaster != null) {
                            teamMaster.sendMessage(CU.t("&6" + player.getName() + " &b退出了岛屿"));
                        }

                        // 给岛屿上的其他成员发送消息
                        for (String memberId : members) {
                            Player teamMember = Bukkit.getPlayer(UUID.fromString(memberId));
                            if (teamMember != null) {
                                teamMember.sendMessage(CU.t("&6" + player.getName() + " &b退出了岛屿"));
                            }
                        }

                        player.kickPlayer(CU.t("&6你退出了岛屿\n&b请重进服务器"));
                        return true;
                    }
                }
            }
            player.sendMessage(CU.t("&c你没有加入岛屿"));
            return true;
        }

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

                // 检查邀请者是否已经是其他岛屿的成员
                if (isPlayerInOtherIsland(player.getUniqueId())) {
                    player.sendMessage(CU.t("&c你不是岛主，没有权限邀请玩家加入岛屿"));
                    return true;
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
                target.sendMessage(CU.t("&b你&c被&6 " + player.getName() + " &b邀请加入他的岛屿")); // 这行代码会给被邀请者发送一条消息
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

                createTeam(inviter, player); // 调用 createTeam 方法

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
            } else if (args[0].equalsIgnoreCase("kick")) {
                kickMember(player, target);
            }
        }
        return true;
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
            completions.add("leave");
            completions.add("transfer");
        } else if (args.length == 2) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                completions.add(player.getName());
            }
        }
        return completions;
    }
}