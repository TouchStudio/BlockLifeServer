package club.blocklife.touch.blocklifebungeecord;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;

public final class Blocklife_BungeeCord extends Plugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getProxy().getPluginManager().registerCommand(this,new Hub());
        getLogger().info(ChatColor.BLUE + "²å¼þ¼ÓÔØ");
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.BLUE + "²å¼þÐ¶ÔØ");
    }
}
