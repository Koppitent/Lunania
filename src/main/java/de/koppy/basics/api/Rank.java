package de.koppy.basics.api;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public enum Rank {
    RANDOM, CONTENT, TEAM, ADMIN, OWNER;

    public static Rank getRank(Player player) {
        if(player.hasPermission("server.rank.owner")) return Rank.OWNER;
        if(player.hasPermission("server.rank.admin")) return Rank.ADMIN;
        if(player.hasPermission("server.rank.team")) return Rank.TEAM;
        if(player.hasPermission("server.rank.content")) return Rank.CONTENT;
        return Rank.RANDOM;
    }

    public static String getRankPrefix(Rank rank) {
//		if(rank == Rank.OWNER) return ChatColor.of("#900502")+"§lO"+ChatColor.of("#9D0A05")+"§lW"+ChatColor.of("#A90F07")+"§lN"+ChatColor.of("#B5140A")+"§lE"+ChatColor.of("#C2190C")+"§lR §r";
//		if(rank == Rank.TEAM) return ChatColor.of("#FF3118")+"§lT"+ChatColor.of("#FF3B21")+"§lE"+ChatColor.of("#FF452A")+"§lA"+ChatColor.of("#FF4F33")+"§lM §r";
//		if(rank == Rank.STREAMER) return ChatColor.of("#CE2892")+"§lS"+ChatColor.of("#CE3599")+"§lT"+ChatColor.of("#CE42A1")+"§lR"+ChatColor.of("#CE4FA8")+"§lE"+ChatColor.of("#CE5CB0")+"§lA"+ChatColor.of("#CE6AB7")+"§lM"+ChatColor.of("#CE77BF")+"§lE"+ChatColor.of("#CE84C6")+"§lR §r";
//		if(rank == Rank.PREMIUM) return getColorofRank(rank) + "§lPREMIUM§r";
//		return ChatColor.of("#7A7A7A")+"§lR"+ChatColor.of("#878787")+"§lA"+ChatColor.of("#949494")+"§lN"+ChatColor.of("#A1A1A1")+"§lD"+ChatColor.of("#ADADAD")+"§lO"+ChatColor.of("#BABABA")+"§lM §r";
        if(rank == Rank.OWNER) return "§f §r";
        if(rank == Rank.ADMIN) return "§f §r";
        if(rank == Rank.TEAM) return "§f §r";
        if(rank == Rank.CONTENT) return "§5Content §r";
        return "§f §r";
    }

    public static int getRankPrio(Rank rank) {
        if(rank == Rank.OWNER) return 1;
        if(rank == Rank.ADMIN) return 2;
        if(rank == Rank.TEAM) return 3;
        if(rank == Rank.CONTENT) return 4;
        return 5;
    }

    public ChatColor getRankColor() {
        if(this == Rank.OWNER) return ChatColor.of("#ff0000");
        if(this == Rank.ADMIN) return ChatColor.of("#ff0000");
        if(this == Rank.TEAM) return ChatColor.of("#ff0000");
        if(this == Rank.CONTENT) return ChatColor.of("#CE2892");
        return ChatColor.of("#726d6d");
    }
}
