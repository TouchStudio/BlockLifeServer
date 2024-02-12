package club.blocklife.touch;

import club.blocklife.touch.Commands.SkyblockMobsCommand;
import club.blocklife.touch.Commands.SpawnAlex;
import club.blocklife.touch.Item.AlexSpawnItem;
import club.blocklife.touch.Listener.PlayerJoin;
import club.blocklife.touch.Mobs.Alex.Alex;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public final class SkyblockMobs extends JavaPlugin {
    public static SkyblockMobs instance;
    public static YamlConfiguration playerProfiles;
    public static File playerProFile;

    @Override
    public void onEnable() {
        instance = this;
        if (!getDataFolder().exists()){
            getDataFolder().mkdir();
        }
        playerProFile = new File(getDataFolder(), "PlayerProfiles.yml");
        if (!playerProFile.exists()){
            try {
                playerProFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        playerProfiles = YamlConfiguration.loadConfiguration(playerProFile);

        getCommand("spawnalex").setExecutor(new SpawnAlex());
        getCommand("skyblockmobs").setExecutor(new SkyblockMobsCommand());
        getServer().getPluginManager().registerEvents(new Alex(), this);
        getServer().getPluginManager().registerEvents(new AlexSpawnItem(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(),this);

        getServer().getConsoleSender().sendMessage("§l§6[BlocklifeServer]§r§6SkyblockMobs插件加载完成");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage("§l§6[BlocklifeServer]§r§6SkyblockMobs插件卸载");
    }

    public static void reload(Player player){
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.disablePlugin(instance);
        pluginManager.enablePlugin(instance);
        player.sendMessage("§l§6[BlocklifeServer]§r§6SkyblockMobs插件重载完成");
    }
}
