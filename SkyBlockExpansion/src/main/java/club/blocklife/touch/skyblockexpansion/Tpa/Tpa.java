package club.blocklife.touch.skyblockexpansion.Tpa;

import club.blocklife.touch.skyblockexpansion.SkyBlockExpansion;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


import static org.bukkit.Bukkit.getScheduler;

public class Tpa implements CommandExecutor {
    public static Player tpaPlayer;
    ;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        tpaPlayer = (Player) sender;
        if (tpaPlayer != null) {
            Player tpaTo = tpaPlayer.getServer().getPlayer(args[0]);
            if (tpaTo != null) {
                tpaPlayer.sendMessage("��a[��bBlockLife��a]��f �ѷ��ʹ�������");
                tpaTo.sendMessage("��a[��bBlockLife��a]��f ��ҡ�e " + tpaPlayer.getName() + " ��f������������� ����60���ڽ��� \n��a[��bBlockLife��a]��f ����/tpacceptͬ�� ����/tpadeny�ܾ�");
                SkyBlockExpansion.tpaList.add(tpaTo.getName());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        SkyBlockExpansion.tpaList.remove(tpaTo.getName());
                        tpaTo.sendMessage("��a[��bBlockLife��a]��f �������");
                        getScheduler().cancelTasks(SkyBlockExpansion.getPlugin(SkyBlockExpansion.class));
                    }
                }.runTaskLater(SkyBlockExpansion.getPlugin(SkyBlockExpansion.class), 20 * 60);
            } else {
                tpaPlayer.sendMessage("��a[��bBlockLife��a]��f ��Ҳ����߻򲻴���");
            }

        } else {
            Bukkit.getConsoleSender().sendMessage("��a[��bSkyBlockExpansion��a]��f �޷��ڿ���̨ʹ�ô�����");
        }
        return false;
    }
}
