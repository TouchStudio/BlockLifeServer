package club.blocklife.touch.skyblockexpansion;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class onInventoryClickEvent implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().equals(showItem.showItemGui)) {
            event.setCancelled(true);
        }
    }
}
