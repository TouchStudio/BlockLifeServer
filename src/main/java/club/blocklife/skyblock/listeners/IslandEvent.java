package club.blocklife.skyblock.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class IslandEvent implements Listener {
    private final JavaPlugin plugin;

    public IslandEvent(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        File playerFile = new File(new File(plugin.getDataFolder(), "playerlocation"), player.getUniqueId().toString() + ".yml");
        FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);

        if (!playerFile.exists()) {
            Location spawnLocation = getRandomLocation();
            spawnLocation.getBlock().getRelative(0, -1, 0).setType(Material.BEDROCK);
            player.teleport(spawnLocation);
            player.setBedSpawnLocation(spawnLocation, true);

            playerConfig.set("location.x", spawnLocation.getX());
            playerConfig.set("location.y", spawnLocation.getY());
            playerConfig.set("location.z", spawnLocation.getZ());
            try {
                playerConfig.save(playerFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Location getRandomLocation() {
        Random random = new Random();
        int x = random.nextInt(4500) + 500;
        int y = -60;
        int z = random.nextInt(4500) + 500;

        x = random.nextBoolean() ? -x : x;
        z = random.nextBoolean() ? -z : z;

        return new Location(Bukkit.getWorlds().get(0), x, y, z);
    }
}