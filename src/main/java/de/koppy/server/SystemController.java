package de.koppy.server;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SystemController {

    private List<Option> options = new ArrayList<>();

    public SystemController(){
        File file = new File("plugins/Lunania", "config.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        try {
            cfg.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeOption(Option option) {
        options.remove(option);
        File file = new File("plugins/Lunania", "config.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set(option.getName(), null);
        try {
            cfg.save(file);
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage("§4ERROR §7cant save file config.yml");
        }
    }

    public void addOption(Option option) {
        if(existOption(option.getName())) return;
        options.add(option);
        File file = new File("plugins/Lunania", "config.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        if(!cfg.contains(option.getName())) {
            cfg.set(option.getName(), option.getDefaultvalue());
            cfg.setComments(option.getName(), option.getDescription());
            try {
                cfg.save(file);
            } catch (IOException e) {
                Bukkit.getConsoleSender().sendMessage("§4ERROR §7cant save file config.yml");
            }
        }
    }

    public boolean existOption(String name) {
        for(Option option : options) {
            if(option.getName().equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }

    public Option getOption(String name) {
        for(Option option : options) {
            if(option.getName().equalsIgnoreCase(name)){
                return option;
            }
        }
        return null;
    }

    public List<Option> getOptions() {
        return options;
    }

    public Object getValue(Option option) {
        File file = new File("plugins/Lunania", "config.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        return cfg.get(option.getName());
    }

    /*
        Watch out using this. response could be null!
     */
    public Object getValue(String name) {
        File file = new File("plugins/Lunania", "config.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        return cfg.get(name);
    }

}