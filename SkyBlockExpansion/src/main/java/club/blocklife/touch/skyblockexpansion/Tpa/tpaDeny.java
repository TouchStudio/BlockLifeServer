package club.blocklife.touch.skyblockexpansion.Tpa;

import club.blocklife.touch.skyblockexpansion.SkyBlockExpansion;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class tpaDeny implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (player != null) {
            if (SkyBlockExpansion.tpaList.contains(player.getName())) {
                player.sendMessage("��a[��bBlockLife��a]��f �Ѿܾ���������");
                SkyBlockExpansion.tpaList.remove(player.getName());
            } else {
                player.sendMessage("��a[��bBlockLife��a]��f ��û�д�������");
            }
        } else {
            Bukkit.getConsoleSender().sendMessage("��a[��bSkyBlockExpansion��a]��f �޷��ڿ���̨ʹ�ô�����");
        }

        return false;
    }
}
