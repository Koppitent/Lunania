package de.koppy.bansystem.commands;

import de.koppy.bansystem.BanSystem;
import de.koppy.bansystem.api.BanManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Unban implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if(!player.hasPermission("server.admin.ban")) { player.sendMessage(BanSystem.getPrefix() + "§ckeine Rechte."); return false;}
            if(args.length == 1) {
                String playername = args[0];
                OfflinePlayer op = Bukkit.getOfflinePlayer(playername);
                UUID uuid = op.getUniqueId();
                BanManager userban = new BanManager(uuid);
                if(!userban.isBanned()) { player.sendMessage("§cDieser Spieler ist nicht gebannt."); return false; }
                userban.unban(false);
                player.sendMessage(BanSystem.getPrefix() + "§7Der Spieler §e" + op.getName() + " §7wurde entbannt.");
            }
        }else {
            if(args.length == 1) {
                String playername = args[0];
                OfflinePlayer op = Bukkit.getOfflinePlayer(playername);
                UUID uuid = op.getUniqueId();
                BanManager userban = new BanManager(uuid);
                if(!userban.isBanned()) { commandSender.sendMessage("§cDieser Spieler ist nicht gebannt."); return false; }
                userban.unban(false);
                commandSender.sendMessage("§7Der Spieler §e" + op.getName() + " §7wurde entbannt.");
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> out = new ArrayList<>();
        if(commandSender instanceof Player) out.add("<playername>");
        return out;
    }
}
