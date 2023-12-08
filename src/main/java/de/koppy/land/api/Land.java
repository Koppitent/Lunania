package de.koppy.land.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.koppy.land.api.Flag;
import de.koppy.land.api.LandInterface;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Land implements LandInterface {

    private Chunk chunk;
    private File file;

    public Land(Chunk chunk) {
        this.chunk = chunk;
        this.file = new File("plugins/Lunania/Lands", chunk.getX()+"I"+chunk.getZ()+".yml");
    }

    public Land(File file) {
        this.file = file;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public boolean isClaimed() {
        return file.exists();
    }

    public void unclaim() {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        String uuidold = cfg.getString("owner");
        File file2 = new File("plugins/Lunania/Landuser", uuidold+".yml");
        FileConfiguration cfg2 = YamlConfiguration.loadConfiguration(file2);
        cfg2.set(chunk.getX()+"I"+chunk.getZ(), null);
        if(file.exists()) file.delete();
    }

    public void setBanall(boolean bool) {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set("banall", bool);
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean getBanall() {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        return cfg.getBoolean("banall");
    }

    public void setFlag(Flag flag, boolean bool) {
        if(file.exists()) {
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            if(flag == Flag.PVE) {
                cfg.set("flagpve", bool);
            }else if(flag == Flag.PVP) {
                cfg.set("flagpvp", bool);
            }if(flag == Flag.TNT) {
                cfg.set("flagtnt", bool);
            }
            try {
                cfg.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean hasAlias() {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        if(cfg.contains("alias")) {
            return cfg.getString("alias") != "";
        }
        return false;
    }

    public boolean isAlias(String alias) {
        if(hasAlias()) {
            return getAlias().equals(alias);
        }
        return false;
    }

    public String getAlias() {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        return cfg.getString("alias");
    }

    public boolean getFlag(Flag flag) {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        if(flag == Flag.PVE) {
            return cfg.getBoolean("flagpve");
        }else if(flag == Flag.PVP) {
            return cfg.getBoolean("flagpvp");
        }if(flag == Flag.TNT) {
            return cfg.getBoolean("flagtnt");
        }
        return false;
    }

    public void setAlias(String alias) {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set("alias", alias);
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMember(UUID uuid) {
        List<String> memberuuids = getMemberUUIDs();
        if(memberuuids.contains(uuid.toString())) return;
        memberuuids.add(uuid.toString());
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set("member", memberuuids);
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeMember(UUID uuid) {
        List<String> memberuuids = getMemberUUIDs();
        if(memberuuids.contains(uuid.toString()) == false) return;
        memberuuids.remove(uuid.toString());
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set("member", memberuuids);
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resetMember() {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set("member", new ArrayList<String>());
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addBanned(UUID uuid) {
        List<String> banneduuids = getBannedUUIDs();
        if(banneduuids.contains(uuid.toString())) return;
        banneduuids.add(uuid.toString());
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set("banned", banneduuids);
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeBanned(UUID uuid) {
        List<String> banneduuids = getBannedUUIDs();
        if(banneduuids.contains(uuid.toString()) == false) return;
        banneduuids.remove(uuid.toString());
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set("banned", banneduuids);
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resetBanned() {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set("banned", new ArrayList<String>());
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getMemberUUIDs() {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        return cfg.getStringList("member");
    }

    public List<String> getBannedUUIDs() {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        return cfg.getStringList("banned");
    }

    public String getOwnerUUID() {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        return cfg.getString("owner");
    }

    public String getOwnerName() {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        String owneruuid = cfg.getString("owner");
        if(owneruuid.equals("Server")) {
            return "Server";
        }else {
            return Bukkit.getOfflinePlayer(UUID.fromString(owneruuid)).getName();
        }
    }

    public void claim(UUID uuid) {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        if(file.exists() == false) {
            File file2 = new File("plugins/Lunania/Landuser", uuid.toString()+".yml");
            FileConfiguration cfg2 = YamlConfiguration.loadConfiguration(file2);
            cfg2.set(chunk.getX()+"I"+chunk.getZ(), "owned");

            cfg.set("owner", uuid.toString());
            cfg.set("member", new ArrayList<String>());
            cfg.set("banned", new ArrayList<String>());
            cfg.set("flagpvp", false);
            cfg.set("flagtnt", false);
            cfg.set("flagpve", false);
            cfg.set("banall", false);
            try {
                cfg.save(file);
                cfg2.save(file2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void claimServer() {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        File file2 = new File("plugins/Lunania/Landuser", "Server"+".yml");
        FileConfiguration cfg2 = YamlConfiguration.loadConfiguration(file2);
        cfg2.set(chunk.getX()+"I"+chunk.getZ(), "owned");

        cfg.set("owner", "Server");
        cfg.set("member", new ArrayList<String>());
        cfg.set("banned", new ArrayList<String>());
        cfg.set("flagpvp", false);
        cfg.set("flagtnt", false);
        cfg.set("flagpve", false);
        cfg.set("banall", false);
        try {
            cfg.save(file);
            cfg2.save(file2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isOwner(UUID uuid) {
        if(file.exists() == false) return false;
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        return cfg.getString("owner").equals(uuid.toString());
    }

    public boolean isMember(UUID uuid) {
        if(file.exists() == false) return false;
        return getMemberUUIDs().contains(uuid.toString());
    }

    public boolean isBanned(UUID uuid) {
        if(file.exists() == false) return false;
        if(getOwnerUUID().equals(uuid.toString())) return false;
        if(getMemberUUIDs().contains(uuid.toString())) return false;
        if(getBanall()) return true;
        return getBannedUUIDs().contains(uuid.toString());
    }

    public void setOwner(UUID uuid) {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        String uuidold = cfg.getString("owner");
        File file2 = new File("plugins/Lunania/Landuser", uuidold+".yml");
        FileConfiguration cfg2 = YamlConfiguration.loadConfiguration(file2);
        cfg2.set(chunk.getX()+"I"+chunk.getZ(), null);

        File file3 = new File("plugins/Lunania/Landuser", uuid.toString()+".yml");
        FileConfiguration cfg3 = YamlConfiguration.loadConfiguration(file3);
        cfg3.set(chunk.getX()+"I"+chunk.getZ(), "owner");

        cfg.set("owner", uuid.toString());
        try {
            cfg.save(file);
            cfg2.save(file2);
            cfg3.save(file3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean existAlias(String alias) {
        File file = new File("plugins/Lunania/Lands");
        for(File f : file.listFiles()) {
            Land land = new Land(f);
            if(land.isAlias(alias)) {
                return true;
            }
        }
        return false;
    }

}