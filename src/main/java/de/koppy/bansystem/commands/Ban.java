package de.koppy.bansystem.commands;

import de.koppy.bansystem.BanSystem;
import de.koppy.bansystem.api.BanManager;
import de.koppy.bansystem.api.BanUI;
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

public class Ban implements CommandExecutor, TabCompleter {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if(!player.hasPermission("server.admin.ban")) { player.sendMessage(BanSystem.getPrefix() + "§ckeine Rechte."); return false;}
            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("help")) {
                    player.sendMessage("§7-----------[ §cBanSystem §7]-----------");
                    player.sendMessage("§7To ban a player you need to type in following syntax:");
                    player.sendMessage("§e/ban <name> <ID/time> <reason>");
                    player.sendMessage("§7");
                    player.sendMessage("§7There is the following Syntyax for the time: (e.g. 2d)");
                    player.sendMessage("§ed - days, w - weeks, m - months, y - years, h - hours");
                    player.sendMessage("§7");
                    return true;
                }

                String playername = args[0];
                OfflinePlayer op = Bukkit.getOfflinePlayer(playername);
                UUID uuid = op.getUniqueId();
                BanManager userban = new BanManager(uuid);
                if(userban.isBanned()) { player.sendMessage(BanSystem.getPrefix() + "§cDieser Spieler ist bereits gebannt."); return false; }
                BanUI banui = new BanUI(uuid, player.getUniqueId().toString());
                player.openInventory(banui.getInventory());
                banui.setMainMenu();
            }else if(args.length == 2) {
                int id = Integer.parseInt(args[1]);
                String playername = args[0];
                OfflinePlayer op = Bukkit.getOfflinePlayer(playername);
                UUID uuid = op.getUniqueId();
                BanManager userban = new BanManager(uuid);
                if(userban.isBanned()) { player.sendMessage(BanSystem.getPrefix() + "§cDieser Spieler ist bereits gebannt."); return false; }
                player.sendMessage(BanSystem.getPrefix() + userban.banID(id, player.getUniqueId().toString()));
            }else if(args.length >= 3) {
                long time = getTimeFromFormat(args[1]);
                if(time < 0) { player.sendMessage(BanSystem.getPrefix() + "§cFalsche Formatierung der Bandauer."); return false; }
                String reason = "";
                for(int i=2; i< args.length; i++) {
                    reason = reason + " " + args[i];
                }
                String playername = args[0];
                OfflinePlayer op = Bukkit.getOfflinePlayer(playername);
                UUID uuid = op.getUniqueId();
                BanManager userban = new BanManager(uuid);
                if(userban.isBanned()) { player.sendMessage(BanSystem.getPrefix() + "§cDieser Spieler ist bereits gebannt."); return false; }
                userban.ban(time, reason.replace("&", "§"), player.getUniqueId().toString());
                player.sendMessage(BanSystem.getPrefix() + "§7Der Spieler §e" + op.getName() + " §7wurde §a" + args[1] + " §7lang für §3" + reason + " §7gebannt.");
            }
        }else {
            if(args.length == 1) {
                String playername = args[0];
                OfflinePlayer op = Bukkit.getOfflinePlayer(playername);
                UUID uuid = op.getUniqueId();
                BanManager userban = new BanManager(uuid);
                if(userban.isBanned()) { commandSender.sendMessage("§cDieser Spieler ist bereits gebannt."); return false; }
                userban.ban(31556952000L *10L, "§cAdmin-Ban", "Console");
                commandSender.sendMessage("§7Der Spieler §e" + op.getName() + " §7wurde §cpermanent §7gebannt.");
            }else if(args.length == 2) {
                int id = Integer.parseInt(args[1]);
                String playername = args[0];
                OfflinePlayer op = Bukkit.getOfflinePlayer(playername);
                UUID uuid = op.getUniqueId();
                BanManager userban = new BanManager(uuid);
                if(userban.isBanned()) { commandSender.sendMessage("§cDieser Spieler ist bereits gebannt."); return false; }
                commandSender.sendMessage(userban.banID(id, "Console"));
            }else if(args.length >= 3) {
                long time = getTimeFromFormat(args[1]);
                if(time < 0) { commandSender.sendMessage("§cFalsche Formatierung der Bandauer."); return false; }
                String reason = "";
                for(int i=2; i< args.length; i++) {
                    reason = reason + " " + args[i];
                }
                String playername = args[0];
                OfflinePlayer op = Bukkit.getOfflinePlayer(playername);
                UUID uuid = op.getUniqueId();
                BanManager userban = new BanManager(uuid);
                if(userban.isBanned()) { commandSender.sendMessage("§cDieser Spieler ist bereits gebannt."); return false; }
                userban.ban(time, reason.replace("&", "§"), "Console");
                commandSender.sendMessage("§7Der Spieler §e" + op.getName() + " §7wurde §a" + args[1] + " §7lang für §3" + reason + " §7gebannt.");
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
            if(player.hasPermission("server.admin.ban")) {
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
