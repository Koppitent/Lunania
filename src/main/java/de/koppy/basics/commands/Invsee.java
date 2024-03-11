package de.koppy.basics.commands;

import de.koppy.basics.api.PlayerProfile;
import de.koppy.lunaniasystem.LunaniaSystem;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Invsee implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if(!player.hasPermission("server.admin.invsee")) return false;

        if(args.length == 1) {
            String name = args[0];
            if(Bukkit.getPlayer(name) != null) {
                Player t = Bukkit.getPlayer(name);
                if(t == player) return false;
                player.openInventory(t.getInventory());
            }else {
                PlayerProfile profile = PlayerProfile.getProfile(player.getUniqueId());
                player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + profile.getMessage("notonline"));
            }
        }

        return false;
    }
}
