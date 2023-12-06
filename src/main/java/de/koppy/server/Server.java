package de.koppy.server;

import de.koppy.bansystem.BanSystem;
import de.koppy.bansystem.commands.Ban;
import de.koppy.basics.BasicSystem;
import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.server.commands.test;
import de.koppy.server.listener.serverevents;
import io.netty.channel.epoll.Epoll;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.awt.*;
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
    private String url = "https://download.mc-packs.net/pack/2f6719d7c1041034a7dcfcf3b1368261452aff85.zip";
    private String hash = "2f6719d7c1041034a7dcfcf3b1368261452aff85";
    private byte[] sha1 = new byte[hash.length() / 2];

    public Server() {
        this.name = "main";
        String lunania = ""+ChatColor.of("#005263")+"§lL"+ChatColor.of("#0A5B6A")+"§lu"+ChatColor.of("#146471")+"§ln"+ChatColor.of("#1E6C78")+"§la"+ChatColor.of("#277580")+"§ln"+ChatColor.of("#317E87")+"§li"+ChatColor.of("#3B878E")+"§la"+ChatColor.of("#459095")+"§l."+ChatColor.of("#4F989C")+"§ln"+ChatColor.of("#59A1A3")+"§le"+ChatColor.of("#63AAAA")+"§lt";
        this.motdlist.clear();
        this.motdlist.add("                §8§l» "+lunania+" §8§l«                      \n        §7» "+ChatColor.of("#166b37")+"Adventure §7and "+ChatColor.of("#156c6e")+"Building");
        this.motdlist.add("                §8§l» "+lunania+" §8§l«                      \n        §7» "+ChatColor.of("#c4351f")+"§lPre-Alpha§r §7starting soon.");
        this.broadcastmessages.add("     §7Willkommen auf §3Lunania.net§7!     ");

        this.systemController = new SystemController();

        //* Some weird calc for the resourcepack
        for (int i = 0; i < sha1.length; i++) {
            int index = i * 2;
            int j = Integer.parseInt(hash.substring(index, index + 2), 16);
            sha1[i] = (byte) j;
        }

        loadConfig();
        checkSystems();
    }

    private void checkSystems() {
        LunaniaSystem.registerCommand("test", new test());
        LunaniaSystem.registerListener(new serverevents());

        systemController.addOption(new Option("Bansystem", true, "Enable/Disable the BanSystem", true));
        systemController.addOption(new Option("Basics", true, "Enable/Disable the BasicsSystem", true));

        for(Option option : systemController.getOptions()) {
            if(option.isSystem()) {
                if((boolean)systemController.getValue(option)) {
                    Bukkit.getConsoleSender().sendMessage("§3"+option.getName() + "§7: §aenabled");
                    loadSystems(option.getName());
                }else {
                    Bukkit.getConsoleSender().sendMessage("§3"+option.getName() + "§7: §cdisabled");
                }
            }
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
            default:
                Bukkit.getConsoleSender().sendMessage("§4ERROR §7Cant find System for §e" + system);
                break;
        }
    }

    public byte[] getSha1() {
        return sha1;
    }

    public String getHash() {
        return hash;
    }

    public String getUrl() {
        return url;
    }

    public void setPlayerListHeaderFooter(Player player) {
        player.setPlayerListHeader("\n\n"+lunaniasymbol+"\n");
        player.setPlayerListFooter("\n"+"§7Discord: §e"+discordlink+"\n"+"§7Website: §elunania.net"+"\n");
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
        run();
    }

    private void run() {
        Bukkit.getScheduler().runTaskTimer(LunaniaSystem.getPlugin(), new Runnable() {
            @SuppressWarnings("deprecation")
            public void run() {
                Date date = new Date();
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
                    //TODO: create World-Backup HERE
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