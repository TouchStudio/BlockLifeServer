package club.blocklife.touch.skyblockexpansion.lookPlayer;

import club.blocklife.touch.skyblockexpansion.SkyBlockExpansion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import java.util.ArrayList;
import java.util.Iterator;

import static club.blocklife.touch.skyblockexpansion.SkyBlockExpansion.PlaceholderAPI;


public class GuiUtil {
    public static Inventory inv = Bukkit.getServer().createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', SkyBlockExpansion.lookPlayerconfig.getString("GuiName")));
    public static void openGui(Player lookplayer, Player openplayer) {
        ItemStack item = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
        ItemMeta id = item.getItemMeta();
        item.setItemMeta(id);
        inv.setItem(0, item);
        inv.setItem(1, item);
        inv.setItem(2, item);
        inv.setItem(3, item);
        inv.setItem(4, item);
        inv.setItem(5, item);
        inv.setItem(6, item);
        inv.setItem(7, item);
        inv.setItem(8, item);
        inv.setItem(9, item);
        inv.setItem(10, item);
        inv.setItem(11, item);
        inv.setItem(12, item);
        inv.setItem(14, item);
        inv.setItem(15, item);
        inv.setItem(16, item);
        inv.setItem(17, item);
        inv.setItem(18, item);
        inv.setItem(20, item);
        inv.setItem(24, item);
        inv.setItem(26, item);
        inv.setItem(27, item);
        inv.setItem(28, item);
        inv.setItem(29, item);
        inv.setItem(30, item);
        inv.setItem(32, item);
        inv.setItem(33, item);
        inv.setItem(34, item);
        inv.setItem(35, item);
        inv.setItem(36, item);
        inv.setItem(37, item);
        inv.setItem(38, item);
        inv.setItem(39, item);
        inv.setItem(41, item);
        inv.setItem(42, item);
        inv.setItem(43, item);
        inv.setItem(44, item);
        inv.setItem(45, item);
        inv.setItem(46, item);
        inv.setItem(47, item);
        inv.setItem(48, item);
        inv.setItem(49, item);
        inv.setItem(50, item);
        inv.setItem(51, item);
        inv.setItem(52, item);
        inv.setItem(53, item);
        ItemMeta im;
        if (lookplayer.getInventory().getHelmet() != null) {
            inv.setItem(13, lookplayer.getInventory().getHelmet());
        } else {
            item = new ItemStack(Material.BARRIER);
            im = item.getItemMeta();
            im.setDisplayName("§c该装备槽目前是空的");
            item.setItemMeta(im);
            inv.setItem(13, item);
        }

        if (lookplayer.getInventory().getChestplate() != null) {
            inv.setItem(22, lookplayer.getInventory().getChestplate());
        } else {
            item = new ItemStack(Material.BARRIER);
            im = item.getItemMeta();
            im.setDisplayName("§c该装备槽目前是空的");
            item.setItemMeta(im);
            inv.setItem(22, item);
        }

        if (lookplayer.getInventory().getLeggings() != null) {
            inv.setItem(31, lookplayer.getInventory().getLeggings());
        } else {
            item = new ItemStack(Material.BARRIER);
            im = item.getItemMeta();
            im.setDisplayName("§c该装备槽目前是空的");
            item.setItemMeta(im);
            inv.setItem(31, item);
        }

        if (lookplayer.getInventory().getBoots() != null) {
            inv.setItem(40, lookplayer.getInventory().getBoots());
        } else {
            item = new ItemStack(Material.BARRIER);
            im = item.getItemMeta();
            im.setDisplayName("§c该装备槽目前是空的");
            item.setItemMeta(im);
            inv.setItem(40, item);
        }

        if (lookplayer.getItemInHand() != null && lookplayer.getInventory().getItemInHand().getType() != Material.AIR) {
            inv.setItem(21, lookplayer.getItemInHand());
        } else {
            item = new ItemStack(Material.BARRIER);
            im = item.getItemMeta();
            im.setDisplayName("§c该装备槽目前是空的");
            item.setItemMeta(im);
            inv.setItem(21, item);
        }

        if (SkyBlockExpansion.lookPlayerconfig.getBoolean("Shield")) {
            if (lookplayer.getInventory().getItemInHand() != null && lookplayer.getInventory().getItemInHand().getType() != Material.AIR){
                inv.setItem(23, lookplayer.getInventory().getItemInHand());
            } else {
                item = new ItemStack(Material.BARRIER);
                im = item.getItemMeta();
                im.setDisplayName("§c该装备槽目前是空的");
                item.setItemMeta(im);
                inv.setItem(23, item);
            }
        } else {
            item = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
            im = item.getItemMeta();
            im.setDisplayName("§c未启用盾牌位置");
            item.setItemMeta(im);
            inv.setItem(23, item);
        }

        item = new ItemStack(Material.DARK_OAK_BUTTON, 1);
        im = item.getItemMeta();
        im.setDisplayName("§f" + lookplayer.getName());
        ArrayList<String> lore = new ArrayList();

        String line;
        for (Iterator var8 = SkyBlockExpansion.lookPlayerconfig.getStringList("PlayerInfo").iterator(); var8.hasNext(); lore.add(line)) {
            line = (String) var8.next();
            line = SkyBlockExpansion.cc(line);
            if (PlaceholderAPI) {
                line = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(lookplayer, line);
            }
        }

        im.setLore(lore);
        item.setItemMeta(im);
        inv.setItem(19, item);
        item = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
        im = item.getItemMeta();
        im.setDisplayName("§c关闭页面");
        item.setItemMeta(im);
        inv.setItem(25, item);
        openplayer.openInventory(inv);
    }
}
