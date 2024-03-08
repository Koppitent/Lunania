package de.koppy.mission.commands;

import java.util.ArrayList;

import de.koppy.mission.api.MissionUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Mission implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return false;
        final Player player = (Player) sender;

        if(args.length == 0) {
            MissionUI ui = new MissionUI(player, "Daily");
            ui.getMenuDaily();
            player.openInventory(ui.getInventory());
        }else if(args.length == 1) {
            if(args[0].equalsIgnoreCase("weekly")) {
                MissionUI ui = new MissionUI(player, "Weekly");
                ui.getMenuWeekly();
                player.openInventory(ui.getInventory());
            }
        }

        return false;
    }
}