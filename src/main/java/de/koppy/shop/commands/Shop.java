package de.koppy.shop.commands;

import java.util.ArrayList;
import java.util.List;

import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.shop.api.Adminshop;
import de.koppy.shop.api.ShopType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Shop implements CommandExecutor, TabCompleter {

    public static ArrayList<Player> ininv = new ArrayList<Player>();
    public static ArrayList<Player> inshop = new ArrayList<Player>();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if(args.length == 0) {
            player.sendMessage(" ");
            player.sendMessage("                  §8§l§ §6§lSHOP §8§l§");
            player.sendMessage("      §7https://www.shop.lunania.net/");
            player.sendMessage(" ");
        }else if(args.length == 1) {
            if(player.hasPermission("server.shop")) {
                if(args[0].equalsIgnoreCase("list")) {
                    player.sendMessage("§7List of §cAdmin§eShops§7:");
                    for(Adminshop f : Adminshop.adminshops) {
                        player.sendMessage("§7"+f.getName()+": §e"+f.getTitle() + " §7Type: §o" + f.getType() + " §7§o("+f.getRows()+" Rows)");
                    }
                }
            }else if(args[0].equalsIgnoreCase("help")) {

                player.sendMessage("§7§m|------------§r§7[§5Shop§7]§m------------|");
                player.sendMessage("§e");
                player.sendMessage("§e/shop create <name> §7- Create a Shop.");
                player.sendMessage("§e/shop edit <name> §7- Edit a Shop.");
                player.sendMessage("§e/shop settitle <name> <title> §7");
                player.sendMessage("§e/shop settype <name> <type> §7");
                player.sendMessage("§e/shop settitle <name> <slot> <title>");
                player.sendMessage("§e/shop setdescription <name> <slot> <descr>");
                player.sendMessage("§e/shop setbuyprice <name> <slot> <price>");
                player.sendMessage("§e/shop setsellprice <name> <slot> <price>");
                player.sendMessage("§e");
                return false;

            }
        }else if(args.length == 2) {
            if(player.hasPermission("server.shop")) {
                String name = args[1];
                if(args[0].equalsIgnoreCase("edit")) {
                    if(Adminshop.existAdminshop(name)) {
                        Adminshop shop = Adminshop.getAdminshop(name);
                        Inventory inventory = Bukkit.createInventory(null, shop.getRows()*9, name+" edit");
                        for(int i=0; i<inventory.getSize(); i++) {
                            if(shop.getShopItembyPosition(i) != null) {
                                inventory.setItem(i, shop.getShopItembyPosition(i).getItem());
                            }
                        }
                        ininv.add(player);
                        player.openInventory(inventory);
                    }else {
                        player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§cShopname doesnt exist.");
                    }
                }else if(args[0].equalsIgnoreCase("testopen")) {
                    if(Adminshop.existAdminshop(name)) {
                        Adminshop shop = Adminshop.getAdminshop(name);
                        Inventory inventory = Bukkit.createInventory(null, shop.getRows()*9, shop.getTitle().replace("&", "§"));
                        for(int i=0; i<inventory.getSize(); i++) {
                            if(shop.getShopItembyPosition(i) != null) {
                                inventory.setItem(i, shop.getShopItembyPosition(i).getShopItem());
                            }
                        }
                        inshop.add(player);
                        player.openInventory(inventory);
                    }else {
                        player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§cShopname doesnt exist.");
                    }
                }
            }
        }else if(args.length == 3) {
            if(player.hasPermission("server.shop")) {
                String name = args[1];
                if(args[0].equalsIgnoreCase("create")) {
                    if(args[2].matches("[0-9]+")) {
                        int rows = Integer.valueOf(args[2]);
                        if(Adminshop.existAdminshop(name) == false) {
                            Adminshop shop = new Adminshop(name, rows);
                            Inventory inventory = Bukkit.createInventory(null, shop.getRows()*9, name+" edit");
                            ininv.add(player);
                            player.openInventory(inventory);
                            player.sendMessage("§7Inventory §acreated§7.");
                        }else {
                            player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§cMust give a number.");
                        }
                    }else {
                        player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§cShopname does already exist.");
                    }
                }else if(args[0].equalsIgnoreCase("settitle")) {
                    if(Adminshop.existAdminshop(name)) {
                        String title = args[2];
                        Adminshop adminshop = Adminshop.getAdminshop(name);
                        adminshop.setTitle(title);
                        player.sendMessage("Title set");
                    }else {
                        player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§cNo Shop with that name.");
                    }
                }else if(args[0].equalsIgnoreCase("settype")) {
                    if(Adminshop.existAdminshop(name)) {
                        ShopType type = ShopType.fromString(args[2]);
                        Adminshop adminshop = Adminshop.getAdminshop(name);
                        adminshop.setShoptype(type);
                        player.sendMessage("Type set");
                    }else {
                        player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§cNo Shop with that name.");
                    }
                }
            }
        }else if(args.length == 4) {
            if(player.hasPermission("server.shop")) {
                String name = args[1];
                if(args[0].equalsIgnoreCase("setbuyprice")) {
                    if(Adminshop.existAdminshop(name)) {
                        if(args[2].matches("[0-9]+")) {
                            int index = Integer.valueOf(args[2]);
                            if(args[3].matches("[0.0-9.9]+")) {
                                double price = Double.valueOf(args[3]);
                                Adminshop adminshop = Adminshop.getAdminshop(name);
                                adminshop.getShopItembyPosition(index).setBuypriceperpiece(price);
                                player.sendMessage("Buyprice set.");
                            }else {
                                player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§cMust be a double number. (arg 3)");
                            }
                        }else {
                            player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§cMust be a index number. (arg 2)");
                        }
                    }else {
                        player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§cNo Shop with that name.");
                    }
                }else if(args[0].equalsIgnoreCase("setsellprice")) {
                    if(Adminshop.existAdminshop(name)) {
                        if(args[2].matches("[0-9]+")) {
                            int index = Integer.valueOf(args[2]);
                            if(args[3].matches("[0.0-9.9]+")) {
                                double price = Double.valueOf(args[3]);
                                Adminshop adminshop = Adminshop.getAdminshop(name);
                                adminshop.getShopItembyPosition(index).setSellpriceperpiece(price);
                                player.sendMessage("sellprice set");
                            }else {
                                player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§cMust be a double number. (arg 3)");
                            }
                        }else {
                            player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§cMust be a index number. (arg 2)");
                        }
                    }else {
                        player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§cNo Shop with that name.");
                    }
                }else if(args[0].equalsIgnoreCase("settitle")) {
                    if(Adminshop.existAdminshop(name)) {
                        if(args[2].matches("[0-9]+")) {
                            int index = Integer.valueOf(args[2]);
                            String title = args[3];
                            Adminshop adminshop = Adminshop.getAdminshop(name);
                            adminshop.getShopItembyPosition(index).setTitle(title);
                            player.sendMessage("Title set for Item at position " + index);
                        }else {
                            player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§cMust be a index number. (arg 2)");
                        }
                    }else {
                        player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§cNo Shop with that name.");
                    }
                }else if(args[0].equalsIgnoreCase("setdescription")) {
                    if(Adminshop.existAdminshop(name)) {
                        if(args[2].matches("[0-9]+")) {
                            int index = Integer.valueOf(args[2]);
                            String desc = args[3];
                            Adminshop adminshop = Adminshop.getAdminshop(name);
                            adminshop.getShopItembyPosition(index).setDescription(desc);
                            player.sendMessage("Description set");
                        }else {
                            player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§cMust be a index number. (arg 2)");
                        }
                    }else {
                        player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§cNo Shop with that name.");
                    }
                }
            }
        }

        return false;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> tcomplete = new ArrayList<String>();
        if(sender instanceof Player) {
            if(((Player)sender).hasPermission("server.admin.shop")) {
                if(args.length == 1) {
                    List<String> check = new ArrayList<String>();
                    check.add("create");
                    check.add("edit");
                    check.add("settitle");
                    check.add("setdescription");
                    check.add("settype");
                    check.add("setbuyprice");
                    check.add("setsellprice");
                    for(String s : check) if(s.startsWith(args[0])) tcomplete.add(s);
                }else if(args.length == 2) {
                    List<String> check = new ArrayList<String>();
                    for(Adminshop shop : Adminshop.adminshops) check.add(shop.getName());
                    for(String s : check) if(s.startsWith(args[1])) tcomplete.add(s);
                }else if(args.length == 3) {
                    List<String> check = new ArrayList<String>();
                    if(args[0].equalsIgnoreCase("settitle")) {
                        check.add("§7For Item: §cgive slot");
                        check.add("§7For Shop: §cgive Title");
                    }else if(args[0].equalsIgnoreCase("setdescription")) {
                        check.add("§cGive slot where item is located.");
                    }else if(args[0].equalsIgnoreCase("settype")) {
                        check.add("§cGive the Shoptype.");
                    }else if(args[0].equalsIgnoreCase("setbuyprice") || args[0].equalsIgnoreCase("setsellprice")) {
                        check.add("§cGive slot where item is located.");
                    }
                    for(String s : check) tcomplete.add(s);
                }else if(args.length == 4) {
                    List<String> check = new ArrayList<String>();
                    if(args[0].equalsIgnoreCase("settitle")) {
                        check.add("§7Give Title for item.");
                    }else if(args[0].equalsIgnoreCase("setdescription")) {
                        check.add("§cGive the description.");
                    }else if(args[0].equalsIgnoreCase("setbuyprice") || args[0].equalsIgnoreCase("setsellprice")) {
                        check.add("§cGive the itemprice.");
                    }
                    for(String s : check) tcomplete.add(s);
                }
            }
        }
        return tcomplete;
    }
}