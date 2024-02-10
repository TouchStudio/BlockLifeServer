package club.blocklife.touch.skyblockexpansion.Tpa;

import club.blocklife.touch.skyblockexpansion.SkyBlockExpansion;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


import static org.bukkit.Bukkit.getScheduler;

public class Tpa implements CommandExecutor {
    public static Player tpaPlayer;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        tpaPlayer = (Player) sender;
        if (tpaPlayer != null) {
            Player tpaTo = tpaPlayer.getServer().getPlayer(args[0]);
            if (tpaTo != null) {
                if (!SkyBlockExpansion.DamageList.contains(tpaPlayer)) {
                    tpaPlayer.sendMessage("§f[§bSkyBlock§f]§f 已发送传送请求");
                    TextComponent accept = new TextComponent("[同意]");
                    accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept"));

                    TextComponent deny = new TextComponent("[拒绝]");
                    deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpadeny"));

                    TextComponent message = new TextComponent("§f[§bSkyBlock§f]§f 玩家 " + sender.getName() + " 请求传送至你身边，请在60秒内接受\n§f[§bSkyBlock§f]§f ");
                    message.addExtra(accept);
                    message.addExtra("  ");
                    message.addExtra(deny);
                    tpaTo.spigot().sendMessage(ChatMessageType.CHAT,message);
                    SkyBlockExpansion.tpaList.add(tpaTo.getName());
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            SkyBlockExpansion.tpaList.remove(tpaTo.getName());
                            tpaTo.sendMessage("§f[§bSkyBlock§f]§f 请求过期");
                            getScheduler().cancelTasks(SkyBlockExpansion.getPlugin(SkyBlockExpansion.class));
                        }
                    }.runTaskLater(SkyBlockExpansion.getPlugin(SkyBlockExpansion.class), 20 * 60);
                }else {
                    ((Player) sender).getPlayer().sendMessage("[§bSkyblock§a]§f 您正处于战斗状态! 请在 " + tpaAccept.num + " 秒后重试");
                    return false;

                }
                } else {
                tpaPlayer.sendMessage("§f[§bSkyBlock§f]§f 玩家不在线或不存在");

            }


        } else {
            Bukkit.getConsoleSender().sendMessage("§a[§bSkyBlockExpansion§a]§f 无法在控制台使用此命令");
        }
        return false;
    }
}
