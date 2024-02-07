package club.blocklife.skyblock.commands;

import club.blocklife.skyblock.utils.CU;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class IslandCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private final Map<UUID, Long> cooldownMap = new HashMap<>();

    public IslandCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void startCooldown(Player player) {
        cooldownMap.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public void stopCooldown(Player player) {
        cooldownMap.remove(player.getUniqueId());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CU.t("&c你必须是一个玩家才能使用此命令"));
            return true;
        }

        Player player = (Player) sender;
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
    }
}