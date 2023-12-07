package de.koppy.mission.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.mission.MissionSystem;
import de.koppy.mysql.api.Column;
import de.koppy.mysql.api.ColumnType;
import de.koppy.mysql.api.Table;
import org.bukkit.Bukkit;

public class PlayerMission {

    private UUID uuid;
    private static final Column daymissionsc = new Column("dailymissions", ColumnType.VARCHAR, 200);
    private static final Column weekmissionsc = new Column("weeklymissions", ColumnType.VARCHAR, 200);
    private static final Column seasonmissionsc = new Column("seasonalmissions", ColumnType.VARCHAR, 200);
    private static final Column uuidc = new Column("uuid", ColumnType.VARCHAR, 200);
    private static final Column datedailyc = new Column("datedaily", ColumnType.INT, 200);
    private static final Column dateweeklyc = new Column("dateweekly", ColumnType.INT, 200);
    private static final Column dateseasonc = new Column("dateseason", ColumnType.INT, 200);
    private static final Table table = MissionSystem.getTable();

    public PlayerMission(UUID uuid) {
        this.uuid = uuid;
    }
    
    public void setState(Type type) {
        if(type == Type.DAILY) {
            table.setValue(datedailyc, LunaniaSystem.getServerInstance().getDay(), uuidc, uuid.toString());
        }else if(type == Type.WEEKLY) {
            table.setValue(dateweeklyc, LunaniaSystem.getServerInstance().getWeek(), uuidc, uuid.toString());
        }else if(type == Type.SEASONAL) {
            table.setValue(dateseasonc, LunaniaSystem.getServerInstance().getSeason(), uuidc, uuid.toString());
        }
    }

    public int getState(Type type) {
        if(type == Type.DAILY) {
            if(table.existEntry(datedailyc, uuidc, uuid.toString())) {
                return (Integer) table.getValue(datedailyc, uuidc, uuid.toString());
            }else {
                return -1;
            }
        }else if(type == Type.WEEKLY) {
            if(table.existEntry(dateweeklyc, uuidc, uuid.toString())) {
                return (Integer) table.getValue(dateweeklyc, uuidc, uuid.toString());
            }else {
                return -1;
            }
        }else if(type == Type.SEASONAL) {
            if(table.existEntry(dateseasonc, uuidc, uuid.toString())) {
                return (Integer) table.getValue(dateseasonc, uuidc, uuid.toString());
            }else {
                return -1;
            }
        }
        return -1;
    }

    public List<String> getDaily() {
        List<String> missions = new ArrayList<String>();
        for(String m : getListDaily().split("=")) {
            missions.add(m);
        }
        return missions;
    }

    public List<String> getWeekly() {
        List<String> missions = new ArrayList<String>();
        for(String m : getListWeek().split("=")) {
            missions.add(m);
        }
        return missions;
    }

    public List<String> getSeason() {
        List<String> missions = new ArrayList<String>();
        for(String m : getListSeason().split("=")) {
            missions.add(m);
        }
        return missions;
    }

    public String getIdentifier(String mission) {
        return mission.split(":")[0];
    }

    public String getStage(String mission) {
        return mission.split(":")[1];
    }

    public void setListDaily(String dailylist) {
        table.setValue(daymissionsc, dailylist, uuidc, uuid.toString());
        setState(Type.DAILY);
    }

    public void setListWeekly(String weeklylist) {
        table.setValue(weekmissionsc, weeklylist, uuidc, uuid.toString());
        setState(Type.WEEKLY);
    }

    public void setListSeason(String seasonlist) {
        table.setValue(seasonmissionsc, seasonlist, uuidc, uuid.toString());
        setState(Type.SEASONAL);
    }

    public String getListDaily() {
        if(table.existEntry(daymissionsc, uuidc, uuid.toString())){
            return (String) table.getValue(daymissionsc, uuidc, uuid.toString());
        }else {
            new MissionHandler().getNewDailies(Bukkit.getPlayer(uuid));
            return getListDaily();
        }
    }

    public String getListWeek() {
        if(table.existEntry(weekmissionsc, uuidc, uuid.toString())) {
            return (String) table.getValue(weekmissionsc, uuidc, uuid.toString());
        }else {
            new MissionHandler().getNewWeeklies(Bukkit.getPlayer(uuid));
            return getListWeek();
        }
    }

    public String getListSeason() {
        if(table.existEntry(seasonmissionsc, uuidc, uuid.toString())) {
            return (String) table.getValue(seasonmissionsc, uuidc, uuid.toString());
        }else {
            new MissionHandler().getNewSeason(Bukkit.getPlayer(uuid));
            return getListSeason();
        }
    }

}