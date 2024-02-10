package club.blocklife.touch.skyblockexpansion.Tpa;

import club.blocklife.touch.skyblockexpansion.SkyBlockExpansion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import static org.bukkit.Bukkit.getScheduler;

public class tpaAccept implements CommandExecutor , Listener {
    public static int num = 0;
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        SkyBlockExpansion.DamageList.add(player);
        num = 5;
        new BukkitRunnable() {
            @Override
            public void run() {
                SkyBlockExpansion.DamageList.remove(player);
            }
        }.runTaskLater(SkyBlockExpansion.instance,50L);
        Bukkit.getScheduler().runTaskTimer(SkyBlockExpansion.instance, new Runnable() {
            @Override
            public void run() {
            num -= 1;
            if (num <= 0){
                Bukkit.getScheduler().cancelTasks(SkyBlockExpansion.instance);
                num = 6;

            }
            }
        },0L,20L);
    }




    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (player != null) {
            if (SkyBlockExpansion.tpaList.contains(player.getName())) {
                    if (!SkyBlockExpansion.DamageList.contains(player)){
                        Location tpaPlayerLocation = player.getLocation();
                        Tpa.tpaPlayer.sendMessage("§f[§bSkyBlock§f]§f 将在3秒后传送");
                        player.sendMessage("§f[§bSkyBlock§f]§f 玩家将在3秒后传送到您身边");
                        new BukkitRunnable(){

                            @Override
                            public void run() {
                                Tpa.tpaPlayer.teleport(tpaPlayerLocation);
                                SkyBlockExpansion.tpaList.remove(player.getName());
                                Tpa.tpaPlayer.sendMessage("§f[§bSkyBlock§f]§f 已传送");
                                player.sendMessage("§f[§bSkyBlock§f]§f " + Tpa.tpaPlayer.getName() + " 已传送到您身边");
                                getScheduler().cancelTasks(SkyBlockExpansion.getPlugin(SkyBlockExpansion.class));
                            }
                        }.runTaskLater(SkyBlockExpansion.getPlugin(SkyBlockExpansion.class), 20 * 3);
                    }else {
                        player.sendMessage("[§bSkyblock§a]§f 您正处于战斗状态! 请在 " + num + " 秒后重试");
                        return false;
                    }


            } else {
                player.sendMessage("§f[§bSkyBlock§f]§f 您没有传送请求");
            }

        } else {
            Bukkit.getConsoleSender().sendMessage("§a[§bSkyBlockExpansion§a]§f 无法在控制台使用此命令");
        }
        return false;
    }
}
