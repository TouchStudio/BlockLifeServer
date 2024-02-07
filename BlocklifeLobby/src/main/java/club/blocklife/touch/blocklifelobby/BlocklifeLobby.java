package club.blocklife.touch.blocklifelobby;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

public final class BlocklifeLobby extends JavaPlugin {
    public static BlocklifeLobby instance;
    public static FileConfiguration config;
    @Override
    public void onEnable() {
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }
        config = getConfig();
        Bukkit.getConsoleSender().sendMessage("§6[§bBlocklifeLobby§6]§a插件加载");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getPluginManager().registerEvents(new LoginGUI(), this);
        instance = this;
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§6[§bBlocklifeLobby§6]§a插件卸载");
    }

    public static void connectToServer(Player player, String targetServer) {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteArray);

        try {
            out.writeUTF("Connect");
            out.writeUTF(targetServer);
        } catch (IOException e) {
            // 错误处理
            e.printStackTrace();
        }

        player.sendPluginMessage(BlocklifeLobby.instance, "BungeeCord", byteArray.toByteArray());
    }
}
