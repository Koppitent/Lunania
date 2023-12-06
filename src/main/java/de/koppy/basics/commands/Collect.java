package de.koppy.basics.commands;

import java.util.ArrayList;

import de.koppy.basics.api.SavedItems;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Collect implements CommandExecutor {

    public static ArrayList<Player> inCollect = new ArrayList<Player>();
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        Inventory inventory = new SavedItems(player.getUniqueId()).getInventory();
        inCollect.add(player);
        player.openInventory(inventory);

        return false;
    }
}