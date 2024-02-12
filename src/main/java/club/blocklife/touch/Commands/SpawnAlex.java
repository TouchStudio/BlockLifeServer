package club.blocklife.touch.Commands;

import club.blocklife.touch.Mobs.Alex.Alex;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @Autho BaicaijunOvO
 * @Github https://github.com/BaicaijunOvO
 * @Date 2024-02 22:33
 * @Tips XuFang is Gay!
 */
public class SpawnAlex implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        Alex.onStart(player.getLocation());
        return false;
    }
}
