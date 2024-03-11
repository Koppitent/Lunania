package de.koppy.land.api;

import java.util.HashMap;

import de.koppy.lunaniasystem.LunaniaSystem;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;

public class ChunkEditor {

    private Chunk chunk;

    public ChunkEditor(Chunk chunk) {
        this.chunk = chunk;
    }

    public ChunkEditor(LunaniaChunk chunk) {
        this.chunk = Bukkit.getWorld("world").getChunkAt(chunk.getX(), chunk.getZ());
    }

    public void setEcken(Material material) {
        int y = chunk.getWorld().getHighestBlockYAt(chunk.getBlock(0, 0, 0).getLocation())+1;
        if(y>=300) y = 0;
        chunk.getBlock(0, y, 0).setType(material);
        y = chunk.getWorld().getHighestBlockYAt(chunk.getBlock(0, 0, 15).getLocation())+1;
        if(y>=300) y = 0;
        chunk.getBlock(0, y, 15).setType(material);
        y = chunk.getWorld().getHighestBlockYAt(chunk.getBlock(15, 0, 0).getLocation())+1;
        if(y>=300) y = 0;
        chunk.getBlock(15, y, 0).setType(material);
        y = chunk.getWorld().getHighestBlockYAt(chunk.getBlock(15, 0, 15).getLocation())+1;
        if(y>=300) y = 0;
        chunk.getBlock(15, y, 15).setType(material);
    }

    public void setRand(Material material) {
        for(int x=0; x<16; x++) {
            for(int z=0; z<16; z++) {
                if(z == 15 || z == 0 || x == 0 || x == 15) {
                    int y = chunk.getWorld().getHighestBlockYAt(chunk.getBlock(x, 0, z).getLocation())+1;
                    if(y>=300) y = 0;
                    chunk.getBlock(x, y, z).setType(material);
                }
            }
        }
    }

    public void setEckenParticle(final Particle particle, final int height, final Player player, int times) {
        for(int i=0; i<times; i++) {
            int time = 0;
            time = time + 1;
            Bukkit.getScheduler().runTaskLater(LunaniaSystem.getPlugin(), new Runnable() {
                public void run() {
                    for(int yadd=0; yadd<height; yadd++) {
                        int y = chunk.getWorld().getHighestBlockYAt(chunk.getBlock(0, 0, 0).getLocation())+1+yadd;
                        if(y>=300) y = 0;
                        player.spawnParticle(particle, chunk.getBlock(0, y, 0).getLocation(), 1);
                        y = chunk.getWorld().getHighestBlockYAt(chunk.getBlock(0, 0, 15).getLocation())+1+yadd;
                        if(y>=300) y = 0;
                        player.spawnParticle(particle, chunk.getBlock(0, y, 15).getLocation(), 1);
                        y = chunk.getWorld().getHighestBlockYAt(chunk.getBlock(15, 0, 0).getLocation())+1+yadd;
                        if(y>=300) y = 0;
                        player.spawnParticle(particle, chunk.getBlock(15, y, 0).getLocation(), 1);
                        y = chunk.getWorld().getHighestBlockYAt(chunk.getBlock(15, 0, 15).getLocation())+1+yadd;
                        if(y>=300) y = 0;
                        player.spawnParticle(particle, chunk.getBlock(15, y, 15).getLocation(), 1);
                    }
                }
            }, time*20);
        }
    }

    public void setRandParticle(final Particle particle, final int height, final Player player, int times) {
        int time = 0;
        for(int i=0; i<times; i++) {
            time = time + 1;
            Bukkit.getScheduler().runTaskLater(LunaniaSystem.getPlugin(), new Runnable() {
                public void run() {
                    for(int yadd=0; yadd<height; yadd++) {
                        for(int x=0; x<16; x++) {
                            for(int z=0; z<16; z++) {
                                if(z == 15 || z == 0 || x == 0 || x == 15) {
                                    int y = chunk.getWorld().getHighestBlockYAt(chunk.getBlock(x, 0, z).getLocation())+1+yadd;
                                    if(y>=300) y = 0;
                                    Location loc = chunk.getBlock(x, y, z).getLocation();
                                    if(x == 15) loc = loc.add(1, 0, 0);
                                    if(z == 15) loc = loc.add(0, 0, 1);
                                    player.spawnParticle(particle, loc, 1);
                                }
                            }
                        }
                    }
                }
            }, time*10);
        }
    }

    public void showSlime(Player player) {
        int y = chunk.getWorld().getHighestBlockYAt(chunk.getBlock(7, 0, 7).getLocation())+50;
        final Slime slime = (Slime) chunk.getWorld().spawnEntity(chunk.getBlock(7, y, 7).getLocation(), EntityType.SLIME);
        slime.setAI(false);
        slime.setSize(10);
        slime.setVisibleByDefault(false);
        slime.setInvulnerable(true);
        slime.setGravity(false);
        slime.setGlowing(true);
        player.showEntity(LunaniaSystem.getPlugin(), slime);
        Bukkit.getScheduler().runTaskLater(LunaniaSystem.getPlugin(), new Runnable() {
            public void run() {
                slime.remove();
            }
        }, 20*10);
    }

    public HashMap<String, BlockData> copy() {
        HashMap<String, BlockData> chunkdatas = new HashMap<String, BlockData>();
        for(int x=0;x<16;x++) {
            for(int y=-63;y<319;y++) {
                for(int z=0;z<16;z++) {
                    chunkdatas.put(x+":"+y+":"+z, chunk.getBlock(x, y, z).getBlockData());
                }
            }
        }
        return chunkdatas;
    }

    public void paste(HashMap<String, BlockData> chunkdata) {
        for(int x=0;x<16;x++) {
            for(int y=-63;y<319;y++) {
                for(int z=0;z<16;z++) {
                    if(chunkdata.get(x+":"+y+":"+z) != null) {
                        chunk.getBlock(x, y, z).setBlockData(chunkdata.get(x+":"+y+":"+z));
                    }
                }
            }
        }
    }

    public void reset() {
        for(int x=0;x<16;x++) {
            for(int y=-63;y<319;y++) {
                for(int z=0;z<16;z++) {
                    chunk.getBlock(x, y, z).setBlockData(Bukkit.getWorld("worldCOPY").getChunkAt(chunk.getX(), chunk.getZ()).getBlock(x, y, z).getBlockData());
                }
            }
        }
    }

    public void clear() {
        for(int x=0;x<16;x++) {
            for(int y=-63;y<319;y++) {
                for(int z=0;z<16;z++) {
                    chunk.getBlock(x, y, z).setType(Material.AIR);
                }
            }
        }
    }

    public void moveHeight(int change) {
        HashMap<String, BlockData> savedata = copy();
        clear();
        for(int x=0;x<16;x++) {
            for(int y=-63;y<319;y++) {
                for(int z=0;z<16;z++) {
                    if(y+change < 319 && y+change > -64) {
                        BlockData data = savedata.get(x+":"+y+":"+z);
                        chunk.getBlock(x, y+change, z).setBlockData(data);
                    }
                }
            }
        }
    }


}