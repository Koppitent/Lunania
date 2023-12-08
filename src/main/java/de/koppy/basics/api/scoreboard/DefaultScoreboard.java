
package de.koppy.basics.api.scoreboard;


import de.koppy.basics.api.Rank;
import de.koppy.economy.EconomySystem;
import de.koppy.economy.api.PlayerAccount;
import de.koppy.job.api.JobType;
import de.koppy.job.api.PlayerJob;
import de.koppy.land.api.Land;
import de.koppy.lunaniasystem.LunaniaSystem;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


public class DefaultScoreboard extends ScoreboardBuilder {

    public DefaultScoreboard(Player player) {
        super(player);
    }

    @Override
    public void createScoreboard() {
        setScore("§3", 13);
        setScore("§7Rank: ", 12);
        updateRank();
        setScore("§4", 10);
        setScore("§7Economy: ", 9);
        updateEco();
        setScore("§5", 7);
        setScore("§7Land: ", 6);
        updateLand(new Land(player.getLocation().getChunk()));
        setScore("§8", 4);
        setScore("§7Job: ", 3);
        updateJob();
        setScore("§1", 1);
    }

    @Override
    public void update() { /* updates are being done in PlayerProfiles! */ }

    public void updateRank() {
        setScore("§f" + Rank.getRankPrefix(Rank.getRank(player))+"                  ", 11);
    }

    public void updateEco() {
        if(LunaniaSystem.getServerInstance().isSystemActive("Economy")) {
            setScore("§e" + new PlayerAccount(player.getUniqueId()).getFormatMoney() + EconomySystem.getEcosymbol(), 8);
        }else {
            setScore("§c" + "§cdisabled", 8);
        }
    }

    public void updateLand(Land land) {
        if(!land.isClaimed()) {
            String landout = "§7"+player.getLocation().getWorld().getName();
            if(player.getLocation().getWorld().getName().equals("world")) {
                if(land.isClaimed()) {
                    landout = "§7" + land.getOwnerName();
                }else {
                    landout = "§cunclaimed";
                }
            }
            setScore(landout, 5);
        }else {
            String landout = "§7"+player.getLocation().getWorld().getName();
            if(player.getLocation().getWorld().getName().equals("world")) {
                if(land.isClaimed()) {
                    landout = "§7" + land.getOwnerName();
                }else {
                    landout = "§cunclaimed";
                }
            }
            setScore(landout, 5);
        }
    }

    public void updateJob() {
        JobType job = new PlayerJob(player.getUniqueId()).getJob();
        String out = "§cArbeitslos";
        if(job != JobType.NONE) {
            out = "§e"+job.toString().substring(0, 1)+job.toString().toLowerCase().substring(1);
        }
        setScore(out+" §f", 2);
    }

}
