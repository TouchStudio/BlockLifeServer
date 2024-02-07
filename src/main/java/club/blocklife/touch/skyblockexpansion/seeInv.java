package club.blocklife.touch.skyblockexpansion;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class seeInv implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player seeInvplayer = Bukkit.getPlayer(args[0]);
        Player player = (Player) sender;
        if (seeInvplayer != null) {
            Inventory seeInv = seeInvplayer.getInventory();

            player.openInventory(seeInv);
        }
        return false;
    }
}
