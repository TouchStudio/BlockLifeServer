package club.blocklife.touch.Listener;

import club.blocklife.touch.SkyblockMobs;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;

/**
 * @Autho BaicaijunOvO
 * @Github https://github.com/BaicaijunOvO
 * @Date 2024-02 19:43
 * @Tips XuFang is Gay!
 */
public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException {
        if (!SkyblockMobs.playerProfiles.contains(event.getPlayer().getName(),true)) {
            SkyblockMobs.playerProfiles.addDefault(event.getPlayer().getName() + ".GetWaterBukkit", true);
            SkyblockMobs.playerProfiles.save(SkyblockMobs.playerProFile);
        }

    }

}
