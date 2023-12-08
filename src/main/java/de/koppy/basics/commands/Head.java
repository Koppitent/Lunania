package de.koppy.basics.commands;

import de.koppy.basics.api.HeadObject;
import de.koppy.basics.api.InventoryHelper;
import de.koppy.basics.api.SkullMethods;
import de.koppy.basics.listener.InventoryEvents;
import de.koppy.lunaniasystem.LunaniaSystem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Head implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if(player.hasPermission("server.admin.head")) {
            if(args.length == 0) {

                Inventory inventory = Bukkit.createInventory(null, 6*9, "§3Head-Menu");
                setHeadMenu(1, inventory);
                InventoryEvents.headinv.add(player);
                player.openInventory(inventory);

            }else if(args.length == 1) {
                player.getInventory().addItem(SkullMethods.getSkullName(args[0]));
                player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§7Hier ist der Kopf.");
            }else if(args.length == 2) {
                if(args[0].equalsIgnoreCase("url")) {
                    player.getInventory().addItem(SkullMethods.getSkull(args[1]));
                    player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§7Hier ist der Kopf von der url.");
                }else if(args[0].equalsIgnoreCase("get")) {
                    ItemStack head = getHead(args[1]);
                    if(head == null) {
                        player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§cNicht in der Datenbank gefunden.");
                        return false;
                    }
                    player.getInventory().addItem(head);
                    player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§7Hier ist der Kopf von den saves.");
                }
            }else if(args.length == 4) {
                if(args[0].equalsIgnoreCase("save")) {
                    if(args[1].equalsIgnoreCase("url")) {
                        player.getInventory().addItem(SkullMethods.getSkull(args[2]));
                        player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§7Hier ist der Kopf von der url (+save).");
                        saveHead(SkullMethods.getSkull(args[2]), args[3]);
                    }else {
                        player.getInventory().addItem(SkullMethods.getSkullName(args[2]));
                        player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§7Hier ist der Kopf (+save).");
                        saveHead(SkullMethods.getSkullName(args[2]), args[3]);
                    }
                }
            }
        }

        return false;
    }

    public static List<HeadObject> getAllHeads() {
        List<HeadObject> heads = new ArrayList<>();
        File file = new File("plugins/HeadDatabase", "head.yml");
        if(!file.exists()) return heads;
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        for(String s : cfg.getKeys(false)) {
            heads.add(new HeadObject(s, cfg.getItemStack(s)));
        }
        return heads;
    }

    public static void setHeadMenu(int site, Inventory inventory) {
        List<HeadObject> heads = getAllHeads();
        heads.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
        inventory.clear();
        InventoryHelper.setRandGlassPane(inventory);
        int next = site+1;
        int before = site-1;
        ItemStack siteright = InventoryHelper.getArrow(true, next);
        ItemStack siteleft = InventoryHelper.getArrow(false, before);
        int maxsite = (heads.size() / (7*4));
        if(heads.size() % 7*4 != 0) maxsite++;
        if(site > 1) inventory.setItem(inventory.getSize()-8, siteleft);
        if(site < maxsite) inventory.setItem(inventory.getSize()-2, siteright);
        int beginindex = ((site-1)*7*4);
        int endindex = beginindex+(7*4);
        for(int i=beginindex; i<endindex; i++) {
            if(i >= heads.size()) continue;
            inventory.addItem(heads.get(i).getItemStack());
        }
    }

    public static ItemStack getHead(String name) {
        File file = new File("plugins/HeadDatabase", "head.yml");
        if(!file.exists()) return null;
        return YamlConfiguration.loadConfiguration(file).getItemStack(name);
    }

    public void saveHead(ItemStack skull, String name) {
        File file = new File("plugins/HeadDatabase", "head.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set(name, skull);
        try {
            cfg.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> out = new ArrayList<>();
        if(args.length == 1) {
            out.add("url");
            out.add("get");
            out.add("save");
        }else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("url")) {
                out.add("[url]");
            }else if(args[0].equalsIgnoreCase("get")) {
                out.add("[name]");
            }else if(args[0].equalsIgnoreCase("save")) {
                out.add("normal");
                out.add("url");
            }
        }else if(args.length == 3) {
            if(args[0].equalsIgnoreCase("save")) {
                if (args[1].equalsIgnoreCase("normal")) {
                    out.add("[name]");
                } else if (args[1].equalsIgnoreCase("url")) {
                    out.add("[url]");
                }
            }
        }else if(args.length == 4) {
            if(args[0].equalsIgnoreCase("save")) {
                out.add("[savename]");
            }
        }
        return out;
    }
}
