package de.koppy.basics.commands;

import de.koppy.basics.api.PlayerProfile;
import de.koppy.lunaniasystem.LunaniaSystem;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Fly implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if(!player.hasPermission("server.admin.fly")) return false;

        boolean flying = !player.getAllowFlight();

        player.setAllowFlight(flying);
        player.setFlying(flying);

        PlayerProfile profile = PlayerProfile.getProfile(player.getUniqueId());
        player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + profile.getMessage("flying").replace("%active%", ""+player.getAllowFlight()));

        return false;
    }
}
