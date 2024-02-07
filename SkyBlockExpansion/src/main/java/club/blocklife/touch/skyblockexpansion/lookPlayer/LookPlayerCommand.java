package club.blocklife.touch.skyblockexpansion.lookPlayer;

import club.blocklife.touch.skyblockexpansion.SkyBlockExpansion;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import java.util.Iterator;
import java.util.List;


public class LookPlayerCommand implements CommandExecutor {
   public boolean onCommand(CommandSender sender, Command command, String lable, String[] args) {
      if (args.length != 0) {
         if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("LookPlayer.Admin")) {
               sender.sendMessage(SkyBlockExpansion.cc(SkyBlockExpansion.lookPlayerconfig.getString("Message.NoPermission")));
               return false;
            }

            try {
               SkyBlockExpansion.lookPlayerconfig.load(SkyBlockExpansion.getInstance().getDataFolder() + "/lookPlayerconfig.yml");
               sender.sendMessage(SkyBlockExpansion.cc(SkyBlockExpansion.lookPlayerconfig.getString("Message.SuccessReload")));
            } catch (Exception var8) {
               var8.printStackTrace();
               sender.sendMessage(SkyBlockExpansion.cc(SkyBlockExpansion.lookPlayerconfig.getString("Message.FailReload")));
            }
         }

         if (args[0].equalsIgnoreCase("look")) {
            Player p = (Player)sender;
            if (!p.hasPermission("LookPlayer.use")) {
               p.sendMessage(SkyBlockExpansion.cc(SkyBlockExpansion.lookPlayerconfig.getString("Message.NoPermission")));
               return false;
            } else {
               Player lp = Bukkit.getPlayer(args[1]);
               if (lp == null) {
                  sender.sendMessage(SkyBlockExpansion.cc(SkyBlockExpansion.lookPlayerconfig.getString("Message.PlayerNull").replace("%player%", args[1])));
                  return false;
               } else {
                  GuiUtil.openGui(lp, p);
                  sender.sendMessage(SkyBlockExpansion.cc(SkyBlockExpansion.lookPlayerconfig.getString("Message.Look").replace("%player%", lp.getName())));
                  return false;
               }
            }
         } else {
            return false;
         }
      } else {
         List<String> main = SkyBlockExpansion.lookPlayerconfig.getStringList("Message.Main");
         Iterator var7 = main.iterator();

         while(var7.hasNext()) {
            String i = (String)var7.next();
            i = SkyBlockExpansion.cc(i);
            sender.sendMessage(i);
         }

         return false;
      }
   }
}
