package de.koppy.basics.commands;

import de.koppy.lunaniasystem.LunaniaSystem;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Heal implements CommandExecutor {

    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player p = (Player) sender;

        if(p.hasPermission("server.admin.heal")) {
            if(args.length == 0) {
                p.setHealth(p.getMaxHealth());
                p.setFoodLevel(20);
                p.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§7You got §ehealed§7.");
            }else if(args.length == 1) {
                String name = args[0];
                if(Bukkit.getPlayer(name) != null) {
                    Player t = Bukkit.getPlayer(name);
                    t.setHealth(t.getMaxHealth());
                    t.setFoodLevel(20);
                    p.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§7You §ehealed§7 the player "+t.getName()+".");
                }else {
                    p.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§cThis player is not online.");
                }
            }
        }

        return false;
    }

}