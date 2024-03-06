package de.koppy.bansystem.api;

import org.bukkit.Material;

import java.util.HashMap;

public class MuteIDs {

    public static HashMap<Integer, MuteIDs> muteids = new HashMap<>();

    private int id;
    private String reason;
    private long duration;
    private Material material;

    public MuteIDs(int id, String reason, long duration, Material material) {
        this.id = id;
        this.reason = reason;
        this.duration = duration;
        this.material = material;
        muteids.put(id, this);
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

    public static MuteIDs getMuteId(int id) {
        if(muteids.containsKey(id)) {
            return muteids.get(id);
        }else {
            return muteids.get(99);
        }
    }

}