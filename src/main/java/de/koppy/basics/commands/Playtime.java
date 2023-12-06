package de.koppy.basics.commands;

import de.koppy.basics.api.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Playtime implements CommandExecutor {

    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player))
            return false;

        Player player = (Player) sender;

        if(args.length == 1) {
            if(player.hasPermission("server.admin.playtime")) {
                String name = args[0];
                if(Bukkit.getPlayer(name) != null) {
                    Player t = Bukkit.getPlayer(name);
                    PlayerProfile profile = PlayerProfile.getProfile(t.getUniqueId());

                    int totalplaytime = profile.getTotalPlaytime();
                    int playtimejoin = profile.getPlaytimeSinceJoin();

                    player.sendMessage("§7"+t.getName()+"'s §7Total playtime: §3" + formatTime(totalplaytime));
                    player.sendMessage("§7"+t.getName()+"'s §7Playtime since joined: §3" + formatTime(playtimejoin));

                    return false;
                }else {
                    OfflinePlayer t = Bukkit.getOfflinePlayer(name);
                    int totalplaytime = PlayerProfile.getTotalPlaytimeSQL(t.getUniqueId());

                    player.sendMessage("§7"+t.getName()+"'s §7Total playtime: §3" + formatTime(totalplaytime));
                    return false;
                }
            }
        }

        PlayerProfile profile = PlayerProfile.getProfile(player.getUniqueId());

        int totalplaytime = profile.getTotalPlaytime();
        int playtimejoin = profile.getPlaytimeSinceJoin();

        player.sendMessage("§7Total playtime: §3" + formatTime(totalplaytime));
        player.sendMessage("§7Playtime since joined: §3" + formatTime(playtimejoin));

        return false;
    }

    private String formatTime(int seconds) {
        int minutes=0;
        int hours=0;
        int days=0;

        if(seconds >= 60) {
            minutes = seconds/60;
            seconds = seconds%60;
        }

        if(minutes >= 60) {
            hours = minutes/60;
            minutes = minutes%60;
        }

        if(hours >= 24) {
            days = hours/24;
            hours = hours%24;
        }

        String out = "§6";

        if(days > 0) {
            out = out + days + "Days, " + hours + "Hours, " + minutes + "Minutes, " + seconds + "Seconds§7.";
        }else if(hours > 0) {
            out = out + hours + "Hours, " + minutes + "Minutes, " + seconds + "Seconds§7.";
        }else if(minutes > 0) {
            out = out + minutes + "Minutes, " + seconds + "Seconds§7.";
        }else {
            out = out + seconds + "Seconds§7.";
        }

        return out;
    }

}