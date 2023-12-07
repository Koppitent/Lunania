package de.koppy.npc.api;

import java.io.File;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class NPCFile {

    private String name;

    public NPCFile(String name) {
        this.name = name;
    }

    public void create(Npc npc) {
        File file = new File("plugins/Lunania/Npcs", name+".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set("displayname", npc.getDisplayname());
        cfg.set("type", npc.getType().toString());
        cfg.set("typecontent", npc.getTypeContent());
        cfg.set("looking", npc.isLooking());
        cfg.set("location", npc.getLocation());
        cfg.set("prefix", npc.getPrefix());
        cfg.set("texture", npc.getTexture());
        cfg.set("signature", npc.getSignature());
        cfg.set("prefix", npc.getPrefix());
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        File file = new File("plugins/Lunania/Npcs", name+".yml");
        if(file.exists()) file.delete();
    }

    public static void loadFile(File file) {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        String displayname = cfg.getString("displayname");
        NpcType type = NpcType.fromString(cfg.getString("type"));
        String typecontent = (cfg.getString("typecontent"));
        boolean looking = cfg.getBoolean("looking");
        String texture = cfg.getString("texture");
        String signature = cfg.getString("signature");
        Location location = cfg.getLocation("location");
        String prefix = cfg.getString("prefix");

        new Npc(file.getName().replace(".yml", ""), displayname, location, texture, signature, typecontent, type, looking, prefix);
    }

    public Npc getNpc() {
        File file = new File("plugins/Lunania/Npcs", name+".yml");
        if(file.exists()) {
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            String displayname = cfg.getString("displayname");
            NpcType type = NpcType.fromString(cfg.getString("type"));
            String typecontent = (cfg.getString("typecontent"));
            boolean looking = cfg.getBoolean("looking");
            String texture = cfg.getString("texture");
            String signature = cfg.getString("signature");
            Location location = cfg.getLocation("location");
            String prefix = cfg.getString("prefix");

            return new Npc(name, displayname, location, texture, signature, typecontent, type, looking, prefix);
        }else {
            return null;
        }
    }

    public void saveSkin(String texture, String signature) {
        File file = new File("plugins/Lunania/Npcs", name+".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set("texture", texture);
        cfg.set("signature", signature);
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveLocation(Location location) {
        File file = new File("plugins/Lunania/Npcs", name+".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set("location", location);
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveLooking(boolean looking) {
        File file = new File("plugins/Lunania/Npcs", name+".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set("looking", looking);
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveType(NpcType type, String typecontent) {
        File file = new File("plugins/Lunania/Npcs", name+".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set("type", type.toString());
        cfg.set("typecontent", typecontent);
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveDisplayname(String displayname) {
        File file = new File("plugins/Lunania/Npcs", name+".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set("displayname", displayname);
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void savePrefix(String prefix) {
        File file = new File("plugins/Lunania/Npcs", name+".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set("prefix", prefix);
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}