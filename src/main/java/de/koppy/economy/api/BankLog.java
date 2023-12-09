package de.koppy.economy.api;

import java.util.Date;

public class BankLog {
    private String doneby;
    private String content;
    private long time;

    public BankLog(long time, String doneby, String content) {
        this.doneby = doneby.replace(";", "").replace(":", "");
        this.content = content.replace(";", "").replace(":", "");
        this.time = time;
    }

    public String getDoneby() {
        return doneby;
    }

    public String getContent() {
        return content;
    }

    public Date getDate() {
        return new Date(time);
    }

    @Override
    public String toString() {
        return time+":"+doneby+":"+content;
    }
}