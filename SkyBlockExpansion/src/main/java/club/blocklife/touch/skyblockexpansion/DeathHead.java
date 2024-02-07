package club.blocklife.touch.skyblockexpansion;

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
        //��ȡ������ҵ�ʵ��
        Player player = event.getEntity();
        if (player.getKiller() != null) {
            //�������ͷ­
            ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
            //��ȡͷ­��Ԫ����
            SkullMeta headMeta = (SkullMeta) head.getItemMeta();
            //����ͷ­��������
            headMeta.setOwner(player.getName());
            //����ͷ­��Ԫ����
            head.setItemMeta(headMeta);
            //��ȡͷ­��Ԫ����
            ItemMeta itemMeta = head.getItemMeta();
            //����ͷ­������
            itemMeta.setDisplayName("��c��l" + player.getName() + "������ͷ­");
            if (player.getKiller().getInventory().getItemInHand().getItemMeta().getDisplayName() == null) {
                killerHeadItem = player.getKiller().getInventory().getItemInHand().getType().toString();
            } else {
                killerHeadItem = player.getKiller().getInventory().getItemInHand().getItemMeta().getDisplayName();
            }
            System.out.println(killerHeadItem);
            //����ͷ­������
            List<String> itemlore = new ArrayList<>();
            itemlore.add("��f��ɱ�� ��c" + player.getKiller().getName());
            itemlore.add("��fʹ�õ����� ��c" + killerHeadItem);
            itemMeta.setLore(itemlore);
            head.setItemMeta(itemMeta);
            //���������������ͷ­
            player.getWorld().dropItemNaturally(player.getLocation(), head);
        }
    }
}
