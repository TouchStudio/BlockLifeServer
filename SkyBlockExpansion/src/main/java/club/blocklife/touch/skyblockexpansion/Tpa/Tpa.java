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
                tpaPlayer.sendMessage("§a[§bBlockLife§a]§f 已发送传送请求");
                tpaTo.sendMessage("§a[§bBlockLife§a]§f 玩家§e " + tpaPlayer.getName() + " §f请求传送至你身边 请在60秒内接受 \n§a[§bBlockLife§a]§f 输入/tpaccept同意 输入/tpadeny拒绝");
                SkyBlockExpansion.tpaList.add(tpaTo.getName());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        SkyBlockExpansion.tpaList.remove(tpaTo.getName());
                        tpaTo.sendMessage("§a[§bBlockLife§a]§f 请求过期");
                        getScheduler().cancelTasks(SkyBlockExpansion.getPlugin(SkyBlockExpansion.class));
                    }
                }.runTaskLater(SkyBlockExpansion.getPlugin(SkyBlockExpansion.class), 20 * 60);
            } else {
                tpaPlayer.sendMessage("§a[§bBlockLife§a]§f 玩家不在线或不存在");
            }

        } else {
            Bukkit.getConsoleSender().sendMessage("§a[§bSkyBlockExpansion§a]§f 无法在控制台使用此命令");
        }
        return false;
    }
}
