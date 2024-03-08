package de.koppy.warp.api;

import java.util.UUID;

import de.koppy.mysql.api.Column;
import de.koppy.mysql.api.ColumnType;
import de.koppy.mysql.api.Table;
import de.koppy.warp.WarpSystem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

public class WarpManager {

    private static final Column namec = new Column("name", ColumnType.VARCHAR, 200);
    private static final Column locationc = new Column("location", ColumnType.VARCHAR, 200);
    private static final Column ownerc = new Column("owner", ColumnType.VARCHAR, 200);
    private static final Column messagec = new Column("message", ColumnType.VARCHAR, 200);
    private static final Column acceptedc = new Column("accepted", ColumnType.BOOL, 200);
    private static final Column serverc = new Column("server", ColumnType.VARCHAR, 200);
    private static final Table table = WarpSystem.getTable();

    private String name;
//	private Location location;
//	private String message;
//	private UUID owner;
//	private boolean accepted;

    public WarpManager(String name) {
        this.name = name;
    }

    public boolean existWarp() {
        return table.existEntry(namec, name);
    }

    public void createWarp(UUID uuid, String message, Location location, String server) {
        table.setValue(ownerc, uuid.toString(), namec, name);
        table.setValue(messagec, message, namec, name);
        table.setValue(acceptedc, false, namec, name);
        table.setValue(locationc, LocToString(location), namec, name);
        table.setValue(serverc, server, namec, name);
    }

    public Material getMaterial() {
        return Material.BOOK;
    }

    public void accept() {
        setAccepted(true);
    }

    public void changeOwner(UUID uuid) {
        setOwner(uuid);
        setAccepted(false);
    }

    public void changeMessage(String message) {
        setMessage(message);
        setAccepted(false);
    }

    public void changeLocation(Location location, String server) {
        setServer(server);
        setLocation(location);
        setAccepted(false);
    }

    public void delete() {
        table.delete(namec, name);
    }

    private void setAccepted(boolean bool) {
        table.setValue(acceptedc, bool, namec, name);
    }

    private void setOwner(UUID uuid) {
        table.setValue(ownerc, uuid.toString(), namec, name);
    }

    private void setServer(String server) {
        table.setValue(serverc, server, namec, name);
    }

    private void setMessage(String message) {
        table.setValue(messagec, message, namec, name);
    }

    private void setLocation(Location location) {
        table.setValue(locationc, LocToString(location), namec, name);
    }

    public boolean isAccepted() {
        return (Boolean) table.getValue(acceptedc, namec, name);
    }

    public String getMessage() {
        return (String) table.getValue(messagec, namec, name);
    }

    public Location getLocation() {
        return StringToLoc((String) table.getValue(locationc, namec, name));
    }

    public String getServer() {
        return (String) table.getValue(serverc, namec, name);
    }

    public UUID getOwner() {
        return UUID.fromString((String) table.getValue(ownerc, namec, name));
    }

    public boolean isOwner(UUID uuid) {
        return uuid.equals(getOwner());
    }

    private Location StringToLoc(String locstring) {
        String[] locstringS = locstring.split(":");
        double x = Double.valueOf(locstringS[0]);
        double y = Double.valueOf(locstringS[1]);
        double z = Double.valueOf(locstringS[2]);
        float yaw = Float.valueOf(locstringS[3]);
        float pitch = Float.valueOf(locstringS[4]);
        String world = locstringS[5];
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    private String LocToString(Location location) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        float yaw = location.getYaw();
        float pitch = location.getPitch();
        String world = location.getWorld().getName();
        return x+":"+y+":"+z+":"+yaw+":"+pitch+":"+world;
    }

}