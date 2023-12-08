package de.koppy.basics.commands;

import de.koppy.basics.BasicSystem;
import de.koppy.basics.api.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Ping implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player p = (Player) sender;

        if(p.hasPermission("server.ping.other")) {
            if(args.length == 1) {
                String name = args[0];
                if(Bukkit.getPlayer(name) != null) {
                    Player t = Bukkit.getPlayer(name);
                    p.sendMessage(BasicSystem.getPrefix() + "§7"+t.getName()+"'s Ping: §3"+"Math.random()*20"+"§bms");
                    return false;
                }else {
                    p.sendMessage(BasicSystem.getPrefix() + PlayerProfile.getProfile(p.getUniqueId()).getMessage("playernotonline"));
                    return false;
                }
            }
        }
        p.sendMessage(BasicSystem.getPrefix() + "§7Ping: §3"+"Math.random()*20"+"§bms");

        return false;
    }
}
