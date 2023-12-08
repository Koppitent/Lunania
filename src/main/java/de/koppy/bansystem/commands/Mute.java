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

public class Mute implements CommandExecutor, TabCompleter {


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
                if(userban.isMuted()) { player.sendMessage(BanSystem.getPrefix() + "§cDieser Spieler ist bereits gemutet."); return false; }
                userban.mute(31556952000L *10L, "§cAdmin-Mute", player.getUniqueId().toString());
                player.sendMessage(BanSystem.getPrefix() + "§7Der Spieler §e" + op.getName() + " §7wurde §cpermanent §7gemutet.");
            }else if(args.length == 2) {
                int id = Integer.parseInt(args[1]);
                String playername = args[0];
                OfflinePlayer op = Bukkit.getOfflinePlayer(playername);
                UUID uuid = op.getUniqueId();
                MuteManager userban = new MuteManager(uuid);
                if(userban.isMuted()) { player.sendMessage(BanSystem.getPrefix() + "§cDieser Spieler ist bereits gemutet."); return false; }
                player.sendMessage(BanSystem.getPrefix() + userban.muteID(id, player.getUniqueId().toString()));
            }else if(args.length >= 3) {
                long time = getTimeFromFormat(args[1]);
                if(time < 0) { player.sendMessage(BanSystem.getPrefix() + "§cFalsche Formatierung der Mutedauer."); return false; }
                String reason = "";
                for(int i=2; i< args.length; i++) {
                    reason = reason + " " + args[i];
                }
                String playername = args[0];
                OfflinePlayer op = Bukkit.getOfflinePlayer(playername);
                UUID uuid = op.getUniqueId();
                MuteManager userban = new MuteManager(uuid);
                if(userban.isMuted()) { player.sendMessage(BanSystem.getPrefix() + "§cDieser Spieler ist bereits gemutet."); return false; }
                userban.mute(time, reason.replace("&", "§"), player.getUniqueId().toString());
                player.sendMessage(BanSystem.getPrefix() + "§7Der Spieler §e" + op.getName() + " §7wurde §a" + args[1] + " §7lang für §3" + reason + " §7gemutet.");
            }
        }else {
            if(args.length == 1) {
                String playername = args[0];
                OfflinePlayer op = Bukkit.getOfflinePlayer(playername);
                UUID uuid = op.getUniqueId();
                MuteManager userban = new MuteManager(uuid);
                if(userban.isMuted()) { commandSender.sendMessage("§cDieser Spieler ist bereits gemutet."); return false; }
                userban.mute(31556952000L *10L, "§cAdmin-mute", "Console");
                commandSender.sendMessage("§7Der Spieler §e" + op.getName() + " §7wurde §cpermanent §7gemutet.");
            }else if(args.length == 2) {
                int id = Integer.parseInt(args[1]);
                String playername = args[0];
                OfflinePlayer op = Bukkit.getOfflinePlayer(playername);
                UUID uuid = op.getUniqueId();
                MuteManager userban = new MuteManager(uuid);
                if(userban.isMuted()) { commandSender.sendMessage("§cDieser Spieler ist bereits gemutet."); return false; }
                commandSender.sendMessage(userban.muteID(id, "Console"));
            }else if(args.length >= 3) {
                long time = getTimeFromFormat(args[1]);
                if(time < 0) { commandSender.sendMessage("§cFalsche Formatierung der Mutedauer."); return false; }
                String reason = "";
                for(int i=2; i< args.length; i++) {
                    reason = reason + " " + args[i];
                }
                String playername = args[0];
                OfflinePlayer op = Bukkit.getOfflinePlayer(playername);
                UUID uuid = op.getUniqueId();
                MuteManager userban = new MuteManager(uuid);
                if(userban.isMuted()) { commandSender.sendMessage("§cDieser Spieler ist bereits gemutet."); return false; }
                userban.mute(time, reason.replace("&", "§"), "Console");
                commandSender.sendMessage("§7Der Spieler §e" + op.getName() + " §7wurde §a" + args[1] + " §7lang für §3" + reason + " §7gemutet.");
            }
        }

        return false;
    }

    private long getTimeFromFormat(String timeformat) {
        if(!timeformat.substring(0, timeformat.length()-1).matches("[0-9]+")) return -1L;
        long mult = Long.parseLong(timeformat.substring(0, timeformat.length()-1));
        String tf = timeformat.substring(timeformat.length()-1);
        long time = -1;
        switch (tf) {
            case "y":
                time = 1000L*60L*60L*24L*365L*mult;
                break;
            case "m":
                time = 1000L*60L*60L*24L*30L*mult;
                break;
            case "d":
                time = 1000L*60L*60L*24L*mult;
                break;
            case "h":
                time = 1000L*60L*60L*mult;
                break;
            default:
                return -1L;
        }
        return time;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> out = new ArrayList<>();

        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if(player.hasPermission("server.admin.mute")) {
                if(args.length == 1) {
                    out.add("<playername>");
                }else if(args.length == 2) {
                    out.add("<ID>");
                    out.add("<time>");
                }else if(args.length == 3) {
                    out.add("reason");
                }
            }
        }

        return out;
    }
}
