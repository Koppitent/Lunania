package de.koppy.mission.commands;

import java.util.ArrayList;

import de.koppy.basics.api.Rank;
import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.mission.api.MissionHandler;
import de.koppy.mission.api.PlayerMission;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class Mission implements CommandExecutor {

    public static ArrayList<Player> inmenudaily = new ArrayList<Player>();
    public static ArrayList<Player> inmenuweekly = new ArrayList<Player>();
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return false;
        final Player player = (Player) sender;

        if(args.length == 0) {
            final Inventory inventory = Bukkit.createInventory(null, 9, "Daily");
            inmenudaily.add(player);
            for(String s : new PlayerMission(player.getUniqueId()).getDaily()) {
                inventory.addItem(new MissionHandler().getMission(s.split(":")[0]).getShowItem(player));
            }

            new BukkitRunnable() {
                public void run() {
                    inventory.clear();
                    for(String s : new PlayerMission(player.getUniqueId()).getDaily()) {
                        inventory.addItem(new MissionHandler().getMission(s.split(":")[0]).getShowItem(player));
                    }
                    if(inmenudaily.contains(player) == false) {
                        cancel();
                    }
                }
            }.runTaskTimer(LunaniaSystem.getPlugin(), 20, 20);
            player.openInventory(inventory);
        }else if(args.length == 1) {
            if(args[0].equalsIgnoreCase("weekly")) {
                final Inventory inventory = Bukkit.createInventory(null, 9, "Weekly");
                inmenuweekly.add(player);
                for(String s : new PlayerMission(player.getUniqueId()).getWeekly()) {
                    inventory.addItem(new MissionHandler().getMission(s.split(":")[0]).getShowItem(player));
                }

                new BukkitRunnable() {
                    public void run() {
                        inventory.clear();
                        for(String s : new PlayerMission(player.getUniqueId()).getWeekly()) {
                            inventory.addItem(new MissionHandler().getMission(s.split(":")[0]).getShowItem(player));
                        }
                        if(inmenuweekly.contains(player) == false) {
                            cancel();
                        }
                    }
                }.runTaskTimer(LunaniaSystem.getPlugin(), 20, 20);
                player.openInventory(inventory);
            }
        }

        if(player.hasPermission("server.mission")) {
            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("test")) {
                    player.sendMessage(Rank.getRank(player).toString());
                }
            }
        }

        return false;
    }



}