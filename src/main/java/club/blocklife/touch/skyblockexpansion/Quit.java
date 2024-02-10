package club.blocklife.touch.skyblockexpansion;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @Autho BaicaijunOvO
 * @Github https://github.com/BaicaijunOvO
 * @Date 2024-02 17:34
 * @Tips XuFang is Gay!
 */
public class Quit implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && command.getName().equalsIgnoreCase("quit")) {
            Player player = (Player) sender;
            player.kickPlayer("Connection refused: no further information");
            return true;
        }
        return true;
    }
}
