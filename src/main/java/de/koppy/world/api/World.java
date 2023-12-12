package de.koppy.world.api;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class World {

    private String name;
    private WorldType worldType = WorldType.NORMAL;
    private Environment environment = Environment.NORMAL;

    public World(String name) {
        this.name = name;
    }

    public static List<String> getAllWorlds() {
        File file = new File("plugins/Lunania", "worlds.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        List<String> out = new ArrayList<>();
        for(String s : cfg.getKeys(false)) {
            if(!s.isEmpty()) {
                out.add(s);
            }
        }
        return out;
    }

    public boolean existWorld() {
        File file = new File("plugins/Lunania", "worlds.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        return cfg.contains(name);
    }

    public void setWorldType(WorldType worldType) {
        this.worldType = worldType;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public void importWorld() {
        if(!existFile()) return;
        if(existWorld()) return;
        WorldCreator wc = new WorldCreator(name);
        wc.createWorld(); //* loaded
        //* saving
        File file = new File("plugins/Lunania", "worlds.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        org.bukkit.World world = Bukkit.getWorld(name);
        cfg.set(name + ".spawnlocation", getSpawnlocation(world.getSpawnLocation()));
        try {
            cfg.save(file);
        } catch (IOException e) {
            System.out.println("§4ERROR §7creating entry in file for new world");
        }
    }

    public boolean existFile() {
        File file = new File(name);
        return file.exists();
    }

    public void createWorld() {
        if(existWorld()) return;
        if(existFile()) return;
        WorldCreator wc = new WorldCreator(name);
        wc.type(worldType);
        wc.environment(environment);
        wc.createWorld();

        File file = new File("plugins/Lunania", "worlds.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        org.bukkit.World world = Bukkit.getWorld(name);
        cfg.set(name + ".spawnlocation", getSpawnlocation(world.getSpawnLocation()));
        try {
            cfg.save(file);
        } catch (IOException e) {
            System.out.println("§4ERROR §7creating entry in file for new world");
        }
    }

    public void deleteFile() {
        File filedel = new File(name);
        try {
            FileUtils.deleteDirectory(filedel);
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage("§4ERROR §7Couldnt delete worldfile.");
        }
    }

    public void deleteWorld() {
        if(!existWorld()) return;
        unloadWorld();
        File file = new File("plugins/Lunania", "worlds.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set(name, null);
        try {
            cfg.save(file);
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage("§4ERROR §7deleting world " + name +".");
        }
        deleteFile();
    }

    public Location getSpawnlocation() {
        File file = new File("plugins/Lunania", "worlds.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        if(!existWorld()) return null;
        return getLocationOfSpawn(cfg.getString(name + ".spawnlocation"));
    }

    public void setSpawnlocation(Location location) {
        File file = new File("plugins/Lunania", "worlds.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        if(!existWorld()) return;
        if(!location.getWorld().getName().equalsIgnoreCase(name)) return;
        cfg.set(name + ".spawnlocation", getSpawnlocation(location));
        try {
            cfg.save(file);
        } catch (IOException e) {
            System.out.println("§4ERROR §7creating entry in file for new world");
        }
    }

    public void loadWorld() {
        if(!existWorld()) return;
        WorldCreator wc = new WorldCreator(name);
        wc.createWorld();
    }

    public void unloadWorld() {
        if(Bukkit.getWorld(name) == null) return;
        Bukkit.unloadWorld(name, true);
    }

    private String getSpawnlocation(Location location) {
        return location.getX()+"I"+location.getY()+"I"+location.getZ()+"I"+location.getYaw()+"I"+location.getPitch();
    }

    private Location getLocationOfSpawn(String spawnloc) {
        String[] spawnlocs = spawnloc.split("I");
        double x = Double.parseDouble(spawnlocs[0]);
        double y = Double.parseDouble(spawnlocs[1]);
        double z = Double.parseDouble(spawnlocs[2]);
        float yaw = Float.parseFloat(spawnlocs[3]);
        float pitch = Float.parseFloat(spawnlocs[4]);
        return new Location(Bukkit.getWorld(name), x, y, z, yaw, pitch);
    }

}
