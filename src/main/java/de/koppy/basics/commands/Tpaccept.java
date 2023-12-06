package de.koppy.basics.commands;

import de.koppy.basics.api.PlayerProfile;
import de.koppy.basics.commands.Tpa;
import de.koppy.lunaniasystem.LunaniaSystem;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Tpaccept implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player p = (Player) sender;
        PlayerProfile profile = PlayerProfile.getProfile(p.getUniqueId());
        if(args.length == 1) {
            String name = args[0];
            if(Bukkit.getPlayer(name) != null) {
                Player targettodadd = Bukkit.getPlayer(name);
                if(Tpa.existInRequest(p, targettodadd)) {
                    Tpa.removeFromRequest(p, targettodadd);
                    PlayerProfile tprofile = PlayerProfile.getProfile(targettodadd.getUniqueId());
                    targettodadd.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + tprofile.getMessage("teleportacceptedfrom").replace("%player%", p.getName()));
                    p.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + profile.getMessage("teleportacceptedfrom").replace("%player%", targettodadd.getName()));
                    targettodadd.teleport(p.getLocation());
                }else {
                    p.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + profile.getMessage("notpasentfrom").replace("%player%", name));
                }
            }else {
                p.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + profile.getMessage("playernotonline"));
            }
        }

        return false;
    }

}