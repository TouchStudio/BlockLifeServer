package club.blocklife.touch.skyblockexpansion;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class hatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        ItemStack handItem;
        ItemStack helmetItem = null;
        if (player.getInventory().getItemInHand() != null){
            handItem = player.getInventory().getItemInHand();
        }else {
            player.sendMessage("��c[��bBloc��c]��f ��������û����Ʒ��");
            return false;
        }

        if (player.getInventory().getHelmet() != null){
            helmetItem = player.getInventory().getHelmet();
        }
        player.getInventory().setHelmet(handItem);
        player.getInventory().setItemInHand(helmetItem);
        player.sendMessage("��a[��bBlockLife��a]��f ����������ñ�ӣ�");
        return false;
    }
}
