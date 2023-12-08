package de.koppy.basics.commands;

import de.koppy.lunaniasystem.LunaniaSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spawn implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if(player.hasPermission("server.admin.spawn")) {
            if(args.length == 1 && args[0].contains("set")) {
                LunaniaSystem.getServerInstance().setSpawnloc(player.getLocation());
                player.sendMessage("Spawn gesetzt uwu.");
                return false;
            }
        }

        if(LunaniaSystem.getServerInstance().getSpawnloc() != null) {
            player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§7Du wurdest zum spawn teleportiert.");
            player.teleport(LunaniaSystem.getServerInstance().getSpawnloc());
            return false;
        }else {
            player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§cDer Spawn ist nur eine faschistische Lüge!!!");
            return false;
        }

    }
}
