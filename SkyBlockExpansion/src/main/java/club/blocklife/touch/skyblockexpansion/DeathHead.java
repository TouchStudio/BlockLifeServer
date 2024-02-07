package club.blocklife.touch.skyblockexpansion;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class DeathHead implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        String killerHeadItem = null;
        //获取死亡玩家的实体
        Player player = event.getEntity();
        if (player.getKiller() != null) {
            //创建玩家头颅
            ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
            //获取头颅的元数据
            SkullMeta headMeta = (SkullMeta) head.getItemMeta();
            //设置头颅的所有者
            headMeta.setOwner(player.getName());
            //设置头颅的元数据
            head.setItemMeta(headMeta);
            //获取头颅的元数据
            ItemMeta itemMeta = head.getItemMeta();
            //设置头颅的名称
            itemMeta.setDisplayName("§c§l" + player.getName() + "的死亡头颅");
            if (player.getKiller().getInventory().getItemInMainHand().getItemMeta().getDisplayName() != "") {
                killerHeadItem = player.getKiller().getInventory().getItemInMainHand().getItemMeta().getDisplayName();
            } else {
                killerHeadItem = player.getKiller().getInventory().getItemInMainHand().getType().toString();
            }
            System.out.println(killerHeadItem);
            //设置头颅的描述
            List<String> itemlore = new ArrayList<>();
            itemlore.add("§f击杀者 §c" + player.getKiller().getName());
            itemlore.add("§f使用的武器 §c" + killerHeadItem);
            itemMeta.setLore(itemlore);
            head.setItemMeta(itemMeta);
            //在玩家死亡处掉落头颅
            player.getWorld().dropItemNaturally(player.getLocation(), head);
        }
    }
}
