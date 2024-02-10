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
                player.sendMessage("§f[§bSkyBlock§f]§f 已拒绝传送请求");
                SkyBlockExpansion.tpaList.remove(player.getName());
            } else {
                player.sendMessage("§f[§bSkyBlock§f]§f 您没有传送请求");
            }
        } else {
            Bukkit.getConsoleSender().sendMessage("§a[§bSkyBlockExpansion§a]§f 无法在控制台使用此命令");
        }

        return false;
    }
}
