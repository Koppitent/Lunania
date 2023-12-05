package de.koppy.server;

import de.koppy.lunaniasystem.LunaniaSystem;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
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

    public Server() {
        this.name = "main";
        loadConfig();
    }

    public void loadConfig() {
        File file = new File("plugins/Lunania", "server.yml");
        if(file.exists()) {
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            this.name = cfg.getString("name");
            this.motdlist = cfg.getStringList("motds");
            setMaxplayer(cfg.getInt("maxplayer"));
            this.day = cfg.getInt("day");
            this.week = cfg.getInt("week");
            this.season = cfg.getInt("season");
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
        cfg.set("motds", new ArrayList<>());
        cfg.set("name", "main");
        cfg.set("maxplayer", maxplayer);
        cfg.set("day", 0);
        cfg.set("week", 0);
        cfg.set("season", 0);
        cfg.save(file);
    }

}