package de.koppy.basics.commands;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Back implements CommandExecutor {

    public static HashMap<Player, Location> back = new HashMap<Player, Location>();
    public static ArrayList<Player> inback = new ArrayList<Player>();
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if(player.hasPermission("server.admin.back")) {
            if(back.containsKey(player)) {
                inback.add(player);
                player.teleport(back.get(player));
                player.sendMessage("§7Teleported back.");
            }else {
                player.sendMessage("§chuba buba.");
            }
        }

        return false;
    }

}