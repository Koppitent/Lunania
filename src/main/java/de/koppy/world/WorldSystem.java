package de.koppy.world;

import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.server.SubSystem;
import de.koppy.world.api.World;
import de.koppy.world.commands.world;
import de.koppy.world.listener.WorldListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class WorldSystem implements SubSystem {
    @Override
    public void loadListener() {
        LunaniaSystem.registerListener(new WorldListener());
    }

    @Override
    public void loadCommands() {
        LunaniaSystem.registerCommand("world", new world());
    }

    @Override
    public void loadClasses() {

        //* Check for World-File Exists
        File file = new File("plugins/Lunania", "worlds.yml");
        if(!file.exists()) {
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            cfg.set("world", Bukkit.getWorld("world").getSpawnLocation());
            cfg.set("world_nether", Bukkit.getWorld("world_nether").getSpawnLocation());
            cfg.set("world_the_end", Bukkit.getWorld("world_the_end").getSpawnLocation());
            try {
                cfg.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else {
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            for(String worldname : cfg.getKeys(false)) {
                if(worldname.isEmpty()) return;
                if(worldname.equalsIgnoreCase("world") || worldname.equalsIgnoreCase("world_nether") || worldname.equalsIgnoreCase("world_the_end")) continue;
                File worldfile = new File(worldname);
                Bukkit.getConsoleSender().sendMessage("§dWorld loading: §5" + worldname);
                if(file.exists()) {
                    new World(worldname).loadWorld();
                }else {
                    cfg.set(worldname, null);
                }
            }
        }

        //* Load classes
        loadCommands();
        loadListener();
    }
}
