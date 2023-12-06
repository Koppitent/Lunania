package de.koppy.basics.commands;

import de.koppy.basics.api.PlayerProfile;
import de.koppy.lunaniasystem.LunaniaSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Tpatoggle implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player p = (Player) sender;
        PlayerProfile profile = PlayerProfile.getProfile(p.getUniqueId());

        if(profile.isTpaAccept()) {
            p.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + profile.getMessage("tpaon"));
            profile.setTpaAccept(false);
        }else {
            p.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + profile.getMessage("tpaoff"));
            profile.setTpaAccept(true);
        }

        return false;
    }

}