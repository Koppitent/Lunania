package de.koppy.server;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Server {

    private  String name;
    private final String prefix = "§3Lunania §8| §r";
    private int maxplayer = 10;
    private List<String> motdlist = new ArrayList<>();

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
        }else {
            try {
                initServerFile(file);
            } catch (IOException e) {
                throw new RuntimeException("ServerFileInitError");
            }
        }
    }


    public int getMaxplayer() {
        return maxplayer;
    }

    public String getPrefix() {
        return prefix;
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
        cfg.save(file);
    }

}