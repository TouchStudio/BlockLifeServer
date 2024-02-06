package club.blocklife.touch.blocklifebungeecord;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

/**
 * @Autho BaicaijunOvO
 * @Github https://github.com/BaicaijunOvO
 * @Date 2024-02 下午 09:10
 * @Tips XuFang is Gay!
 */
public class Hub extends Command{

    public Hub() {
        super("hub");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        ProxiedPlayer player = (ProxiedPlayer) commandSender;
        player.sendMessage(ChatColor.GOLD +"正在传送...");
        player.connect(ProxyServer.getInstance().getServerInfo("lobby"));

    }
}
