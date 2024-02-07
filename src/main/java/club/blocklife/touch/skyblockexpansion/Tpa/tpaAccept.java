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
                Tpa.tpaPlayer.sendMessage("§a[§bBlockLife§a]§f 将在3秒后传送");
                player.sendMessage("§a[§bBlockLife§a]§f 玩家将在3秒后传送到您身边");
                new BukkitRunnable(){

                    @Override
                    public void run() {
                        Tpa.tpaPlayer.teleport(tpaPlayerLocation);
                        SkyBlockExpansion.tpaList.remove(player.getName());
                        Tpa.tpaPlayer.sendMessage("§a[§bBlockLife§a]§f 已传送");
                        player.sendMessage("§a[§bBlockLife§a]§f " + Tpa.tpaPlayer.getName() + " 已传送到您身边");
                        getScheduler().cancelTasks(SkyBlockExpansion.getPlugin(SkyBlockExpansion.class));
                    }
                }.runTaskLater(SkyBlockExpansion.getPlugin(SkyBlockExpansion.class), 20 * 3);

            } else {
                player.sendMessage("§a[§bBlockLife§a]§f 您没有传送请求");
            }

        } else {
            Bukkit.getConsoleSender().sendMessage("§a[§bSkyBlockExpansion§a]§f 无法在控制台使用此命令");
        }
        return false;
    }
}
