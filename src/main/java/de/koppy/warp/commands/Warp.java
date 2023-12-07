package de.koppy.warp.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.koppy.basics.api.PlayerProfile;
import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.mysql.api.Column;
import de.koppy.mysql.api.ColumnType;
import de.koppy.mysql.api.Table;
import de.koppy.warp.WarpSystem;
import de.koppy.warp.api.WarpManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class Warp implements CommandExecutor, TabCompleter {

    private static final Column namec = new Column("name", ColumnType.VARCHAR, 200);
    private static final Column acceptedc = new Column("accepted", ColumnType.BOOL, 200);
    private static final Table table = WarpSystem.getTable();

    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return true;
        Player p = (Player) sender;

        if(args.length == 2) {
            if(args[0].equalsIgnoreCase("list")) {
                String number = args[1];
                if(number.matches("[0-9]+")) {
                    int num = Integer.valueOf(number);
                    //* Creating list
                    p.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§7List of available Warps:");
                    for(String w : getWarplist(num, true)) {
                        p.sendMessage("§8§ §7"+w);
                    }
                    p.sendMessage(" ");
                    return false;
                }else {
                    //* Creating list
                    p.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§7List of available Warps:");
                    for(String w : getWarplist(1, true)) {
                        p.sendMessage("§8§ §7"+w);
                    }
                    p.sendMessage(" ");
                    return false;
                }
            }else if(args[0].equalsIgnoreCase("listrequested")) {
                if(p.hasPermission("server.admin.warp")) {
                    String number = args[1];
                    if(number.matches("[0-9]+")) {
                        int num = Integer.valueOf(number);
                        //* Creating list
                        p.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§7List of Warps to accept:");
                        for(String w : getWarplist(num, false)) {
                            p.sendMessage("§8§ §7"+w);
                        }
                        p.sendMessage(" ");
                        return false;
                    }else {
                        //* Creating list
                        p.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§7List of Warps to accept:");
                        for(String w : getWarplist(1, false)) {
                            p.sendMessage("§8§ §7"+w);
                        }
                        p.sendMessage(" ");
                        return false;
                    }
                }
            }
        }

        if(args.length == 0) {
            PlayerProfile profile = PlayerProfile.getProfile(p.getUniqueId());
            p.sendMessage("§7You currently have §e"+profile.getWarptokens()+" §7Warp-Tokens.");
            p.sendMessage("§7Create Warp - §e3 Tokens");
            p.sendMessage("§7Edit Warp - §e1 Token");
            p.sendMessage(" ");
            p.sendMessage("§7/warp create <name> <description>");
        }else if(args.length == 1) {
            if(args[0].equalsIgnoreCase("list")) {

                //* Creating list
                p.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§7List of available Warps:");
                for(String w : getWarplist(1, true)) {
                    p.sendMessage("§8§ §7"+w);
                }
                p.sendMessage(" ");

            }else {
                String warpname = args[0];
                WarpManager warp = new WarpManager(warpname);
                if(warp.existWarp()) {
                    if(warp.isAccepted() || p.hasPermission("server.admin.warp.accept") || warp.isOwner(p.getUniqueId())) {

                        if(!warp.isAccepted()) {
                            p.sendMessage("§cThis Warp is not accepted yet.");
                        }
                        Location location = warp.getLocation();
                        if(location == null) {
                            p.sendMessage("§cThis Warp does have an error.");
                            return true;
                        }
                        p.teleport(location);
                        p.sendMessage(warp.getMessage());

                    }else {
                        p.sendMessage("§cWarp has not been accepted yet.");
                    }
                }else {
                    p.sendMessage("§cWarp doesnt exist.");
                }
            }
        }else if(args.length >= 2) {
            if(args[0].equalsIgnoreCase("create")) {
                String name = args[1];
                String message = "";
                if(args.length > 2) {
                    for(int i=2; i<args.length; i++) {
                        message = message + " " + args[i];
                    }
                }
                if(checkFormat(name, 3, 20) && checkFormat(message, 0, 60)) {
                    WarpManager warp = new WarpManager(name);
                    if(!warp.existWarp()) {
                        PlayerProfile profile = PlayerProfile.getProfile(p.getUniqueId());
                        if(profile.getWarptokens() >= 3) {
                            int tokens = profile.getWarptokens();
                            tokens = tokens - 3;
                            profile.setWarptokens(tokens);
                            warp.createWarp(p.getUniqueId(), message, p.getLocation(), LunaniaSystem.getServerInstance().getName());
                            p.sendMessage("§7Du hast erfolgreisch einen Antrag f§r einen Warp gestellt.");
                            p.sendMessage("§7Ein §cAdmin §7wird baldmöglichst alles überprüfen.");
                            p.sendMessage("§4§l!!! §r§7Bedenke, dass vorsetzliche Verstöße zu einem Bann führen §4§l!!!");
                        }else {
                            p.sendMessage("§cYou dont have enough Warptokens anymore!");
                        }
                    }else {
                        p.sendMessage("§cThis Warpname already exists.");
                    }
                }else {
                    p.sendMessage("§cWrong format.");
                }
            }else if(args[0].equalsIgnoreCase("setlocation")) {
                String name = args[1];
                WarpManager warp = new WarpManager(name);
                if(warp.existWarp()) {
                    if(warp.isOwner(p.getUniqueId())) {
                        PlayerProfile profile = PlayerProfile.getProfile(p.getUniqueId());
                        if(profile.getWarptokens() >= 1) {
                            int tokens = profile.getWarptokens();
                            tokens = tokens - 1;
                            profile.setWarptokens(tokens);
                            warp.changeLocation(p.getLocation(), LunaniaSystem.getServerInstance().getName());
                            p.sendMessage("§7Warplocation requested!");
                        }else {
                            p.sendMessage("§cYou dont have enough Warptokens anymore!");
                        }
                    }else {
                        p.sendMessage("§cYou need to be the owner of the warp to do that.");
                    }
                }else {
                    p.sendMessage("§cThis warp doesnt exist.");
                }
            }else if(args[0].equalsIgnoreCase("setmessage")) {
                String name = args[1];
                String message = "";
                if(args.length > 2) {
                    for(int i=2; i<args.length; i++) {
                        message = message + " " + args[i];
                    }
                }
                WarpManager warp = new WarpManager(name);
                if(checkFormat(message, 0, 20)) {
                    if(warp.existWarp()) {
                        if(warp.isOwner(p.getUniqueId())) {
                            PlayerProfile profile = PlayerProfile.getProfile(p.getUniqueId());
                            if(profile.getWarptokens() >= 1) {
                                int tokens = profile.getWarptokens();
                                tokens = tokens - 1;
                                profile.setWarptokens(tokens);
                                warp.changeMessage(message);
                                p.sendMessage("§7Warpmessage requested!");
                            }else {
                                p.sendMessage("§cYou dont have enough Warptokens anymore!");
                            }
                        }else {
                            p.sendMessage("§cYou need to be the owner of the warp to do that.");
                        }
                    }else {
                        p.sendMessage("§cThis warp doesnt exist.");
                    }
                }else {
                    p.sendMessage("§cWrong message format.");
                }
            }else if(args[0].equalsIgnoreCase("delete")) {
                String name = args[1];
                WarpManager warp = new WarpManager(name);
                if(warp.existWarp()) {
                    if(p.hasPermission("server.admin.warp.delete") || warp.isOwner(p.getUniqueId())) {
                        warp.delete();
                        p.sendMessage("§7Warp got deleted.");
                    }else {
                        p.sendMessage("§cYou need to be the owner of the warp to do that.");
                    }
                }else {
                    p.sendMessage("§cThis warp doesnt exist.");
                }
            }else if(args[0].equalsIgnoreCase("accept")) {
                String name = args[1];
                if(p.hasPermission("server.admin.warp.accept")) {
                    WarpManager warp = new WarpManager(name);
                    if(warp.existWarp()) {
                        if(warp.isAccepted() == false) {
                            warp.accept();
                            p.sendMessage("§7Warp got accepted.");
                        }else {
                            p.sendMessage("§cThis warp is already accepted.");
                        }
                    }else {
                        p.sendMessage("§cThis warp doesnt exist.");
                    }
                }
            }else if(args[0].equalsIgnoreCase("deny")) {
                String name = args[1];
                if(p.hasPermission("server.admin.warp.deny")) {
                    WarpManager warp = new WarpManager(name);
                    if(warp.existWarp()) {
                        if(warp.isAccepted() == false) {
                            UUID uuid = warp.getOwner();
                            PlayerProfile.addWarpTokens(uuid, 3);
                            warp.delete();
                            p.sendMessage("§7Warp got denied.");
                        }else {
                            p.sendMessage("§cThis warp is already accepted.");
                        }
                    }else {
                        p.sendMessage("§cThis warp doesnt exist.");
                    }
                }
            }
        }
        if(args.length == 4) {
            if(p.hasPermission("server.admin.warp.tokens")) {
                if(args[0].equalsIgnoreCase("tokens")) {
                    if(args[1].equalsIgnoreCase("add")) {
                        String name = args[2];
                        if(Bukkit.getOfflinePlayer(name).hasPlayedBefore()) {
                            if(args[3].matches("[0-9]+")) {
                                int amount = Integer.valueOf(args[3]);
                                PlayerProfile.addWarpTokens(Bukkit.getOfflinePlayer(name).getUniqueId(), amount);
                                p.sendMessage("§7Warptokens added");
                            }
                        }else {
                            p.sendMessage("§cPlayer was never online.");
                        }
                    }else if(args[1].equalsIgnoreCase("remove")) {
                        String name = args[2];
                        if(Bukkit.getOfflinePlayer(name).hasPlayedBefore()) {
                            if(args[3].matches("[0-9]+")) {
                                int amount = Integer.valueOf(args[3]);
                                PlayerProfile.removeWarpTokens(Bukkit.getOfflinePlayer(name).getUniqueId(), amount);
                                p.sendMessage("§7Warptokens removed");
                            }
                        }else {
                            p.sendMessage("§cPlayer was never online.");
                        }
                    }else if(args[1].equalsIgnoreCase("set")) {
                        String name = args[2];
                        if(Bukkit.getOfflinePlayer(name).hasPlayedBefore()) {
                            if(args[3].matches("[0-9]+")) {
                                int amount = Integer.valueOf(args[3]);
                                PlayerProfile.setWarptokens(Bukkit.getOfflinePlayer(name).getUniqueId(), amount);
                                p.sendMessage("§7Warptokens set");
                            }
                        }else {
                            p.sendMessage("§cPlayer was never online.");
                        }
                    }
                }
            }
        }

        return false;
    }

    public List<String> getWarplist(int site, boolean bool) {
        List<String> newwarplist = new ArrayList<String>();
        int showpersite = 7;
        List<String> warplist = table.getListFilter(namec, acceptedc, bool);
        int maxsite = warplist.size() / showpersite;
        if(warplist.size() % showpersite != 0) maxsite = maxsite + 1;
        if(site > maxsite) site = 1;

        int begin = (site-1)*showpersite;
        int end = (site*showpersite)-1;

        for(int i=begin; i<(end+1); i++) {
            if(warplist.size() > i) {
                newwarplist.add(warplist.get(i));
            }
        }

        return newwarplist;
    }

    private boolean checkFormat(String input, int minlength, int maxlength) {
        if(input.replace(" ", "").matches("[a-zA-Z0-9]+") || input.equals("")) {
            if(input.length() <= maxlength && input.length() >= minlength) {
                return true;
            }
        }
        return false;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> tcomplete = new ArrayList<String>();
        if(sender instanceof Player) {
            if(args.length == 1) {
                List<String> check = new ArrayList<String>();
                for(String w : table.getListFilter(namec, acceptedc, true)) {
                    check.add(w);
                }
                for(String s : check) if(s.startsWith(args[0])) tcomplete.add(s);
            }
        }
        return tcomplete;
    }

}