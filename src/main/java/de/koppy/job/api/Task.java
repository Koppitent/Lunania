package de.koppy.job.api;

public class Task {

    private double payoutamount;
    private int toachieve;
    private int xpgain;
    private String title;
    private String description;
    private JobType job;

    public Task(String title, String description, int toachieve, double payoutamount, int xpgain, JobType job) {
        this.payoutamount = payoutamount;
        this.toachieve = toachieve;
        this.title = title;
        this.description = description;
        this.job = job;
        this.xpgain = xpgain;
    }

    public int getXpgain() {
        return xpgain;
    }

    public JobType getJob() {
        return job;
    }

    public int getToachieve() {
        return toachieve;
    }

    public double getPayoutamount(double level) {
        double bonus = (level/100D) * payoutamount;
        return (bonus + payoutamount);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

}