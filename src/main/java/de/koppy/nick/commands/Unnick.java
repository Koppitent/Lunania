package de.koppy.nick.commands;
import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.nick.api.NickManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Unnick implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player p = (Player) sender;

        if(p.hasPermission("server.nick")) {
            NickManager nm = new NickManager(p);
            nm.unnick();
            p.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§7You got unnicked.");
        }

        return false;
    }

}