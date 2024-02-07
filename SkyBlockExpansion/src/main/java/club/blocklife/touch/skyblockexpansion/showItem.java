package club.blocklife.touch.skyblockexpansion;


import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class showItem implements CommandExecutor, Listener {
    public static Inventory showItemGui = Bukkit.createInventory(null, 27, "展示物品");


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("showitem")) {
            String format;
            if (args.length > 0) {
                try {
                    int itemID = Integer.parseInt(args[0]);
                    ShowItem showItem = SkyBlockExpansion.itemMAP.get(itemID);
                    if (showItem != null) {
                        Inventory inv = Bukkit.getServer().createInventory(null, 27, SkyBlockExpansion.showitemconfig.getString("Gui.Title"));
                        inv.setItem(13, showItem.getItem());
                        if (sender instanceof Player) {
                            ((Player) sender).openInventory(inv);
                        }

                        if (SkyBlockExpansion.showitemconfig.getBoolean("Setting.Preview")) {
                            format = SkyBlockExpansion.showitemconfig.getString("Message.Preview");
                            ItemMeta itemmeta = showItem.getItem().getItemMeta();
                            String itemName = itemmeta.hasDisplayName() ? itemmeta.getDisplayName() : (showItem.getItem().getType() == Material.AIR ? "Unknown" : showItem.getItem().getType().name());
                            format = format.replace("%player%", ((Player) sender).getDisplayName()).replace("%itemname%", itemName);
                            Bukkit.getServer().getPlayer(showItem.getPlayerName()).sendMessage(format);
                        }
                    }
                } catch (Exception var13) {
                }
            } else if (sender instanceof Player) {
                Player player = (Player) sender;
                if (SkyBlockExpansion.showitemconfig.getBoolean("Setting.Permission") && !player.hasPermission("showitem.use")) {
                    player.sendMessage(SkyBlockExpansion.showitemconfig.getString("Message.NoPermission"));
                    return true;
                }

                if (SkyBlockExpansion.cooldowns.containsKey(player.getName())) {
                    long secondsLeft = SkyBlockExpansion.cooldowns.get(player.getName()) / 1000L + (long) SkyBlockExpansion.showitemconfig.getInt("Setting.CoolDown") - System.currentTimeMillis() / 1000L;
                    if (secondsLeft > 0L) {
                        player.sendMessage(SkyBlockExpansion.showitemconfig.getString("Message.CoolDown").replace("%cd%", String.valueOf(secondsLeft)));
                        return true;
                    }
                }

                SkyBlockExpansion.cooldowns.put(player.getName(), System.currentTimeMillis());
                ItemStack item = player.getInventory().getItemInHand();

                try {
                    String itemName = item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : (item.getType() == Material.AIR ? "Unknown" : item.getType().name());
                    format = SkyBlockExpansion.showitemconfig.getString("Message.Format");
                    format = format.replace("%player%", player.getDisplayName()).replace("%itemname%", itemName);
                    TextComponent message = new TextComponent(format);
                    message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(SkyBlockExpansion.showitemconfig.getString("Gui.HoverShow"))}));
                    int itemID = SkyBlockExpansion.random.nextInt();
                    ShowItem showItem = new ShowItem(player.getDisplayName(), item.clone());
                    SkyBlockExpansion.itemMAP.put(itemID, showItem);
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/showitem " + itemID));
                    Bukkit.getServer().spigot().broadcast(message);
                } catch (Exception var12) {
                    player.sendMessage(SkyBlockExpansion.showitemconfig.getString("Message.ItemInHandNull"));
                }
            }

            return true;
        } else {
            return false;
        }
    }

    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(SkyBlockExpansion.showitemconfig.getString("Gui.Title"))) {
            event.setCancelled(true);
        }

    }

    public class ShowItem {
        private String playerName;
        private ItemStack item;

        public ShowItem(String playerName, ItemStack item) {
            this.playerName = playerName;
            this.item = item;
        }

        public String getPlayerName() {
            return playerName;
        }

        public ItemStack getItem() {
            return item;
        }
    }


}

