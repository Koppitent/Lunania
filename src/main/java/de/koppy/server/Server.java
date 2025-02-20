package de.koppy.server;

import de.koppy.bansystem.BanSystem;
import de.koppy.bansystem.api.BanIDs;
import de.koppy.bansystem.api.MuteIDs;
import de.koppy.basics.BasicSystem;
import de.koppy.basics.commands.Debug;
import de.koppy.cases.CaseSystem;
import de.koppy.economy.EconomySystem;
import de.koppy.job.JobSystem;
import de.koppy.land.LandSystem;
import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.mission.MissionSystem;
import de.koppy.mysql.MysqlSystem;
import de.koppy.mysql.api.Column;
import de.koppy.mysql.api.ColumnType;
import de.koppy.nick.NickSystem;
import de.koppy.npc.NpcSystem;
import de.koppy.npc.api.NPCFile;
import de.koppy.quest.QuestSystem;
import de.koppy.server.commands.test;
import de.koppy.server.listener.serverevents;
import de.koppy.shop.ShopSystem;
import de.koppy.shop.api.Adminshop;
import de.koppy.shop.listener.ShopListener;
import de.koppy.warp.WarpSystem;
import de.koppy.world.WorldSystem;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Server {

    private  String name;
    private final String prefix = "§3Lunania §8| §r";
    private int maxplayer = 10;
    private List<String> motdlist = new ArrayList<>();
    private int day = 0;
    private int week = 0;
    private int season = 0;
    private boolean shutdownbackup;
    private SystemController systemController;
    private String joinmessage = "§8§m------------------\n§7§r\n  §7Welcome to §3Lunania  \n§7\n§8§m------------------";
    private String lunaniasymbol = "§f\uE007";
    private String discordlink = "discord.gg";
    private int broadcasttimer = 0;
    private List<String> broadcastmessages = new ArrayList<>();
    private boolean versionmessage = true;
    private String url = "https://drive.kytress.de/index.php/s/6xGxbm49xk8R4L3/download";
    private List<String> activesystems = new ArrayList<>();
    private boolean consoledebug = false;
    private Location spawnloc;
    private boolean customRanks = false;

    public Server() {
        this.name = "main";
        String lunania = ""+ChatColor.of("#005263")+"§lL"+ChatColor.of("#0A5B6A")+"§lu"+ChatColor.of("#146471")+"§ln"+ChatColor.of("#1E6C78")+"§la"+ChatColor.of("#277580")+"§ln"+ChatColor.of("#317E87")+"§li"+ChatColor.of("#3B878E")+"§la"+ChatColor.of("#459095")+"§l."+ChatColor.of("#4F989C")+"§ln"+ChatColor.of("#59A1A3")+"§le"+ChatColor.of("#63AAAA")+"§lt";
        this.motdlist.clear();
        this.motdlist.add("                §8§l» "+lunania+" §8§l«                      \n        §7» "+ChatColor.of("#166b37")+"Adventure §7and "+ChatColor.of("#156c6e")+"Building");
        this.motdlist.add("                §8§l» "+lunania+" §8§l«                      \n        §7» "+ChatColor.of("#c4351f")+"§lPre-Alpha§r §7starting soon.");
        this.broadcastmessages.add("     §7Willkommen auf §3Lunania.net§7!     ");

        new BanIDs(99, "Admin", 1000L * 60L * 60L * 24000000L, Material.REDSTONE_BLOCK);
        new BanIDs(1, "Hacking/Cheating", 1000L*60L*60L*24L*30L, Material.IRON_SWORD);
        new BanIDs(2, "Peterismus", 1000L*60L*60L*24L*30L*3L, Material.DIAMOND_SHOVEL);

        new MuteIDs(99, "Admin", 1000L * 60L * 60L * 24000000L, Material.REDSTONE_BLOCK);
        new MuteIDs(1, "Hacking/Cheating", 1000L*60L*60L*24L*30L, Material.IRON_SWORD);
        new MuteIDs(2, "Peterismus", 1000L*60L*60L*24L*30L*3L, Material.DIAMOND_SHOVEL);

        this.systemController = new SystemController();

        File fileshop = new File("plugins/Lunania", "savedshops.yml");
        Bukkit.getConsoleSender().sendMessage("§3INFO §7Loading Shops...");
        if(fileshop.exists()) {
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(fileshop);
            for(String s : cfg.getKeys(false)) {
                String[] sS = s.split("I");
                Location location = new Location(Bukkit.getWorld("world"), Integer.parseInt(sS[0]), Integer.parseInt(sS[1]), Integer.parseInt(sS[2]));
                ShopListener.signchestlocs.put(location, cfg.getLocation(s));
            }
        }


        loadConfig();
        checkSystems();
        run();

        File file = new File("plugins/Lunania/Adminshops");
        if(file.listFiles() != null) {
            for(File f : file.listFiles()) {
                new Adminshop(f);
            }
        }

        File file2 = new File("plugins/Lunania/Npcs");
        if(file2.listFiles() != null) {
            for(File f : file2.listFiles()) {
                NPCFile.loadFile(f);
            }
        }

    }

    public void save() {
        saveShops();
    }

    private void saveShops() {
        Bukkit.getConsoleSender().sendMessage("§3INFO §7Saving Shops...");
        File fileshop = new File("plugins/Lunania", "savedshops.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(fileshop);
        for(Location location : ShopListener.signchestlocs.keySet()) {
            cfg.set((int) location.getX()+"I"+(int) location.getY()+"I"+(int) location.getZ(), ShopListener.signchestlocs.get(location));
        }
        try {
            cfg.save(fileshop);
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage("§4ERROR §7Couldnt load UserShops");
        }
    }

    private void checkSystems() {
        LunaniaSystem.registerCommand("test", new test());
        LunaniaSystem.registerListener(new serverevents());

        //* Define what should load in loadSystems() !
        systemController.addOption(new Option("MySQL", true, "Enable/Disable the MySQLSystem", true));
        systemController.addOption(new Option("Basics", true, "Enable/Disable the BasicsSystem", true));
        systemController.addOption(new Option("Bansystem", true, "Enable/Disable the BanSystem", true));
        systemController.addOption(new Option("Cases", true, "Enable/Disable the CaseSystem", true));
        systemController.addOption(new Option("Economy", true, "Enable/Disable the EconomySystem", true));
        systemController.addOption(new Option("Job", true, "Enable/Disable the JobSystem", true));
        systemController.addOption(new Option("Nick", true, "Enable/Disable the NickSystem", true));
        systemController.addOption(new Option("NPC", true, "Enable/Disable the NPCSystem", true));
        systemController.addOption(new Option("Shop", true, "Enable/Disable the ShopSystem", true));
        systemController.addOption(new Option("Warp", true, "Enable/Disable the WarpSystem", true));
        systemController.addOption(new Option("Quest", true, "Enable/Disable the QuestSystem", true));
        systemController.addOption(new Option("Land", true, "Enable/Disable the LandSystem", true));
        systemController.addOption(new Option("Mission", true, "Enable/Disable the MissionSystem", true));
        systemController.addOption(new Option("World", true, "Enable/Disable the WorldSystem", true));


        loadOptions();
    }

    public void loadOptions() {
        for(Option option : systemController.getOptions()) {
            if(option.isSystem()) {
                if((boolean)systemController.getValue(option)) {
                    Bukkit.getConsoleSender().sendMessage("§3"+option.getName() + "§7: §aenabled");
                    activesystems.add(option.getName());
                    loadSystems(option.getName());
                }else {
                    Bukkit.getConsoleSender().sendMessage("§3"+option.getName() + "§7: §cdisabled");
                }
            }
        }
    }

    public boolean isCustomRanks() {
        return customRanks;
    }

    public Location getSpawnloc() {
        return spawnloc;
    }

    public void setSpawnloc(Location spawnloc) {
        this.spawnloc = spawnloc;
        File file = new File("plugins/Lunania", "server.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set("spawnloc", this.spawnloc);
        try {
            cfg.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadSystems(String system) {
        switch (system) {
            case "Bansystem":
                new BanSystem().loadClasses();
                break;
            case "Basics":
                new BasicSystem().loadClasses();
                break;
            case "Cases":
                new CaseSystem().loadClasses();
                break;
            case "Economy":
                new EconomySystem().loadClasses();
                break;
            case "Job":
                new JobSystem().loadClasses();
                break;
            case "Nick":
                new NickSystem().loadClasses();
                break;
            case "MySQL":
                new MysqlSystem().loadClasses();
                break;
            case "NPC":
                new NpcSystem().loadClasses();
                break;
            case "Shop":
                new ShopSystem().loadClasses();
                break;
            case "Warp":
                new WarpSystem().loadClasses();
                break;
            case "Quest":
                new QuestSystem().loadClasses();
                break;
            case "Land":
                new LandSystem().loadClasses();
                break;
            case "Mission":
                new MissionSystem().loadClasses();
                break;
            case "World":
                new WorldSystem().loadClasses();
                break;
            default:
                Bukkit.getConsoleSender().sendMessage("§4ERROR §7Cant find System for §e" + system);
                break;
        }
    }

    public void setConsoledebug(boolean consoledebug) {
        this.consoledebug = consoledebug;
    }

    public boolean isConsoledebug() {
        return Debug.consoledebug;
    }

    public int getDay() {
        return day;
    }

    public int getWeek() {
        return week;
    }

    public int getSeason() {
        return season;
    }

    public boolean isSystemActive(String system) {
        return activesystems.contains(system);
    }

    public SystemController getSystemController() {
        return systemController;
    }

    public void applyTexturepack(Player player) {
        player.setResourcePack(url);
    }

    public void setPlayerListHeaderFooter(Player player) {
        player.setPlayerListHeaderFooter("Lunania", "Willkommen");
    }

    public String getDiscordlink() {
        return discordlink;
    }

    public String getLunaniasymbol() {
        return lunaniasymbol;
    }

    public boolean isVersionmessage() {
        return versionmessage;
    }

    private void loadConfig() {
        File file = new File("plugins/Lunania", "server.yml");
        if(file.exists()) {
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            if(!cfg.contains("name")) cfg.set("name", this.name);
            else this.name = cfg.getString("name");
            if(!cfg.contains("motds")) cfg.set("motds", this.motdlist);
            else this.motdlist = cfg.getStringList("motds");
            if(!cfg.contains("maxplayer")) cfg.set("maxplayer", this.maxplayer);
            else this.maxplayer = cfg.getInt("maxplayer");
            if(!cfg.contains("day")) cfg.set("day", this.day);
            else this.day = cfg.getInt("day");
            if(!cfg.contains("week")) cfg.set("week", this.week);
            else this.week = cfg.getInt("week");
            if(!cfg.contains("season")) cfg.set("season", this.season);
            else this.season = cfg.getInt("season");
            if(!cfg.contains("joinmessage")) cfg.set("joinmessage", this.joinmessage);
            else this.joinmessage = cfg.getString("joinmessage");
            if(!cfg.contains("broadcastmessages")) cfg.set("broadcastmessages", this.broadcastmessages);
            else this.broadcastmessages = cfg.getStringList("broadcastmessages");
            if(!cfg.contains("broadcasttimer")) cfg.set("broadcasttimer", this.broadcasttimer);
            else this.broadcasttimer = cfg.getInt("broadcasttimer");
            if(!cfg.contains("versionmessage")) cfg.set("versionmessage", this.versionmessage);
            else this.versionmessage = cfg.getBoolean("versionmessage");
            if(!cfg.contains("spawnloc")) cfg.set("spawnloc", this.spawnloc);
            else this.spawnloc = cfg.getLocation("spawnloc");
            if(!cfg.contains("consoledebug")) cfg.set("consoledebug", this.consoledebug);
            else this.consoledebug = cfg.getBoolean("consoledebug");
            if(!cfg.contains("customranks")) cfg.set("customranks", this.customRanks);
            else this.customRanks = cfg.getBoolean("customranks");
            try {
                cfg.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else {
            try {
                initServerFile(file);
            } catch (IOException e) {
                throw new RuntimeException("ServerFileInitError");
            }
        }
    }

    private void run() {
        Bukkit.getScheduler().runTaskTimer(LunaniaSystem.getPlugin(), new Runnable() {
            @SuppressWarnings("deprecation")
            public void run() {
                Date date = new Date();
                if(date.getMinutes() == 0 && date.getSeconds() == 0) {
                    BanSystem.getTable().existEntry(new Column("uuid", ColumnType.VARCHAR, 200), "uwu");
                }

                if(date.getHours() == 4 && date.getMinutes() == 0 && date.getSeconds() == 0) {
                    day++;
                    setValueServer("day", day);
                    if(date.getDay() == 1) {
                        week++;
                        setValueServer("week", week);
                    }
                }
                if(date.getHours() == 0 && date.getMinutes() == 0 && date.getSeconds() == 0) {
                    shutdownbackup = true;
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        p.kickPlayer("§cServer start Backup of the Worlds.\nServer will be back online in around 5 Minutes.");
                    }

                    if(name.equalsIgnoreCase("main")) {
                        WorldManager wm = new WorldManager();
                        wm.backupWorld(Bukkit.getWorld("world").getWorldFolder());
                    }

                }
                if(date.getHours() == 0 && date.getMinutes() == 3 && date.getSeconds() == 0 && shutdownbackup) {
                    Bukkit.getServer().shutdown();
                }
            }
        }, 20, 20);



        if(broadcasttimer > 0) {
            Bukkit.getScheduler().runTaskTimer(LunaniaSystem.getPlugin(), new Runnable() {
                @SuppressWarnings("deprecation")
                public void run() {
                    String msg = getBroadcastmessage();
                    String pins = "§8§m";
                    for(int i=0; i<30; i++) {
                        pins = pins + "-";
                    }
                    for(Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage("");
                        player.sendMessage(pins);
                        player.sendMessage("");
                        player.sendMessage(msg);
                        player.sendMessage("");
                        player.sendMessage(pins);
                        player.sendMessage("");
                    }
                }
            }, 20 * broadcasttimer, 20 * broadcasttimer);
        }
    }

    public String getBroadcastmessage() {
        Random rndm = new Random();
        return broadcastmessages.get(rndm.nextInt(broadcastmessages.size()));
    }

    public boolean isShutdownbackup() {
        return shutdownbackup;
    }

    private void setValueServer(String name, Object obj) {
        File file = new File("plugins/Lunania", "server.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set(name, obj);
        try {
            cfg.save(file);
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage("§4ERROR §7cant save server.yml file");
        }
    }


    public int getMaxplayer() {
        return maxplayer;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getLunania() {
        return ""+ ChatColor.of("#0073FF")+"L"+ChatColor.of("#0089FF")+"u"+ChatColor.of("#009EFF")+"n"+ChatColor.of("#00B4FF")+"a"+ChatColor.of("#00C9FF")+"n"+ChatColor.of("#00DFFF")+"i"+ChatColor.of("#00F4FF")+"a";
    }

    public String getName() {
        return name;
    }

    public void sendConsoleMessage(String msg) {
        Bukkit.getConsoleSender().sendMessage("§8[§3LUNANIA§8] §7" + msg);
    }

    public void sendConsoleError(String msg) {
        Bukkit.getConsoleSender().sendMessage("§8[§4ERROR§8] §7" + msg);
    }

    public void broadcastMessage(String msg) {
        Bukkit.getServer().broadcastMessage("");
        Bukkit.getServer().broadcastMessage(msg);
        Bukkit.getServer().broadcastMessage("");
    }

    public String getMotd() {
        Random rndm = new Random();
        return motdlist.get(rndm.nextInt(motdlist.size()));
    }

    public void setMaxplayer(int maxplayer) {
        Bukkit.getServer().setMaxPlayers(maxplayer);
        this.maxplayer = maxplayer;
    }

    private void initServerFile(File file) throws IOException {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set("motds", motdlist);
        cfg.set("name", "main");
        cfg.set("maxplayer", maxplayer);
        cfg.set("day", 0);
        cfg.set("week", 0);
        cfg.set("season", 0);
        cfg.set("joinmessage", joinmessage);
        cfg.set("broadcastmessages", broadcastmessages);
        cfg.set("broadcasttimer", 0);
        List<String> list = new ArrayList<>();
        list.add("Every how many seconds a message should be broadcastet");
        list.add("Set to 0 if disabling");
        cfg.setComments("broadcasttimer", list);
        cfg.save(file);
    }

    public String getJoinMessage() {
        return joinmessage;
    }
}