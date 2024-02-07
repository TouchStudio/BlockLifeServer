package club.blocklife.touch.skyblockexpansion;

import club.blocklife.touch.skyblockexpansion.Tpa.Tpa;
import club.blocklife.touch.skyblockexpansion.Tpa.tpaAccept;
import club.blocklife.touch.skyblockexpansion.Tpa.tpaDeny;
import club.blocklife.touch.skyblockexpansion.lookPlayer.LookPlayerCommand;
import club.blocklife.touch.skyblockexpansion.lookPlayer.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public final class SkyBlockExpansion extends JavaPlugin {
    public static YamlConfiguration showitemconfig;
    public static Map<Integer, showItem.ShowItem> itemMAP = new HashMap();
    public static Random random = new Random();
    public static Map<String, Long> cooldowns = new HashMap();
    public static SkyBlockExpansion instance;
    public static Boolean PlaceholderAPI = false;
    public static List<String> tpaList = new ArrayList<>();
    public static YamlConfiguration lookPlayerconfig;

    public static SkyBlockExpansion getInstance() {
        return instance;
    }

    public static String cc(String s) {
        s = ChatColor.translateAlternateColorCodes('&', s);
        return s;
    }

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        File loolplayerConifgFile = new File(getDataFolder(), "lookplayer.yml");
        File showitemconfigFile = new File(getDataFolder(), "showitem.yml");
        if (!loolplayerConifgFile.exists()) {
            try {
                loolplayerConifgFile.createNewFile();
                FileWriter fileWriter = new FileWriter(loolplayerConifgFile);
                fileWriter.write("#��ʾ���������GUI�ϵı���\n" +
                        "GuiName: \"&4&l�����Ϣ\"\n" +
                        "#�Ƿ�������λ��(1.9���Ͽ���)\n" +
                        "Shield: false\n" +
                        "#�����Ϣ(���ͷ­)Lore�Զ���.֧��PlaceholderAPI����  \n" +
                        "#>>>   https://www.spigotmc.org/wiki/placeholderapi-placeholders/   <<<\n" +
                        "PlayerInfo:\n" +
                        "  - \"&c&M&l   &6&m&l   &e&m&l   &a&m&l   &b&m&l &a&m&l   &e&m&l   &6&m&l   &c&M&l   \"\n" +
                        "#�Ƿ�Ĭ�ϵ��Ҽ������л��������� (�����鿪��,��ʹ��EntityDamageByEntityEvent�¼�)\n" +
                        "LeftMouse: false\n" +
                        "\n" +
                        "Message:\n" +
                        "  NoPermission: \"��6[��bSkyBlockExpansion��6]��c��û��Ȩ��������\"\n" +
                        "  SuccessReload: \"��6[��bSkyBlockExpansion��6]��a�������!\"\n" +
                        "  FailReload: \"��6[��bSkyBlockExpansion��6]��c����ʧ��!��ǰ������̨�鿴����.\"\n" +
                        "  Main:\n" +
                        "  - \"��e/LookPlayer look ��6[���] ��7- ��b�鿴�����Ϣ\"\n" +
                        "  - \"��e/LookPlayer ��6reload ��7- ��4���ز��\"\n" +
                        "  PlayerNull: \"��6[��bLSkyBlockExpansion��6]��c��ҡ�d%player%��e�Ĳ�����.\"\n" +
                        "  Look: \"��6[��SkyBlockExpansion��6]��e��ѯ��ҡ�d%player%��e����Ϣ.\"");
                fileWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        if (!showitemconfigFile.exists()) {
            try {
                showitemconfigFile.createNewFile();
                FileWriter fileWriter = new FileWriter(showitemconfigFile);
                fileWriter.write("Setting:\n" +
                        "  #չʾ��Ʒ�Ƿ���Ҫ showitem.use Ȩ��\n" +
                        "  Permission: false\n" +
                        "  #�Ƿ���Ԥ����Ϣ\n" +
                        "  Preview: true\n" +
                        "  #չʾ��Ʒ��ȴʱ�� ��λ��\n" +
                        "  CoolDown: 5\n" +
                        "\n" +
                        "Gui:\n" +
                        "  Title: '��8չʾ��Ʒ ��8(��E�رս���)'\n" +
                        "  HoverShow: '��a����鿴'\n" +
                        "\n" +
                        "Message:\n" +
                        "  CoolDown: '��cϵͳ��7����c��ȴ��,���Ե� %cd% ��'\n" +
                        "  Format: '��c�����7����ҡ�f %player% ��7չʾ����Ʒ��f %itemname% ��7[����鿴]'\n" +
                        "  Preview: '��cϵͳ��7����ҡ�f %player% ��7�鿴����ġ�f %itemname% ��7��Ʒ'\n" +
                        "  ItemInHandNull: '��cϵͳ��7����c������û����Ʒ'\n" +
                        "  NoPermission: '��4��û��Ȩ��ִ��������'");
                fileWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        lookPlayerconfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "lookplayer.yml"));
        showitemconfig = YamlConfiguration.loadConfiguration(showitemconfigFile);

        instance = this;
        getServer().getPluginManager().registerEvents(new DeathHead(), this);
        getServer().getPluginManager().registerEvents(new showItem(), this);
        getServer().getPluginManager().registerEvents(new onInventoryClickEvent(),this);
        getCommand("showitem").setExecutor(new showItem());
        getCommand("seeinv").setExecutor(new seeInv());
        getCommand("tpa").setExecutor(new Tpa());
        getCommand("tpaccept").setExecutor(new tpaAccept());
        getCommand("tpadeny").setExecutor(new tpaDeny());
        getCommand("hat").setExecutor(new hatCommand());
        this.getCommand("LookPlayer").setExecutor(new LookPlayerCommand());
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            PlaceholderAPI = true;
            Bukkit.getConsoleSender().sendMessage("��6[��bSkyBlockExpansion��6]��e��⵽ǰ�ò��PlaceholderAPI");
        }

        Bukkit.getConsoleSender().sendMessage("��6[��bSkyBlockExpansion��6]��a�������");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getConsoleSender().sendMessage("��6[��bSkyBlockExpansion��6]��e���ж��");
    }
}
