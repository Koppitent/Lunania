package de.koppy.job.api;

import java.util.ArrayList;
import java.util.List;

public class JobXpLevel {

    private List<Integer> levels = new ArrayList<Integer>();

    public JobXpLevel() { }

    public void addLevel(int xpnextlevel) {
        levels.add(xpnextlevel);
    }

    public void setXpNeededForNextLevel(int level, int xpnextlevel) {
        levels.set(level, xpnextlevel);
    }

    public int getMaxLevel() {
        return levels.size();
    }

    public int xpNeededForLevelup(int level) {
        if(level < getMaxLevel()) return levels.get(level);
        return 999999999;
    }

}