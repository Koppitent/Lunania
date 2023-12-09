package de.koppy.land.api;

import de.koppy.basics.api.PlayerProfile;
import de.koppy.land.LandSystem;
import de.koppy.mysql.api.Column;
import de.koppy.mysql.api.ColumnType;
import de.koppy.mysql.api.Table;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Land implements LandInterface {

    private static final Column landc = new Column("lands",ColumnType.VARCHAR, 200);
    private static final Column memberc = new Column("member", ColumnType.TEXT, 10000);
    private static final Column bannedc = new Column("banned", ColumnType.TEXT, 10000);
    private static final Column ownerc = new Column("owner", ColumnType.VARCHAR, 200);
    private static final Table table = LandSystem.getTable();

    private Chunk chunk;

    public Land(Chunk chunk) {
        this.chunk = chunk;
    }

    @Override
    public void claim(UUID uuid) {
        if(isClaimed()) return;
        PlayerProfile.addLand(chunk, uuid.toString());
        table.setValue(ownerc, uuid.toString(), landc, getChunkString());
        table.setValue(memberc, ListToString(new ArrayList<>()), landc, getChunkString());
        table.setValue(bannedc, ListToString(new ArrayList<>()), landc, getChunkString());
        for(Flag flag : Flag.values()) { table.setValue(new Column(flag.toString().toLowerCase(), ColumnType.BOOL, 200), flag.getDefault(), landc, getChunkString()); }
    }

    @Override
    public void unclaim() {
        PlayerProfile.removeLand(chunk, getOwnerUUID());
        table.delete(landc, getChunkString());
    }

    @Override
    public void claimServer() {
        if(isClaimed()) return;
        PlayerProfile.addLand(chunk, "Server");
        table.setValue(ownerc, "Server", landc, getChunkString());
        table.setValue(memberc, ListToString(new ArrayList<>()), landc, getChunkString());
        table.setValue(bannedc, ListToString(new ArrayList<>()), landc, getChunkString());
        for(Flag flag : Flag.values()) { table.setValue(new Column(flag.toString().toLowerCase(), ColumnType.BOOL, 200), flag.getDefault(), landc, getChunkString()); }
    }

    @Override
    public boolean isClaimed() {
        return table.existEntry(landc, landc, getChunkString());
    }

    @Override
    public void setFlag(Flag flag, boolean bool) {
        if(!isClaimed()) return;
        table.setValue(new Column(flag.toString().toLowerCase(), ColumnType.BOOL, 200), bool, landc, getChunkString());
    }

    @Override
    public void addMember(UUID uuid) {
        addMember(uuid.toString());
    }

    @Override
    public void removeMember(UUID uuid) {
        removeMember(uuid.toString());
    }

    @Override
    public void resetMember() {
        if(!isClaimed()) return;
        table.setValue(memberc, ListToString(new ArrayList<>()), landc, getChunkString());
    }

    @Override
    public void addBanned(UUID uuid) {
        addBanned(uuid.toString());
    }

    @Override
    public void removeBanned(UUID uuid) {
        removeBanned(uuid.toString());
    }

    @Override
    public void resetBanned() {
        if(!isClaimed()) return;
        table.setValue(bannedc, ListToString(new ArrayList<>()), landc, getChunkString());
    }

    @Override
    public void setOwner(UUID uuid) {
        if(!isClaimed()) return;
        String olduuid = getOwnerUUID();
        PlayerProfile.removeLand(chunk, olduuid);
        PlayerProfile.addLand(chunk, uuid.toString());
        table.setValue(ownerc, uuid.toString(), landc, getChunkString());
    }

    @Override
    public boolean isOwner(UUID uuid) {
        if(!isClaimed()) return false;
        return getOwnerUUID().equalsIgnoreCase(uuid.toString());
    }

    @Override
    public boolean isMember(UUID uuid) {
        if(!isClaimed()) return false;
        return getMemberUUIDs().contains(uuid.toString());
    }

    @Override
    public boolean isBanned(UUID uuid) {
        if(!isClaimed()) return false;
        return getBannedUUIDs().contains(uuid.toString());
    }

    @Override
    public boolean getFlag(Flag flag) {
        if(!isClaimed()) return flag.getDefault();
        return (boolean) table.getValue(new Column(flag.toString().toLowerCase(), ColumnType.BOOL, 200), landc, getChunkString());
    }

    @Override
    public List<String> getMemberUUIDs() {
        if(!isClaimed()) return null;
        return StringToList((String) table.getValue(memberc, landc, getChunkString()));
    }

    @Override
    public List<String> getBannedUUIDs() {
        if(!isClaimed()) return null;
        return StringToList((String) table.getValue(bannedc, landc, getChunkString()));
    }

    @Override
    public String getOwnerUUID() {
        if(!isClaimed()) return null;
        return (String) table.getValue(ownerc, landc, getChunkString());
    }

    @Override
    public String getOwnerName() {
        if(!isClaimed()) return null;
        String trans = getOwnerUUID();
        if(trans.equalsIgnoreCase("Server")) {
            return trans;
        }else {
            return Bukkit.getOfflinePlayer(UUID.fromString(trans)).getName();
        }
    }

    @Override
    public Chunk getChunk() {
        return chunk;
    }

    private String getChunkString() {
        return chunk.getX()+"I"+chunk.getZ();
    }

    private String ListToString(List<String> list) {
        String out = "";
        if(list.isEmpty()) return out;
        for(String o : list) {
            out = out + o + ":";
        }
        out = out.substring(0, out.length()-1);
        return out;
    }

    private List<String> StringToList(String rawstring) {
        List<String> out = new ArrayList<>();
        if(rawstring == null) return out;
        if(rawstring.isEmpty()) return out;
        if(!rawstring.contains(":")) {
            out.add(rawstring);
            return out;
        }
        for(String o : rawstring.split(":")) {
            out.add(o);
        }
        return out;
    }

    public boolean getBanall() {
        return getBannedUUIDs().contains("*");
    }

    public void setBanall(boolean b) {
        if(!isClaimed()) return;
        if(b) addBanned("*");
        else removeBanned("*");
    }

    private void addBanned(String s) {
        if(!isClaimed()) return;
        List<String> bannedlist = getBannedUUIDs();
        if(bannedlist.contains(s)) return;
        bannedlist.add(s);
        table.setValue(bannedc, ListToString(bannedlist), landc, getChunkString());
    }

    private void removeBanned(String s) {
        if(!isClaimed()) return;
        List<String> bannedlist = getBannedUUIDs();
        bannedlist.remove(s);
        table.setValue(bannedc, ListToString(bannedlist), landc, getChunkString());
    }

    private void addMember(String s) {
        if(!isClaimed()) return;
        List<String> memberlist = getMemberUUIDs();
        if(memberlist.contains(s)) return;
        memberlist.add(s);
        table.setValue(memberc, ListToString(memberlist), landc, getChunkString());
    }

    private void removeMember(String s) {
        if(!isClaimed()) return;
        List<String> memberlist = getMemberUUIDs();
        memberlist.remove(s);
        table.setValue(memberc, ListToString(memberlist), landc, getChunkString());
    }
}
