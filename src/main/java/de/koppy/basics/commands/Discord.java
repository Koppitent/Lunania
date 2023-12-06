package de.koppy.basics.commands;
import de.koppy.lunaniasystem.LunaniaSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Discord implements CommandExecutor{

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player))
            return false;

        ChatColor dccolor = ChatColor.of("#7289da");

        Player player = (Player) sender;
        player.sendMessage(" ");
        player.sendMessage("                §8§l§ "+dccolor+"§lDISCORD §8§l§");
        player.sendMessage("      §7"+ LunaniaSystem.getServerInstance().getDiscordlink());
        player.sendMessage(" ");

        return false;
    }

}