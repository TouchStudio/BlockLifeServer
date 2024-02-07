package club.blocklife.skyblock.listeners;

import club.blocklife.skyblock.commands.IslandCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @Author XuFang
 * @Github https://github.com/XuFangGG
 * @Date 2024-02 17:41
 */
public class PlayerDeathEvent implements Listener {
    private final IslandCommand islandCommand;

    public PlayerDeathEvent(IslandCommand islandCommand) {
        this.islandCommand = islandCommand;
    }

    @EventHandler
    public void onPlayerDeath(org.bukkit.event.entity.PlayerDeathEvent event) {
        Player player = event.getEntity();
        islandCommand.stopCooldown(player);
    }
}
