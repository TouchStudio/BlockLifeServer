package club.blocklife.touch.skyblockexpansion;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

/**
 * @Autho BaicaijunOvO
 * @Github https://github.com/BaicaijunOvO
 * @Date 2024-02 20:15
 * @Tips XuFang is Gay!
 */
public class DanceSpeed implements Listener {

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (event.isSneaking()) {
            Block block = player.getLocation().getBlock().getRelative(0, 0, 0);
            if (block.getType() == Material.OAK_SAPLING || block.getType() == Material.BIRCH_SAPLING || block.getType() == Material.SPRUCE_SAPLING || block.getType() == Material.JUNGLE_SAPLING || block.getType() == Material.ACACIA_SAPLING || block.getType() == Material.DARK_OAK_SAPLING) {
                player.sendMessage("§a你正在加速树苗生长!");
                block.applyBoneMeal(BlockFace.UP);
            }
        }
    }


}
