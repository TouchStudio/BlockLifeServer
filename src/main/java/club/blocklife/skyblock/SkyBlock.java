package club.blocklife.skyblock;

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
        // Plugin startup logic
        ChatUtil.info("SkyBlock Start!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
