package de.koppy.server.commands;

import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.server.Server;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class test implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(sender instanceof ConsoleCommandSender) {

            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("serverclass")) {
                    Server server = LunaniaSystem.getServerInstance();
                    Bukkit.getConsoleSender().sendMessage("");
                    Bukkit.getConsoleSender().sendMessage("Server Name: " + server.getName());
                    Bukkit.getConsoleSender().sendMessage("Server Maxplayer: " + server.getMaxplayer());
                    Bukkit.getConsoleSender().sendMessage("Server Prefix: " + server.getPrefix());
                    Bukkit.getConsoleSender().sendMessage("");
                }
            }

        }else {
            Player p = (Player) sender;
            p.sendMessage("§cDieses Kommando ist NUR für die Konsole.");
        }

        return false;
    }
}
