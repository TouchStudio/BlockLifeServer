package club.blocklife.touch.skyblockexpansion.Tpa;

import club.blocklife.touch.skyblockexpansion.SkyBlockExpansion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static org.bukkit.Bukkit.getScheduler;

public class tpaAccept implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (player != null) {
            if (SkyBlockExpansion.tpaList.contains(player.getName())) {
                Location tpaPlayerLocation = player.getLocation();
                Tpa.tpaPlayer.sendMessage("��a[��bBlockLife��a]��f ����3�����");
                player.sendMessage("��a[��bBlockLife��a]��f ��ҽ���3����͵������");
                new BukkitRunnable(){

                    @Override
                    public void run() {
                        Tpa.tpaPlayer.teleport(tpaPlayerLocation);
                        SkyBlockExpansion.tpaList.remove(player.getName());
                        Tpa.tpaPlayer.sendMessage("��a[��bBlockLife��a]��f �Ѵ���");
                        player.sendMessage("��a[��bBlockLife��a]��f " + Tpa.tpaPlayer.getName() + " �Ѵ��͵������");
                        getScheduler().cancelTasks(SkyBlockExpansion.getPlugin(SkyBlockExpansion.class));
                    }
                }.runTaskLater(SkyBlockExpansion.getPlugin(SkyBlockExpansion.class), 20 * 3);

            } else {
                player.sendMessage("��a[��bBlockLife��a]��f ��û�д�������");
            }

        } else {
            Bukkit.getConsoleSender().sendMessage("��a[��bSkyBlockExpansion��a]��f �޷��ڿ���̨ʹ�ô�����");
        }
        return false;
    }
}
