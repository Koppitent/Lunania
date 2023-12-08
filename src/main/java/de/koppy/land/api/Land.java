package de.koppy.land.api;

import org.bukkit.Chunk;

import java.util.List;
import java.util.UUID;

public class Land implements LandInterface{


    @Override
    public void unclaim() {

    }

    @Override
    public void setFlag(Flag flag, boolean bool) {

    }

    @Override
    public void setAlias(String alias) {

    }

    @Override
    public void addMember(UUID uuid) {

    }

    @Override
    public void removeMember(UUID uuid) {

    }

    @Override
    public void resetMember() {

    }

    @Override
    public void addBanned(UUID uuid) {

    }

    @Override
    public void removeBanned(UUID uuid) {

    }

    @Override
    public void resetBanned() {

    }

    @Override
    public void setOwner(UUID uuid) {

    }

    @Override
    public void claim(UUID uuid) {

    }

    @Override
    public void claimServer() {

    }

    @Override
    public boolean isClaimed() {
        return false;
    }

    @Override
    public boolean isOwner(UUID uuid) {
        return false;
    }

    @Override
    public boolean isMember(UUID uuid) {
        return false;
    }

    @Override
    public boolean isBanned(UUID uuid) {
        return false;
    }

    @Override
    public boolean getFlag(Flag flag) {
        return false;
    }

    @Override
    public List<String> getMemberUUIDs() {
        return null;
    }

    @Override
    public List<String> getBannedUUIDs() {
        return null;
    }

    @Override
    public String getOwnerUUID() {
        return null;
    }

    @Override
    public String getOwnerName() {
        return null;
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public Chunk getChunk() {
        return null;
    }
}
