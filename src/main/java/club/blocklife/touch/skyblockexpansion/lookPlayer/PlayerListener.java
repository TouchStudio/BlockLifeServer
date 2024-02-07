package club.blocklife.touch.skyblockexpansion.lookPlayer;

import club.blocklife.touch.skyblockexpansion.SkyBlockExpansion;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;



public class PlayerListener implements Listener {
   @EventHandler
   public void onInteract(PlayerInteractEntityEvent e) {
      if (!SkyBlockExpansion.lookPlayerconfig.getBoolean("LeftMouse") && e.getPlayer().isSneaking() && e.getRightClicked().getType().equals(EntityType.PLAYER)) {
         Player lookplayer = (Player)e.getRightClicked();
         Player runplayer = e.getPlayer();
         if (!runplayer.hasPermission("LookPlayer.Interact")) {
            return;
         }

         if (lookplayer != null) {
            GuiUtil.openGui(lookplayer, runplayer);
         }
      }

   }

   @EventHandler
   public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
      if (e.getDamager() instanceof Player && e.getEntity() instanceof Player && SkyBlockExpansion.lookPlayerconfig.getBoolean("LeftMouse") && ((Player)e.getDamager()).isSneaking()) {
         Player lookplayer = (Player)e.getEntity();
         Player runplayer = (Player)e.getDamager();
         if (!runplayer.hasPermission("LookPlayer.Interact")) {
            return;
         }

         if (lookplayer != null) {
            e.setCancelled(true);
            GuiUtil.openGui(lookplayer, runplayer);
         }
      }

   }

   @EventHandler
   public void onInventoryClick(InventoryClickEvent e) {
      if (e.getWhoClicked() instanceof Player && e.getInventory().equals(GuiUtil.inv)) {
         e.setCancelled(true);
         if (e.getRawSlot() == 25) {
            e.getWhoClicked().closeInventory();
         }
      }

   }
}
