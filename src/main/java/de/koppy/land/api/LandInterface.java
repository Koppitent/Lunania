package de.koppy.land.api;

import java.util.List;
import java.util.UUID;

import de.koppy.land.api.Flag;
import org.bukkit.Chunk;

public interface LandInterface {

    void unclaim();
    void setFlag(Flag flag, boolean bool);
    void setAlias(String alias);
    void addMember(UUID uuid);
    void removeMember(UUID uuid);
    void resetMember();
    void addBanned(UUID uuid);
    void removeBanned(UUID uuid);
    void resetBanned();
    void setOwner(UUID uuid);
    void claim(UUID uuid);
    void claimServer();
    boolean isClaimed();
    boolean isOwner(UUID uuid);
    boolean isMember(UUID uuid);
    boolean isBanned(UUID uuid);
    boolean getFlag(Flag flag);
    List<String> getMemberUUIDs();
    List<String> getBannedUUIDs();
    String getOwnerUUID();
    String getOwnerName();
    String getAlias();
    Chunk getChunk();

}