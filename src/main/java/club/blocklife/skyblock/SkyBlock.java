package club.blocklife.skyblock;

import club.blocklife.skyblock.commands.IslandCommand;
import club.blocklife.skyblock.listeners.IslandEvent;
import club.blocklife.skyblock.listeners.PlayerDamageEvent;
import club.blocklife.skyblock.listeners.PlayerDeathEvent;
import club.blocklife.skyblock.utils.ChatUtil;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @Author XuFang
 * @Github https://github.com/XuFangGG
 * @Date 2024-02-06 18:06:22
 */

public final class SkyBlock extends JavaPlugin {

    @Override
    public void onEnable() {
        ChatUtil.info("SkyBlock Start!");

        IslandCommand islandCommand = new IslandCommand(this);
        getServer().getPluginManager().registerEvents(new IslandEvent(this),this);
        getServer().getPluginManager().registerEvents(new PlayerDamageEvent(islandCommand), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathEvent(islandCommand), this);

        getCommand("is").setExecutor(islandCommand);
        getCommand("island").setExecutor(islandCommand);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
