package de.koppy.basics.api;

import de.koppy.basics.BasicSystem;
import de.koppy.server.Column;
import de.koppy.server.ColumnType;
import de.koppy.server.Table;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerProfile {

    private static List<PlayerProfile> profiles = new ArrayList<>();
    private UUID uuid;
    private final static Table table = BasicSystem.getTable();
    private final static Column uuidc = new Column("uuid", ColumnType.VARCHAR, 200);
    private final static Column heartsc = new Column("hearts", ColumnType.DOUBLE, 200);
    private final static Column foodc = new Column("food", ColumnType.INT, 200);
    private final static Column homesc = new Column("homes", ColumnType.VARCHAR, 60000);
    private final static Column maxhomesc = new Column("maxhomes", ColumnType.INT, 200);
    public PlayerProfile(UUID uuid) {
        this.uuid = uuid;
    }

    public List<Home> getHomes() {
        List<Home> homelist = new ArrayList<>();
        if(table.existEntry(homesc, uuidc, uuid.toString())) {
            String homestring = (String) table.getValue(homesc, uuidc, uuid.toString());
            return StringToHomes(homestring);
        }else {
            table.setValue(homesc, HomesToString(homelist), uuidc, uuid.toString());
            return homelist;
        }
    }

    public void addHome(Home home) {
        if(existHome(home.getName())) return;
        List<Home> homelist = getHomes();
        homelist.add(home);
        saveHomes(homelist);
    }

    public void removeHome(Home home) {
        if(!existHome(home.getName())) return;
        List<Home> homelist = getHomes();
        homelist.remove(home);
        saveHomes(homelist);
    }

    public boolean existHome(String name) {
        for(Home home : getHomes()) {
            if(home.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public void saveHomes(List<Home> homelist) {
        table.setValue(homesc, homelist, uuidc, uuid.toString());
    }

    private List<Home> StringToHomes(String homelist) {
        List<Home> homes = new ArrayList<>();
        if(homelist == null || homelist.isEmpty()) return homes;
        for(String homedata : homelist.split(";")) {
            homes.add(Home.StringToHome(homedata));
        }
        return homes;
    }

    private String HomesToString(List<Home> homes) {
        String out = "";
        for(Home home : homes) {
            out = out + home.toString();
        }
        return out;
    }

    public double getHeartsDatabase() {
        if(table.existEntry(heartsc, uuidc, uuid.toString())) return (double) table.getValue(heartsc, uuidc, uuid.toString());
        else return 20;
    }

    public int getFoodDatabase() {
        if(table.existEntry(foodc, uuidc, uuid.toString())) return (int) table.getValue(foodc, uuidc, uuid.toString());
        else return 20;
    }

    public int getMaxhomes() {
        if(table.existEntry(maxhomesc, uuidc, uuid.toString())) {
            return (int) table.getValue(maxhomesc, uuidc, uuid.toString());
        }else {
            setMaxhomes(3);
            return 3;
        }
    }

    public void setMaxhomes(int maxhomes) {
        table.setValue(maxhomesc, maxhomes, uuidc, uuid.toString());
    }

    public void saveHearts(Player player) {
        table.setValue(heartsc, player.getHealth(), uuidc, uuid.toString());
    }

    public void saveFood(Player player) {
        table.setValue(foodc, player.getFoodLevel(), uuidc, uuid.toString());
    }

    public UUID getUuid() {
        return uuid;
    }

    public static PlayerProfile getProfile(UUID uuid) {
        for(PlayerProfile profile : profiles) {
            if(profile.getUuid().equals(uuid)) return profile;
        }
        return new PlayerProfile(uuid);
    }

}
