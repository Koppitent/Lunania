package de.koppy.basics.api;

import org.bukkit.entity.Player;

public enum Rank {
    RANDOM, CONTENT, TEAM, ADMIN, OWNER;

    public static Rank getRank(Player player) {
        if(player.hasPermission("server.rank.owner")) return Rank.OWNER;
        if(player.hasPermission("server.rank.admin")) return Rank.ADMIN;
        if(player.hasPermission("server.rank.team")) return Rank.TEAM;
        if(player.hasPermission("server.rank.content")) return Rank.CONTENT;
        else return Rank.RANDOM;
    }
}
