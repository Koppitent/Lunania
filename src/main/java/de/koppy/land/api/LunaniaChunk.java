package de.koppy.land.api;

public class LunaniaChunk {

    private int x;
    private int z;
    private String world;

    public LunaniaChunk(int x, int z) {
        this.x = x;
        this.z = z;
        this.world = "world";
    }

    public LunaniaChunk(int x, int z, String world) {
        this.x = x;
        this.z = z;
        this.world = world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public String getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof LunaniaChunk)) return false;
        LunaniaChunk chunk = (LunaniaChunk) obj;
        return chunk.getX() == this.getX() && chunk.getZ() == this.getZ();
    }

}
