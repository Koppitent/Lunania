package de.koppy.basics.api;

import de.koppy.basics.BasicSystem;
import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.server.Column;
import de.koppy.server.ColumnType;
import de.koppy.server.Table;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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
    private final static Column playtimec = new Column("playtime", ColumnType.INT, 200);
    private final static Column languagec = new Column("language", ColumnType.VARCHAR, 200);
    private final static Column tptogglec = new Column("tptoggle", ColumnType.BOOL, 200);
    private final static Column msgtogglec = new Column("msgtoggle", ColumnType.BOOL, 200);
    private final static Column usetexturepackc = new Column("usetexturepack", ColumnType.BOOL, 200);
    private int sessionplaytime = 0;
    private Language language;

    public PlayerProfile(UUID uuid) {
        this.uuid = uuid;
        if(table.existEntry(languagec, uuidc, uuid.toString())) {
            this.language = Language.fromString((String) table.getValue(languagec, uuidc, uuid.toString()));
        }else {
            this.language = Language.ENGLISH;
            setLanguage(this.language);
        }

        run();
    }

    private void run() {
        new BukkitRunnable(){
            @Override
            public void run() {
                sessionplaytime++;
                if(Bukkit.getPlayer(uuid) == null) cancel();
            }
        }.runTaskTimer(LunaniaSystem.getPlugin(), 20, 20);
    }

    //* TPA
    public void setTpaAccept(boolean bool) {
        table.setValue(tptogglec, bool, uuidc, uuid.toString());
    }

    public boolean isTpaAccept() {
        return ((Boolean) table.getValue(tptogglec, uuidc, uuid.toString()));
    }

    //* Messages
    public void setMsgAccept(boolean bool) {
        table.setValue(msgtogglec, bool, uuidc, uuid.toString());
    }

    public boolean isMsgAccept() {
        return ((Boolean) table.getValue(msgtogglec, uuidc, uuid.toString()));
    }

    //* TexturePack
    public void setUseTP(boolean bool) {
        table.setValue(usetexturepackc, !bool, uuidc, uuid.toString());
    }

    public boolean isUseTexturepack() {
        return (!(Boolean) table.getValue(usetexturepackc, uuidc, uuid.toString()));
    }

    public void setPlaytime(int playtime) {
        table.setValue(playtimec, playtime, uuidc, uuid.toString());
    }

    public int getTotalPlaytime() {
        return getPlaytimeDatabase() + sessionplaytime;
    }

    public int getPlaytimeSinceJoin() {
        return sessionplaytime;
    }

    public int getPlaytimeDatabase() {
        if(table.existEntry(playtimec, uuidc, uuid.toString())) {
            return (int) table.getValue(playtimec, uuidc, uuid.toString());
        }else {
            setPlaytime(0);
            return 0;
        }
    }

    //* Homes
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

    public void removeHome(String homename) {
        List<Home> homelist = getHomes();
        Home homef = null;
        for(Home home : homelist) {
            if(home.getName().equalsIgnoreCase(homename)) {
                homef = home;
            }
        }
        if(homef != null) homelist.remove(homef);
        saveHomes(homelist);
    }

    public boolean existHome(String name) {
        List<Home> homelist = getHomes();
        for(Home home : homelist) {
            if(home.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public Home getHome(String name) {
        for(Home home : getHomes()) {
            if(home.getName().equalsIgnoreCase(name)) {
                return home;
            }
        }
        return null;
    }

    public void saveHomes(List<Home> homelist) {
        table.setValue(homesc, HomesToString(homelist), uuidc, uuid.toString());
    }

    public void resetHomes() {
        saveHomes(new ArrayList<>());
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
        if(homes.isEmpty()) return out;
        for(Home home : homes) {
            out = out + home.toString() + ";";
        }
        out = out.substring(0, out.length()-1);
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
            int maxhomes = (int) table.getValue(maxhomesc, uuidc, uuid.toString());
            if(maxhomes < 3) {
                setMaxhomes(3);
                return 3;
            }
            return maxhomes;
        }else {
            setMaxhomes(3);
            return 3;
        }
    }

    public void setMaxhomes(int maxhomes) {
        table.setValue(maxhomesc, maxhomes, uuidc, uuid.toString());
    }


    //* save sync-playerdata
    public void saveHearts(Player player) {
        table.setValue(heartsc, player.getHealth(), uuidc, uuid.toString());
    }

    public void saveFood(Player player) {
        table.setValue(foodc, player.getFoodLevel(), uuidc, uuid.toString());
    }

    //* Language
    public void setLanguage(Language language) {
        this.language = language;
        table.setValue(languagec, language.toString().toLowerCase(), uuidc, this.uuid.toString());
    }

    public Language getLanguage() {
        return language;
    }

    public String getMessage(String abbreviation) {
        Column langcolumn = new Column(language.toString().toLowerCase(), ColumnType.VARCHAR, 200);
        Column abbreviationc = new Column("abbreviation", ColumnType.VARCHAR, 200);
        if(BasicSystem.getLangtable().getValue(langcolumn, abbreviationc, abbreviation) != null) {
            return ((String) BasicSystem.getLangtable().getValue(langcolumn, abbreviationc, abbreviation)).replace("&", "§");
        }else {
            return "§cno message set for language "+language.toString().toLowerCase()+". ("+abbreviation+")";
        }
    }

    public void sendMessage(String prefix, String abbreviation) {
        Bukkit.getPlayer(uuid).sendMessage(prefix + getMessage(abbreviation));
    }

    public void sendMessage(String abbreviation) {
        Bukkit.getPlayer(uuid).sendMessage(getMessage(abbreviation));
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

    public void remove() {
        setPlaytime(getTotalPlaytime());
        profiles.remove(this);
    }

    public static int getTotalPlaytimeSQL(UUID uniqueId) {
        if(table.existEntry(playtimec, uuidc, uniqueId.toString())) {
            return (int) table.getValue(playtimec, uuidc, uniqueId.toString());
        }else {
            return 0;
        }
    }

    //TODO: implement Nick-System
    public boolean isNicked() {
        return false;
    }
}