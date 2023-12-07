package de.koppy.job.api;

import de.koppy.basics.api.PlayerProfile;
import de.koppy.job.JobSystem;
import de.koppy.mysql.api.Column;
import de.koppy.mysql.api.ColumnType;

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