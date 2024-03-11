package de.koppy.job.api;

import de.koppy.basics.api.PlayerProfile;
import de.koppy.job.JobSystem;
import de.koppy.mysql.api.Column;
import de.koppy.mysql.api.ColumnType;
import org.bukkit.Bukkit;

import java.util.UUID;

public class PlayerJob {

    private UUID uuid;
    private Column uuidc = new Column("uuid", ColumnType.VARCHAR, 200);
    private Column currentjobc = new Column("currentjob", ColumnType.VARCHAR, 200);

    public PlayerJob(UUID uuid) {
        this.uuid = uuid;
    }

    public void setJob(JobType jobtype) {
        JobSystem.getTable().setValue(currentjobc, jobtype.toString(), uuidc, uuid.toString());
        getTaskAmount().registerTaskAmount(jobtype.getTaskAmount());
    }

    public void addXp(int amount, JobType job) {
        int xp = getXP(job);
        xp = xp + amount;
        int level = getLevel(job);
        checkLevelUp(xp, level, job);
    }

    public void checkLevelUp(int xp, int level, JobType job) {
        JobXpLevel jxpl = job.getJobXpLevel();
        if(level < jxpl.getMaxLevel()) {
            int xpneeded = jxpl.xpNeededForLevelup(level);
            if(xp >= xpneeded) {
                xp = xp - xpneeded;
                level++;
                Bukkit.getPlayer(uuid).sendMessage("§e§l"+job.toString().toUpperCase()+" §r§7You reached §aLevel "+level+"§7! Congratulations!");
            }
            if(level == 10) {
                Bukkit.getPlayer(uuid).sendMessage(JobSystem.getPrefix() + "§7You reached max Level on §e"+job.toString().toLowerCase() + "§7.");
                Bukkit.getPlayer(uuid).sendMessage(JobSystem.getPrefix() + "§7As a reward you'll receive +5 Lands and +1 Home Capacity.");
                PlayerProfile profile = PlayerProfile.getProfile(uuid);
                int maxhomes = profile.getMaxhomes();
                maxhomes = maxhomes + 1;
                int maxlands = profile.getMaxLands();
                maxlands = maxlands + 5;
                profile.setMaxhomes(maxhomes);
                profile.setMaxLands(maxlands);
            }
            setXPandLevel(job, xp, level);
        }
    }

    public JobType getJob() {
        String jobS = (String) JobSystem.getTable().getValue(currentjobc, uuidc, uuid.toString());
        if(jobS != null) {
            return JobType.fromString(jobS);
        }else {
            return JobType.NONE;
        }
    }

    public int getLevel(JobType jobtype) {
        String xpandlevel = getXPandLevel(jobtype);
        return Integer.valueOf(xpandlevel.split(":")[1]);
    }

    public int getXP(JobType jobtype) {
        String xpandlevel = getXPandLevel(jobtype);
        return Integer.valueOf(xpandlevel.split(":")[0]);
    }

    public void setXPandLevel(JobType jobtype, int xp, int level) {
        Column jobc = new Column(jobtype.toString(), ColumnType.VARCHAR, 200);
        JobSystem.getTable().setValue(jobc, xp+":"+level, uuidc, uuid.toString());
    }

    public TaskAmount getTaskAmount() {
        return PlayerProfile.getProfile(uuid).getTaskamount();
    }

    private String getXPandLevel(JobType jobtype) {
        Column jobc = new Column(jobtype.toString(), ColumnType.VARCHAR, 200);
        String out = (String) JobSystem.getTable().getValue(jobc, uuidc, uuid.toString());
        if(out == null) {
            setXPandLevel(jobtype, 0, 0);
            return "0:0";
        }else {
            return out;
        }
    }

}