package de.koppy.basics.commands;

import de.koppy.basics.api.PlayerProfileUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Profile implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        PlayerProfileUI ui = new PlayerProfileUI(((Player) sender).getUniqueId());
        ui.getMainMenu();
        ((Player) sender).openInventory(ui.getInventory());
        return false;
    }
}
