package club.blocklife.touch.blocklifelobby;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

import java.util.ArrayList;
import java.util.List;

public class LoginGUI implements Listener {


    public Inventory menu = Bukkit.createInventory(null, 9, "菜单");
    public ItemStack menuItem = new ItemStack(Material.NETHER_STAR);


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        ItemStack s = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta smata = s.getItemMeta();
        ItemMeta menuItemMeta = menuItem.getItemMeta();
        menuItemMeta.setDisplayName(ChatColor.BLUE + "菜单");
        smata.setDisplayName(ChatColor.GREEN + "生存");
        List<String> slora = new ArrayList<>();
        List<String> mlora = new ArrayList<>();
        mlora.add("§lBlocklifeServer菜单");
        slora.add("§l进入生存服");
        menuItemMeta.setLore(mlora);
        smata.setLore(slora);
        s.setItemMeta(smata);
        menuItem.setItemMeta(menuItemMeta);
        menu.setItem(3, s);
        e.getPlayer().getInventory().setItem(4, menuItem);
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Action action = e.getAction();

        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = e.getItem(); // 获取玩家右键点击的物品
            if (item != null && item.isSimilar(menuItem)) {
                player.openInventory(menu);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equals("菜单")) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.GRASS_BLOCK) {
                if (e.getWhoClicked() instanceof Player player) {
                    BlocklifeLobby.connectToServer(player, "shengcun");

                }
            }
        }


    }
}
