package de.koppy.basics.api;

import de.koppy.basics.BasicSystem;
import de.koppy.basics.api.scoreboard.DefaultScoreboard;
import de.koppy.economy.api.PlayerAccount;
import de.koppy.job.JobSystem;
import de.koppy.job.api.JobType;
import de.koppy.job.api.PlayerJob;
import de.koppy.job.api.TaskAmount;
import de.koppy.land.api.Land;
import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.mission.api.MissionHandler;
import de.koppy.mysql.api.Column;
import de.koppy.mysql.api.ColumnType;
import de.koppy.mysql.api.Table;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    private final static Column warptokensc = new Column("warptokens",  ColumnType.INT, 200);
    private final static Column nicknamec = new Column("nickname", ColumnType.VARCHAR, 200);
    private final static Column uidc = new Column("uid", ColumnType.VARCHAR, 200);
    private final static Column maxlandsc = new Column("maxlands", ColumnType.INT, 200);
    private final static Column landsc = new Column("lands", ColumnType.TEXT, 40000);
    private int sessionplaytime = 0;
    private TaskAmount taskamount;
    private DefaultScoreboard scoreboard;
    private Language language;

    /**
     * Rules:
     * 1. Only saving/loading data on leave/join
     * 2. If necessary save data immediately
     * 3. If necessary lock before executing action
     *
     */
    public PlayerProfile(UUID uuid) {
        profiles.add(this);
        this.uuid = uuid;
        this.scoreboard = new DefaultScoreboard(Bukkit.getPlayer(uuid));
        taskamount = new TaskAmount();
        PlayerJob pj = new PlayerJob(uuid);
        taskamount.registerTaskAmount(pj.getJob().getTaskAmount());
        if(table.existEntry(languagec, uuidc, uuid.toString())) {
            this.language = Language.fromString((String) table.getValue(languagec, uuidc, uuid.toString()));
        }else {
            this.language = Language.ENGLISH;
            setLanguage(this.language);
        }
        Player player = Bukkit.getPlayer(uuid);
        new MissionHandler().checkDailies(player);
        new MissionHandler().checkWeeklies(player);
        new MissionHandler().checkSeasonal(player);

        run();
    }

    private void run() {
        new BukkitRunnable(){
            @Override
            public void run() {
                if(Bukkit.getPlayer(uuid) == null) cancel();
                sessionplaytime++;
                if(Bukkit.getPlayer(uuid) != null && Bukkit.getPlayer(uuid).getScoreboard() != null) {
                    scoreboard.updateRank();
                }
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

    //* Lands
    public void setMaxLands(int maxlands) {
        table.setValue(maxlandsc, maxlands, uuidc, this.uuid.toString());
    }

    public int initmaxlands = 5;

    public int getMaxLands() {
        if(table.existEntry(maxlandsc, uuidc, uuid.toString())) {
            int amount = (Integer) table.getValue(maxlandsc, uuidc, uuid.toString());
            if(amount < initmaxlands) {
                setMaxLands(initmaxlands);
                return initmaxlands;
            }else {
                return amount;
            }
        }else {
            setMaxLands(initmaxlands);
            return initmaxlands;
        }
    }

    public void addLand(Chunk chunk) {
        addLand(chunk, uuid.toString());
    }

    public void removeLand(Chunk chunk) {
        removeLand(chunk, uuid.toString());
    }

    public void saveLands(List<String> lands) {
        saveLands(lands, uuid.toString());
    }

    public List<String> getLands() {
        return getLands(uuid.toString());
    }



    //* JobSystem
    public TaskAmount getTaskamount() {
        return taskamount;
    }

    public void setJob(JobType jobtype) {
        Column currentjobc = new Column("currentjob", ColumnType.VARCHAR, 200);
        JobSystem.getTable().setValue(currentjobc, jobtype.toString(), uuidc, uuid.toString());
        getTaskamount().registerTaskAmount(jobtype.getTaskAmount());
    }

    public UUID getUuid() {
        return uuid;
    }

    //* Scoreboard
    public DefaultScoreboard getScoreboard() {
        return scoreboard;
    }

    //* Nick-Data
    public boolean isNicked() {
        if(getNickname() == null) return false;
        return (!getNickname().equals("none"));
    }

    public String getNickname() {
        if(table.existEntry(nicknamec, uuidc, uuid.toString())) {
            String nickname = (String) table.getValue(nicknamec, uuidc, uuid.toString());
            return nickname;
        }else {
            setNickname("none");
            return null;
        }
    }

    public void setNickname(String nickname) {
        table.setValue(nicknamec, nickname, uuidc, uuid.toString());
    }

    //* Warp-Tokens
    public void setWarptokens(int tokens) {
        table.setValue(warptokensc, tokens, uuidc, uuid.toString());
    }

    public int getWarptokens() {
        if(table.existEntry(warptokensc, uuidc, uuid.toString())) {
            return (Integer) table.getValue(warptokensc, uuidc, uuid.toString());
        }else {
            setWarptokens(0);
            return 0;
        }
    }

    //* Money
    public double getMoney() {
        return new PlayerAccount(uuid).getMoney();
    }

    public void remove() {
        setPlaytime(getTotalPlaytime());
        profiles.remove(this);
    }

    public static PlayerProfile getProfile(UUID uuid) {
        for(PlayerProfile profile : profiles) {
            if(profile.getUuid().equals(uuid)) return profile;
        }
        return new PlayerProfile(uuid);
    }

    public static int getTotalPlaytimeSQL(UUID uniqueId) {
        if(table.existEntry(playtimec, uuidc, uniqueId.toString())) {
            return (int) table.getValue(playtimec, uuidc, uniqueId.toString());
        }else {
            return 0;
        }
    }

    public static void setWarptokens(UUID uuid, int tokens) {
        table.setValue(warptokensc, tokens, uuidc, uuid.toString());
    }

    private static int getWarptokens(UUID uuid) {
        if(table.existEntry(warptokensc, uuidc, uuid.toString())) {
            return (Integer) table.getValue(warptokensc, uuidc, uuid.toString());
        }else {
            return 0;
        }
    }

    public static void addWarpTokens(UUID uuid, int i) {
        int a = getWarptokens(uuid);
        a+=i;
        setWarptokens(uuid, a);
    }

    public static void removeWarpTokens(UUID uuid, int i) {
        int a = getWarptokens(uuid);
        a = a - i;
        setWarptokens(uuid, a);
    }

    public static void addLand(Chunk chunk, String uuid) {
        String chunkstring = chunk.getX()+"I"+chunk.getZ();
        List<String> lands = getLands(uuid);
        if(!lands.contains(chunkstring)) lands.add(chunkstring);
        saveLands(lands, uuid);
    }

    public static void removeLand(Chunk chunk, String uuid) {
        String chunkstring = chunk.getX()+"I"+chunk.getZ();
        List<String> lands = getLands(uuid);
        lands.remove(chunkstring);
        saveLands(lands, uuid);
    }

    public static void saveLands(List<String> lands, String uuid) {
        String raw = "";
        if(!lands.isEmpty()) {
            for (String s : lands) {
                raw = raw + s + ":";
            }
            raw = raw.substring(0, raw.length()-1);
        }
        table.setValue(landsc, raw, uuidc, uuid);
    }

    public static List<String> getLands(String uuid) {
        List<String> lands = new ArrayList<>();
        String landsraw;
        if(table.existEntry(landsc, uuidc, uuid)) {
            landsraw = (String) table.getValue(landsc, uuidc, uuid);
        }else {
            saveLands(lands, uuid);
            return lands;
        }
        if(landsraw == null || landsraw.isEmpty()) return lands;
        if(!landsraw.contains(":")) {
            lands.add(landsraw);
            return lands;
        }
        for(String land : landsraw.split(":")) {
            lands.add(land);
        }
        return lands;
    }


    public String getUID() {
        if(table.existEntry(uidc, uuidc, uuid.toString())) {
            String uid = (String) table.getValue(uidc, uuidc, uuid.toString());
            if(uid != null) {
                return uid;
            }
        }
        return setUID();
    }

    private String setUID() {
        String uid = generateNewPassword(6);
        table.setValue(uidc, uid, uuidc, uuid.toString());
        return uid;
    }

    public static String getRNDMstring() {
        String[] var = {"a", "A", "b", "B", "c", "C", "d", "D", "e", "E", "f", "F", "g", "G",
                "h", "H", "i", "I", "j", "J", "k", "K", "l", "L", "m", "M", "n", "N", "o",
                "O", "p", "P", "q", "Q", "r", "R", "s", "S", "t", "T", "u", "U", "v", "V",
                "w", "W", "x", "X", "y", "Y", "z", "Z", "0", "1", "2", "3", "4", "5", "6",
                "7", "8", "9"};

        Random rndm = new Random();
        Integer rndmint = rndm.nextInt(var.length);

        return var[rndmint];
    }

    public static String generateNewPassword(int length) {
        String out="";
        for(int i=0; i<length; i++) {
            out = out+getRNDMstring();
        }
        return out;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }
}