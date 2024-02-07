package club.blocklife.skyblock.listeners;

import club.blocklife.skyblock.commands.IslandCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * @Author XuFang
 * @Github https://github.com/XuFangGG
 * @Date 2024-02 17:31
 */
public class PlayerDamageEvent implements Listener {

    private final IslandCommand islandCommand;

    public PlayerDamageEvent(IslandCommand islandCommand) {
        this.islandCommand = islandCommand;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        islandCommand.startCooldown(player);
    }
}
