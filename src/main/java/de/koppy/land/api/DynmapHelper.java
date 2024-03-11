package de.koppy.land.api;

import org.bukkit.Bukkit;

import java.util.Random;

public class DynmapHelper {

    private DynmapHelper(){};

    public static int setDynmapArea(int x1, int z1, int x2, int z2, String name) {
        int id = new Random().nextInt(1000000000);
        sendCommand("dmarker addcorner " + x1 + " 10 " + z1 + " world");
        sendCommand("dmarker addcorner " + x2 + " 10 " + z2 + " world");
        sendCommand("dmarker addarea color:blue label:"+name+" id:"+id);
        return id;
    }

    public static int setDynmapArea(LunaniaChunk chunk, String name) {
       return setDynmapArea((int)chunk.getX()*16, (int)chunk.getZ()*16, ((int)chunk.getX()*16)+15, ((int)chunk.getZ()*16)+15, name);
    }

    private static void sendCommand(String cmd) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
    }

    public static void deleteDynmapAreaByID(int id) {
        sendCommand("/dynmap:dmarker deletearea id:"+id);
    }

}
