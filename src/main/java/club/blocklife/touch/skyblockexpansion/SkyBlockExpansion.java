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
                fileWriter.write("#显示在玩家箱子GUI上的标题\n" +
                        "GuiName: \"&4&l玩家信息\"\n" +
                        "#是否开启盾牌位置(1.9以上开启)\n" +
                        "Shield: false\n" +
                        "#玩家信息(左侧头颅)Lore自定义.支持PlaceholderAPI变量  \n" +
                        "#>>>   https://www.spigotmc.org/wiki/placeholderapi-placeholders/   <<<\n" +
                        "PlayerInfo:\n" +
                        "  - \"&c&M&l   &6&m&l   &e&m&l   &a&m&l   &b&m&l &a&m&l   &e&m&l   &6&m&l   &c&M&l   \"\n" +
                        "#是否将默认的右键操作切换至鼠标左键 (不建议开启,将使用EntityDamageByEntityEvent事件)\n" +
                        "LeftMouse: false\n" +
                        "\n" +
                        "Message:\n" +
                        "  NoPermission: \"§6[§bSkyBlockExpansion§6]§c你没有权限这样做\"\n" +
                        "  SuccessReload: \"§6[§bSkyBlockExpansion§6]§a重载完成!\"\n" +
                        "  FailReload: \"§6[§bSkyBlockExpansion§6]§c重载失败!请前往控制台查看报错.\"\n" +
                        "  Main:\n" +
                        "  - \"§e/LookPlayer look §6[玩家] §7- §b查看玩家信息\"\n" +
                        "  - \"§e/LookPlayer §6reload §7- §4重载插件\"\n" +
                        "  PlayerNull: \"§6[§bLSkyBlockExpansion§6]§c玩家§d%player%§e的不存在.\"\n" +
                        "  Look: \"§6[§SkyBlockExpansion§6]§e查询玩家§d%player%§e的信息.\"");
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
                        "  #展示物品是否需要 showitem.use 权限\n" +
                        "  Permission: false\n" +
                        "  #是否开启预览消息\n" +
                        "  Preview: true\n" +
                        "  #展示物品冷却时间 单位秒\n" +
                        "  CoolDown: 5\n" +
                        "\n" +
                        "Gui:\n" +
                        "  Title: '§8展示物品 §8(按E关闭界面)'\n" +
                        "  HoverShow: '§a点击查看'\n" +
                        "\n" +
                        "Message:\n" +
                        "  CoolDown: '§c系统§7》§c冷却中,请稍等 %cd% 秒'\n" +
                        "  Format: '§c公告§7》玩家§f %player% §7展示了物品§f %itemname% §7[点击查看]'\n" +
                        "  Preview: '§c系统§7》玩家§f %player% §7查看了你的§f %itemname% §7物品'\n" +
                        "  ItemInHandNull: '§c系统§7》§c你手中没有物品'\n" +
                        "  NoPermission: '§4你没有权限执行这个命令！'");
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
            Bukkit.getConsoleSender().sendMessage("§6[§bSkyBlockExpansion§6]§e检测到前置插件PlaceholderAPI");
        }

        Bukkit.getConsoleSender().sendMessage("§6[§bSkyBlockExpansion§6]§a插件加载");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getConsoleSender().sendMessage("§6[§bSkyBlockExpansion§6]§e插件卸载");
    }
}
