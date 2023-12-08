package de.koppy.bansystem.commands;

import de.koppy.bansystem.BanSystem;
import de.koppy.bansystem.api.MuteManager;
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

public class Unmute implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if(!player.hasPermission("server.admin.mute")) { player.sendMessage(BanSystem.getPrefix() + "§ckeine Rechte."); return false;}
            if(args.length == 1) {
                String playername = args[0];
                OfflinePlayer op = Bukkit.getOfflinePlayer(playername);
                UUID uuid = op.getUniqueId();
                MuteManager userban = new MuteManager(uuid);
                if(!userban.isMuted()) { player.sendMessage("§cDieser Spieler ist nicht gemutet."); return false; }
                userban.unmute(false);
                player.sendMessage(BanSystem.getPrefix() + "§7Der Spieler §e" + op.getName() + " §7wurde gemutet.");
            }
        }else {
            if(args.length == 1) {
                String playername = args[0];
                OfflinePlayer op = Bukkit.getOfflinePlayer(playername);
                UUID uuid = op.getUniqueId();
                MuteManager userban = new MuteManager(uuid);
                if(!userban.isMuted()) { commandSender.sendMessage("§cDieser Spieler ist nicht gemutet."); return false; }
                userban.unmute(false);
                commandSender.sendMessage("§7Der Spieler §e" + op.getName() + " §7wurde gemutet.");
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
