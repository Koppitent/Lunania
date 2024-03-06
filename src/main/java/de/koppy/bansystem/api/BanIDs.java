package de.koppy.bansystem.api;

import org.bukkit.Material;

import java.util.HashMap;

public class BanIDs {

    public static HashMap<Integer, BanIDs> banids = new HashMap<>();

    private int id;
    private String reason;
    private long duration;
    private Material material;

    public BanIDs(int id, String reason, long duration, Material material) {
        this.id = id;
        this.reason = reason;
        this.duration = duration;
        this.material = material;
        banids.put(id, this);
    }

    public int getId() {
        return id;
    }

    public Material getMaterial() {
        return material;
    }

    public String getReason() {
        return reason;
    }

    public long getDuration() {
        return duration;
    }

    public String getTimeDuration() {
        long seconds = duration / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        String time = days + "days " + hours % 24 + "hours " + minutes % 60 + "minutes " + seconds % 60 + "seconds";
        if(days <= 0) {
            time = hours % 24 + "hours " + minutes % 60 + "minutes " + seconds % 60 + "seconds";
        }
        return time;
    }

    public static BanIDs getBanId(int id) {
        if(banids.containsKey(id)) {
            return banids.get(id);
        }else {
            return banids.get(99);
        }
    }

}