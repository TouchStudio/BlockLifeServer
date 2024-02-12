package club.blocklife.touch.Commands;

import club.blocklife.touch.SkyblockMobs;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @Autho BaicaijunOvO
 * @Github https://github.com/BaicaijunOvO
 * @Date 2024-02 21:25
 * @Tips XuFang is Gay!
 */
public class SkyblockMobsCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        if (!player.hasPermission("op")){
            player.sendMessage("§a[§bBlockLife§a]§f 你没有权限使用这个命令");
            return false;
        }
        if (strings.length == 0) {
            player.sendMessage("§l§6[BlocklifeServer]§r§6SkyblockMobs插件指令帮助");
            player.sendMessage("§l§6/skyblockmobs help §r§6查看帮助");
            player.sendMessage("§l§6/skyblockmobs reload §r§6重载插件");
            return true;
        }else if (strings.length == 1 && strings[0].equalsIgnoreCase("help")) {
            player.sendMessage("§l§6[BlocklifeServer]§r§6SkyblockMobs插件指令帮助");
            player.sendMessage("§l§6/skyblockmobs help §r§6查看帮助");
            player.sendMessage("§l§6/skyblockmobs reload §r§6重载插件");
            return true;
        }else if (strings.length == 1 && strings[0].equalsIgnoreCase("reload")) {
            SkyblockMobs.reload(player);
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }

        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("help");
            completions.add("reload");
        } else if (args.length == 2) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                completions.add(player.getName());
            }
        }
        return completions;
    }
}
