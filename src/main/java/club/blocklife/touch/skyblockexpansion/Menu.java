package club.blocklife.touch.skyblockexpansion;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @Autho BaicaijunOvO
 * @Github https://github.com/BaicaijunOvO
 * @Date 2024-02 13:22
 * @Tips XuFang is Gay!
 */
public class Menu implements Listener {
    public static ItemStack menuItem = new ItemStack(Material.NETHER_STAR);
    public static Inventory menuGui = Bukkit.createInventory(null, 54, "§l§6Skyblock菜单");

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ItemMeta menuMata = menuItem.getItemMeta();
        menuMata.setDisplayName("§l§6菜单");
        menuItem.setItemMeta(menuMata);
        event.getPlayer().getInventory().setItem(8, menuItem);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // 如果玩家在背包中点击了指定的物品，取消移动该物品的操作
        if (event.getCurrentItem() != null && event.getCurrentItem().isSimilar(menuItem)) {
            event.setCancelled(true);
        }


    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Action action = e.getAction();
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = e.getItem(); // 获取玩家右键点击的物品
            if (item != null && item.isSimilar(menuItem)) {
                player.openInventory(menuGui);
            }


        }
    }
}
