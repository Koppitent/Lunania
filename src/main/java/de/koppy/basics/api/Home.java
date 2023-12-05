package de.koppy.basics.api;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Home {

    private String name;
    private String server;
    private Location location;

    public Home(String name, String server, Location location) {
        this.name = name;
        this.server = server;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public String getServer() {
        return server;
    }

    @Override
    public String toString() {
        return HomeToString(this);
    }

    public static Home StringToHome(String homedata) {
        String[] homedataS = homedata.split(":");
        String name = homedataS[0];
        String server = homedataS[1];
        String world = homedataS[2];
        double x = Double.parseDouble(homedataS[3]);
        double y = Double.parseDouble(homedataS[4]);
        double z = Double.parseDouble(homedataS[5]);
        float yaw = Float.parseFloat(homedataS[6]);
        float pitch = Float.parseFloat(homedataS[7]);

        return new Home(name, server, new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch));
    }

    public static String HomeToString(Home home) {
        String name = home.getName();
        String server = home.getServer();
        String world = home.getLocation().getWorld().getName();
        int x = (int) home.getLocation().getX();
        int y = (int) home.getLocation().getY();
        int z = (int) home.getLocation().getZ();
        float yaw = home.getLocation().getYaw();
        float pitch = home.getLocation().getPitch();

        return name+":"+server+":"+world+":"+x+":"+y+":"+z+":"+yaw+":"+pitch;
    }

}