package club.blocklife.touch.Item;

import club.blocklife.touch.Mobs.Alex.Alex;
import club.blocklife.touch.SkyblockMobs;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Autho BaicaijunOvO
 * @Github https://github.com/BaicaijunOvO
 * @Date 2024-02 00:02
 * @Tips XuFang is Gay!
 */
public class AlexSpawnItem implements Listener {
    public static ItemStack spawnItem3 = new ItemStack(Material.PAPER);
    public ItemStack spawnItem2 = new ItemStack(Material.PAPER);
    public ItemStack spawnItem1 = new ItemStack(Material.PAPER);
    public ItemStack createSpawnItem3(){
        ItemMeta spawnItemMeta = spawnItem3.getItemMeta();
        spawnItemMeta.setDisplayName("§l§6召唤Alex");
        List<String> lore = new ArrayList<>();
        lore.add("§l§6右键点击召唤Alex");
        lore.add("§l§6有10%的机会获得岩浆桶");
        lore.add("§l§6剩余使用次数：3");
        lore.add("§l§6BlocklifeServer");
        spawnItemMeta.setLore(lore);
        spawnItem3.setItemMeta(spawnItemMeta);
        createSpawnItem2();
        return spawnItem3;
    }
    public ItemStack createSpawnItem2(){
        ItemMeta spawnItemMeta = spawnItem2.getItemMeta();
        spawnItemMeta.setDisplayName("§l§6召唤Alex");
        List<String> lore = new ArrayList<>();
        lore.add("§l§6右键点击召唤Alex");
        lore.add("§l§6有10%的机会获得岩浆桶");
        lore.add("§l§6剩余使用次数：2");
        lore.add("§l§6BlocklifeServer");
        spawnItemMeta.setLore(lore);
        spawnItem2.setItemMeta(spawnItemMeta);
        createSpawnItem1();
        return spawnItem2;
    }
    public ItemStack createSpawnItem1(){
        ItemMeta spawnItemMeta = spawnItem1.getItemMeta();
        spawnItemMeta.setDisplayName("§l§6召唤Alex");
        List<String> lore = new ArrayList<>();
        lore.add("§l§6右键点击召唤Alex");
        lore.add("§l§6有10%的机会获得岩浆桶");
        lore.add("§l§6剩余使用次数：1");
        lore.add("§l§6BlocklifeServer");
        spawnItemMeta.setLore(lore);
        spawnItem1.setItemMeta(spawnItemMeta);
        return spawnItem1;
    }
    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem().getItemStack();
        if (item.getType() == Material.WATER_BUCKET && SkyblockMobs.playerProfiles.getBoolean(player.getName() + ".GetWaterBukkit") ) {
            ItemStack spawn = createSpawnItem3();
            player.sendMessage("§l§6您已获得水桶，获得召唤Alex的权利");
            player.getInventory().addItem(spawn);
            SkyblockMobs.playerProfiles.set(player.getName() + ".GetWaterBukkit", false);
            try {
                SkyblockMobs.playerProfiles.save(SkyblockMobs.playerProFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) throws IOException {
        Player player = e.getPlayer();
        Action action = e.getAction();
        // 检查玩家手中物品是否是水桶
        if (player.getItemInHand().getType() == Material.WATER_BUCKET  && SkyblockMobs.playerProfiles.getBoolean(player.getName() + ".GetWaterBukkit")) {
            ItemStack spawn = createSpawnItem3();
            player.sendMessage("§l§6您已获得水桶，获得召唤Alex的权利");
            player.getInventory().addItem(spawn);
            SkyblockMobs.playerProfiles.set(player.getName() + ".GetWaterBukkit", false);
            SkyblockMobs.playerProfiles.save(SkyblockMobs.playerProFile);
        }
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = e.getItem(); // 获取玩家右键点击的物品
            if (item != null && item.getItemMeta().getDisplayName().equals("§l§6召唤Alex") && item.getItemMeta().getLore().get(2).equals("§l§6剩余使用次数：3")) {
                e.getPlayer().sendMessage("§l§6Alex is coming!");
                e.getPlayer().getInventory().remove(spawnItem3);
                e.getPlayer().getInventory().addItem(spawnItem2);
                Alex.onStart(e.getPlayer().getLocation());
            }else if (item != null && item.getItemMeta().getDisplayName().equals("§l§6召唤Alex") && item.getItemMeta().getLore().get(2).equals("§l§6剩余使用次数：2")) {
                e.getPlayer().sendMessage("§l§6Alex is coming!");
                e.getPlayer().getInventory().remove(spawnItem2);
                e.getPlayer().getInventory().addItem(spawnItem1);
                Alex.onStart(e.getPlayer().getLocation());
            }else if (item != null && item.getItemMeta().getDisplayName().equals("§l§6召唤Alex") && item.getItemMeta().getLore().get(2).equals("§l§6剩余使用次数：1")) {
                e.getPlayer().sendMessage("§l§6Alex is coming!");
                e.getPlayer().getInventory().remove(spawnItem1);
                Alex.onStart(e.getPlayer().getLocation());
            }
        }
    }

}
