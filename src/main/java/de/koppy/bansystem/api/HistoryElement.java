package de.koppy.bansystem.api;

import java.util.Date;

public class HistoryElement {

    private Date expiredate;
    private boolean reban;
    private boolean unban;
    private String bannedby;
    private String reason;
    private long duration;

    public HistoryElement(Date expiredate, boolean reban, boolean unban, String bannedby, String reason, long duration) {
        this.expiredate = expiredate;
        this.reban = reban;
        this.unban = unban;
        this.bannedby = bannedby;
        this.reason = reason;
        this.duration = duration;
    }

    public void setUnban(boolean unban) {
        this.unban = unban;
    }

    public long getDuration() {
        return duration;
    }

    public Date getExpiredate() {
        return expiredate;
    }

    public boolean isReban() {
        return reban;
    }

    public boolean isUnban() {
        return unban;
    }

    public String getBannedby() {
        return bannedby;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return expiredate.getTime() + ":" + reban + ":" + unban + ":" + bannedby + ":" + reason + ":" + duration;
    }

    public static HistoryElement fromString(String element) {
        String[] elementS = element.split(":");
        String time = elementS[0];
        String reban = elementS[1];
        String unban = elementS[2];
        String bannedby = elementS[3];
        String reason = elementS[4];
        String duration = elementS[5];
        return new HistoryElement(new Date(Long.parseLong(time)), Boolean.parseBoolean(reban), Boolean.parseBoolean(unban), bannedby, reason, Long.parseLong(duration));
    }

}
