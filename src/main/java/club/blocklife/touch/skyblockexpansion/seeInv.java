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
       if (sender.hasPermission("op")){
           Player seeInvplayer = Bukkit.getPlayer(args[0]);
           Player player = (Player) sender;
           if (seeInvplayer != null && seeInvplayer != player) {
               Inventory seeInv = seeInvplayer.getInventory();
               player.openInventory(seeInv);
           }else if (seeInvplayer == null){
               player.sendMessage("§f[§bSkyBlock§f]§f 请输入玩家名称");
           }else if (seeInvplayer == player){
               player.sendMessage("§f[§bSkyBlock§f]§f 无法查看自己的背包");
           }

       }else {
           sender.sendMessage("§f[§bSkyBlock§f]§f 你没有权限");
       }
        return false;
    }
}
