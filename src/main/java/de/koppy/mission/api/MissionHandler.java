package de.koppy.mission.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.koppy.lunaniasystem.LunaniaSystem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MissionHandler {

    public static List<Daily> dailies = new ArrayList<Daily>();
    public static List<Weekly> weeklies = new ArrayList<Weekly>();
    public static List<Seasonal> seasonal = new ArrayList<Seasonal>();
    public static List<Mission> missions = new ArrayList<Mission>();

    public Mission getMission(String identifier) {
        for(Mission m : missions) {
            if(m.getIdentifier().equalsIgnoreCase(identifier)) {
                return m;
            }
        }
        return null;
    }

    public void checkDailies(Player p) {
        PlayerMission pm = new PlayerMission(p.getUniqueId());
        if(pm.getState(Type.DAILY) != LunaniaSystem.getServerInstance().getDay()) {
            getNewDailies(p);
            p.sendMessage("§7You got new daily missions!");
        }
    }

    public void checkWeeklies(Player p) {
        PlayerMission pm = new PlayerMission(p.getUniqueId());
        if(pm.getState(Type.WEEKLY) != LunaniaSystem.getServerInstance().getWeek()) {
            getNewWeeklies(p);
            p.sendMessage("§7You got new weekly missions!");
        }
    }

    public void checkSeasonal(Player p) {
        PlayerMission pm = new PlayerMission(p.getUniqueId());
        if(pm.getState(Type.SEASONAL) != LunaniaSystem.getServerInstance().getSeason()) {
            getNewSeason(p);
            p.sendMessage("§7You got new seasonal missions!");
        }
    }

    public void getNewDailies(Player p) {
        List<Daily> toremove = new ArrayList<Daily>();
        Daily daily1 = getRandomDaily(toremove);
        toremove.add(daily1);
        Daily daily2 = getRandomDaily(toremove);
        toremove.add(daily2);
        Daily daily3 = getRandomDaily(toremove);
        toremove.add(daily3);
        Daily daily4 = getRandomDaily(toremove);

        String out = daily1.getIdentifier()+":"+"0"+"="+daily2.getIdentifier()
                +":"+"0"+"="+daily3.getIdentifier()+":"+"0"+"="+daily4.getIdentifier()+":"+"0";

        new PlayerMission(p.getUniqueId()).setListDaily(out);
    }

    public void getNewWeeklies(Player p) {
        List<Weekly> toremove = new ArrayList<Weekly>();
        Weekly daily1 = getRandomWeekly(toremove);
        toremove.add(daily1);
        Weekly daily2 = getRandomWeekly(toremove);
        toremove.add(daily2);
        Weekly daily3 = getRandomWeekly(toremove);
        toremove.add(daily3);
        Weekly daily4 = getRandomWeekly(toremove);

        String out = daily1.getIdentifier()+":"+"0"+"="+daily2.getIdentifier()
                +":"+"0"+"="+daily3.getIdentifier()+":"+"0"+"="+daily4.getIdentifier()+":"+"0";

        new PlayerMission(p.getUniqueId()).setListWeekly(out);
    }

    public void getNewSeason(Player p) {
        String out = "";
        for(Seasonal s : seasonal) {
            out = out + "=" + s.getIdentifier()+":"+"0";
        }
        out = out.substring(1);

        new PlayerMission(p.getUniqueId()).setListSeason(out);
    }

    public void claimRewardDaily(Player p, String identifier) {
        PlayerMission pm = new PlayerMission(p.getUniqueId());
        for(String m : pm.getDaily()) {
            if(m.split(":")[0].equalsIgnoreCase(identifier)) {
                Integer i = Integer.valueOf(m.split(":")[1]);
                if(i == getMission(identifier).getStages()) {
                    addStageDaily(p, identifier, true);
                    p.sendMessage("§7You §asucessfully §7claimed your daily rewards.");
                    Mission mission = getMission(identifier);
                    for(ItemStack istack : mission.getRewards()) {
                        p.getInventory().addItem(istack);
                    }
                }else {
                    p.sendMessage("§cYou cant claim this.");
                }
            }
        }
    }

    public void claimRewardWeekly(Player p, String identifier) {
        PlayerMission pm = new PlayerMission(p.getUniqueId());
        for(String m : pm.getWeekly()) {
            if(m.split(":")[0].equalsIgnoreCase(identifier)) {
                Integer i = Integer.valueOf(m.split(":")[1]);
                if(i == getMission(identifier).getStages()) {
                    addStageWeekly(p, identifier, true);
                    p.sendMessage("§7You §asucessfully §7claimed your weekly rewards.");
                    Mission mission = getMission(identifier);
                    for(ItemStack istack : mission.getRewards()) {
                        p.getInventory().addItem(istack);
                    }
                }else {
                    p.sendMessage("§cYou cant claim this.");
                }
            }
        }
    }

    public void claimRewardSeasonal(Player p, String identifier) {
        PlayerMission pm = new PlayerMission(p.getUniqueId());
        for(String m : pm.getSeason()) {
            if(m.split(":")[0].equalsIgnoreCase(identifier)) {
                Integer i = Integer.valueOf(m.split(":")[1]);
                if(i == getMission(identifier).getStages()) {
                    addStageSeasonal(p, identifier, true);
                    p.sendMessage("§7You §asucessfully §7claimed your season rewards.");
                    Mission mission = getMission(identifier);
                    for(ItemStack istack : mission.getRewards()) {
                        p.getInventory().addItem(istack);
                    }
                }else {
                    p.sendMessage("§cYou cant claim this.");
                }
            }
        }
    }

    public boolean addStageDailyIfExist(Player p, String identifier) {
        PlayerMission pm = new PlayerMission(p.getUniqueId());
        for(String s : pm.getDaily()) {
            if(s.split(":")[0].equalsIgnoreCase(identifier)) {
                addStageDaily(p, identifier, false);
                return true;
            }
        }
        return false;
    }

    public void addStageDaily(Player p, String identifier, boolean ignorelevel) {
        PlayerMission pm = new PlayerMission(p.getUniqueId());
        String out = "";
        for(String m : pm.getDaily()) {
            if(m.split(":")[0].equalsIgnoreCase(identifier)) {
                Integer i = Integer.valueOf(m.split(":")[1]);
                if(i >= getMission(identifier).getStages() && ignorelevel == false) return;
                i++;
                out = out + "=" + m.split(":")[0]+":"+i;
            }else {
                out = out + "=" + m;
            }
        }
        out = out.substring(1);
        new PlayerMission(p.getUniqueId()).setListDaily(out);
    }

    public boolean addStageWeeklyIfExist(Player p, String identifier) {
        PlayerMission pm = new PlayerMission(p.getUniqueId());
        for(String s : pm.getWeekly()) {
            if(s.split(":")[0].equalsIgnoreCase(identifier)) {
                addStageWeekly(p, identifier, false);
                return true;
            }
        }
        return false;
    }

    public void addStageWeekly(Player p, String identifier, boolean ignorelevel) {
        PlayerMission pm = new PlayerMission(p.getUniqueId());
        String out = "";
        for(String m : pm.getWeekly()) {
            if(m.split(":")[0].equalsIgnoreCase(identifier)) {
                Integer i = Integer.valueOf(m.split(":")[1]);
                if(i >= getMission(identifier).getStages() && ignorelevel == false) return;
                i++;
                out = out + "=" + m.split(":")[0]+":"+i;
            }else {
                out = out + "=" + m;
            }
        }
        out = out.substring(1);
        new PlayerMission(p.getUniqueId()).setListWeekly(out);
    }

    public boolean addStageSeasonalIfExist(Player p, String identifier) {
        PlayerMission pm = new PlayerMission(p.getUniqueId());
        for(String s : pm.getSeason()) {
            if(s.split(":")[0].equalsIgnoreCase(identifier)) {
                addStageSeasonal(p, identifier, false);
                return true;
            }
        }
        return false;
    }

    public void addStageSeasonal(Player p, String identifier, boolean ignorelevel) {
        PlayerMission pm = new PlayerMission(p.getUniqueId());
        String out = "";
        for(String m : pm.getSeason()) {
            if(m.split(":")[0].equalsIgnoreCase(identifier)) {
                Integer i = Integer.valueOf(m.split(":")[1]);
                if(i >= getMission(identifier).getStages() && ignorelevel == false) return;
                i++;
                out = out + "=" + m.split(":")[0]+":"+i;
            }else {
                out = out + "=" + m;
            }
        }
        out = out.substring(1);
        new PlayerMission(p.getUniqueId()).setListSeason(out);
    }

    public int getStage(Player p, String identifier, Type type) {
        if(type == Type.DAILY) {
            return getStageDaily(p, identifier);
        }else if(type == Type.WEEKLY) {
            return getStageWeekly(p, identifier);
        }else if(type == Type.SEASONAL) {
            return getStageSeason(p, identifier);
        }
        return -1;
    }

    public int getStageDaily(Player p, String identifier) {
        PlayerMission pm = new PlayerMission(p.getUniqueId());
        for(String m : pm.getDaily()) {
            if(m.split(":")[0].equalsIgnoreCase(identifier)) {
                return Integer.valueOf(m.split(":")[1]);
            }
        }
        return -1;
    }

    public int getStageWeekly(Player p, String identifier) {
        PlayerMission pm = new PlayerMission(p.getUniqueId());
        for(String m : pm.getWeekly()) {
            if(m.split(":")[0].equalsIgnoreCase(identifier)) {
                return Integer.valueOf(m.split(":")[1]);
            }
        }
        return -1;
    }

    public int getStageSeason(Player p, String identifier) {
        PlayerMission pm = new PlayerMission(p.getUniqueId());
        for(String m : pm.getSeason()) {
            if(m.split(":")[0].equalsIgnoreCase(identifier)) {
                return Integer.valueOf(m.split(":")[1]);
            }
        }
        return -1;
    }

    public Daily getRandomDaily(List<Daily> remove) {
        Random rndm = new Random();
        List<Daily> l = new ArrayList<Daily>();
        l.addAll(dailies);
        l.removeAll(remove);
        int index = rndm.nextInt(l.size());
        return l.get(index);
    }

    public Weekly getRandomWeekly(List<Weekly> remove) {
        Random rndm = new Random();
        List<Weekly> l = new ArrayList<Weekly>();
        l.addAll(weeklies);
        l.removeAll(remove);
        int index = rndm.nextInt(l.size());
        return l.get(index);
    }

}