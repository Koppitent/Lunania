package de.koppy.basics.api.scoreboard;
import de.koppy.basics.api.PlayerProfile;
import de.koppy.basics.api.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Tablist {

    @SuppressWarnings("deprecation")
    public void updateTablist(Player player) {
        Scoreboard sb = player.getScoreboard();
        if(sb == null) sb = Bukkit.getScoreboardManager().getNewScoreboard();

        for(Player onlineplayer : Bukkit.getOnlinePlayers()) {
            //* get players information
            String prefix = Rank.getRankPrefix(Rank.getRank(onlineplayer));
            String name = onlineplayer.getName();
            String suffix = "";
            int rankprio = Rank.getRankPrio(Rank.getRank(onlineplayer));
            if(onlineplayer.hasPermission("server.team")) suffix = "§8[§cTeam§8]";

            PlayerProfile profile = PlayerProfile.getProfile(onlineplayer.getUniqueId());
            if(profile.isNicked()) {
                prefix = Rank.getRankPrefix(Rank.RANDOM);
                rankprio = Rank.getRankPrio(Rank.RANDOM);
                suffix = "";
            }

            for(Rank rank : Rank.values()) {
                int prio = Rank.getRankPrio(rank);
                Team teamtest = sb.getTeam(prio+onlineplayer.getName()+onlineplayer.getUniqueId().toString());
                if(teamtest != null) {
                    teamtest.removePlayer(onlineplayer);
                }
            }

            Team team = sb.getTeam(rankprio+onlineplayer.getName()+onlineplayer.getUniqueId().toString());
            if(team == null) team = sb.registerNewTeam(rankprio+onlineplayer.getName()+onlineplayer.getUniqueId().toString());
            team.setDisplayName(name);
            team.setColor(ChatColor.GRAY);
            team.setPrefix(prefix);
            team.setSuffix(" "+suffix);
            team.addPlayer(onlineplayer);

            player.setScoreboard(sb);
        }

    }

    public static void updateTablistForAll() {
        for(Player all : Bukkit.getOnlinePlayers()) {
            new Tablist().updateTablist(all);
        }
    }

}