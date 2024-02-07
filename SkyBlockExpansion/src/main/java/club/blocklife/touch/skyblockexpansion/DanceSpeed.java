package club.blocklife.touch.skyblockexpansion;

import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
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
    public int sneakInt = 0;

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        if (event.isSneaking()) {
            Block centerBlock = event.getPlayer().getLocation().getBlock();
            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    for (int y = -1; y <= 1; y++) {
                        Block block = centerBlock.getRelative(x, y, z);
                        if (block.getBlockData() instanceof Ageable ageable) {
                            ageable.setAge(Math.min(ageable.getMaximumAge(), ageable.getAge() + 1));
                            block.setBlockData(ageable);
                        }
                    }
                }
            }


        }


    }


}
