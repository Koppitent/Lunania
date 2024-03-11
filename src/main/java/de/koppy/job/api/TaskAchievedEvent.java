package de.koppy.job.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TaskAchievedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private JobType jobType;
    private double amountmoney;
    private double amountxp;

    public TaskAchievedEvent(Player player, JobType jobType, double amountmoney, double amountxp) {
        this.player = player;
        this.jobType = jobType;
        this.amountmoney = amountmoney;
        this.amountxp = amountxp;
    }

    public Player getPlayer() {
        return player;
    }

    public JobType getJobType() {
        return jobType;
    }

    public double getAmountmoney() {
        return amountmoney;
    }

    public double getAmountxp() {
        return amountxp;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
