package club.blocklife.skyblock.util;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

/**
 * @Author XuFang
 * @Github https://github.com/XuFangGG
 * @Date 2024-02-06 18:06:22
 */

public class ChatUtil {
    public static void say(String s) {
        CommandSender sender = Bukkit.getConsoleSender();
        sender.sendMessage(s);
    }
    public static void debug(String debugsay) {
        CommandSender sender = Bukkit.getConsoleSender();
        sender.sendMessage("Debug: " + debugsay);
    }

    public static void info(String infosay) {
        CommandSender sender = Bukkit.getConsoleSender();
        sender.sendMessage("Info: " + infosay);
    }
}
