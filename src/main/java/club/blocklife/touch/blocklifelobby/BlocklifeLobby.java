package club.blocklife.touch.blocklifelobby;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class BlocklifeLobby extends JavaPlugin {
    public static BlocklifeLobby instance;
    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage("��6[��bBlocklifeLobby��6]��a�������");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getPluginManager().registerEvents(new LoginGUI(), this);
        instance = this;
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("��6[��bBlocklifeLobby��6]��a���ж��");
    }

    public static void connectToServer(Player player, String targetServer) {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteArray);

        try {
            out.writeUTF("Connect");
            out.writeUTF(targetServer);
        } catch (IOException e) {
            // ������
            e.printStackTrace();
        }

        player.sendPluginMessage(BlocklifeLobby.instance, "BungeeCord", byteArray.toByteArray());
    }
}
