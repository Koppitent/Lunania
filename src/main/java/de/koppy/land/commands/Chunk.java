package de.lunania.land.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.koppy.basics.api.PlayerProfile;
import de.koppy.land.api.ChunkEditor;
import de.koppy.land.api.Direction;
import de.koppy.lunaniasystem.LunaniaSystem;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class Chunk implements CommandExecutor, TabCompleter {

    public static HashMap<Player, ArrayList<org.bukkit.Chunk>> chunkselection = new HashMap<Player, ArrayList<org.bukkit.Chunk>>();
    public static HashMap<Player, HashMap<String, BlockData>> playerclipboard = new HashMap<Player, HashMap<String,BlockData>>();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player))
            return false;
        Player player = (Player) sender;

        if(player.hasPermission("server.chunkedit")) {
            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("reset")) {
                    ChunkEditor ce = new ChunkEditor(player.getLocation().getChunk());
                    ce.reset();
                    PlayerProfile.getProfile(player.getUniqueId()).sendMessage("chunkresettet");
                }else if(args[0].equalsIgnoreCase("direction")) {
                    player.sendMessage("�7Direction: �e"+getDirectionNESW(player).toString());
                }else if(args[0].equalsIgnoreCase("select")) {
                    ArrayList<org.bukkit.Chunk> chunks = new ArrayList<org.bukkit.Chunk>();
                    chunks.add(player.getLocation().getChunk());
                    chunkselection.put(player, chunks);
                    PlayerProfile.getProfile(player.getUniqueId()).sendMessage("chunkselected");
                }else if(args[0].equalsIgnoreCase("copy")) {
                    ChunkEditor ce = new ChunkEditor(player.getLocation().getChunk());
                    playerclipboard.put(player, ce.copy());
                    PlayerProfile.getProfile(player.getUniqueId()).sendMessage("chunkcopied");
                }else if(args[0].equalsIgnoreCase("paste")) {
                    if(chunkselection.containsKey(player)) {
                        if(playerclipboard.containsKey(player)) {
                            HashMap<String, BlockData> chunkdata = playerclipboard.get(player);
                            for(org.bukkit.Chunk chunk : chunkselection.get(player)) {
                                ChunkEditor ce = new ChunkEditor(chunk);
                                ce.paste(chunkdata);
                            }
                            PlayerProfile.getProfile(player.getUniqueId()).sendMessage("chunkspasted");
                        }else {
                            PlayerProfile.getProfile(player.getUniqueId()).sendMessage("nothinginclipboard");
                        }
                    }else {
                        PlayerProfile.getProfile(player.getUniqueId()).sendMessage("nochunkselected");
                    }
                }
            }else if(args.length == 2) {
                if(args[0].equalsIgnoreCase("select")) {
                    String range = args[1];
                    if(range.contains("x")) {
                        if(range.split("x")[0].matches("[0-9]+") && range.split("x")[1].matches("[0-9]+")) {
                            ArrayList<org.bukkit.Chunk> chunks = new ArrayList<org.bukkit.Chunk>();
                            int xi = Integer.valueOf(range.split("x")[0]);
                            int zi = Integer.valueOf(range.split("x")[1]);
                            Direction direction = getDirectionNESW(player);
                            int x = player.getLocation().getChunk().getX();
                            int z = player.getLocation().getChunk().getZ();
                            if(direction == Direction.NORTH) {
                                //z -
                                //x +
                                for(int xs=0;xs<xi;xs++) {
                                    for(int zs=0;zs<zi;zs++) {
                                        org.bukkit.Chunk chunk = player.getLocation().getWorld().getChunkAt(x+xs, z-zs);
                                        chunks.add(chunk);
                                    }
                                }
                            }else if(direction == Direction.SOUTH) {
                                //z +
                                //x -
                                for(int xs=0;xs<xi;xs++) {
                                    for(int zs=0;zs<zi;zs++) {
                                        org.bukkit.Chunk chunk = player.getLocation().getWorld().getChunkAt(x-xs, z+zs);
                                        chunks.add(chunk);
                                    }
                                }
                            }else if(direction == Direction.WEST) {
                                //z -
                                //x -
                                for(int xs=0;xs<xi;xs++) {
                                    for(int zs=0;zs<zi;zs++) {
                                        org.bukkit.Chunk chunk = player.getLocation().getWorld().getChunkAt(x-xs, z-zs);
                                        chunks.add(chunk);
                                    }
                                }
                            }else if(direction == Direction.EAST) {
                                //z +
                                //x +
                                for(int xs=0;xs<xi;xs++) {
                                    for(int zs=0;zs<zi;zs++) {
                                        org.bukkit.Chunk chunk = player.getLocation().getWorld().getChunkAt(x+xs, z+zs);
                                        chunks.add(chunk);
                                    }
                                }
                            }
                            chunkselection.put(player, chunks);
                            player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + PlayerProfile.getProfile(player.getUniqueId()).getMessage("chunksselected").replace("%amount%", ""+(xi*zi)));
                        }else {
                            PlayerProfile.getProfile(player.getUniqueId()).sendMessage("chunkselectwrongformat");
                        }
                    }else {
                        PlayerProfile.getProfile(player.getUniqueId()).sendMessage("chunkselectwrongformat");
                    }
                }else if(args[0].equalsIgnoreCase("up")) {
                    if(args[1].matches("[0-9]+")) {
                        int height = Integer.valueOf(args[1]);
                        if(chunkselection.containsKey(player)) {
                            for(org.bukkit.Chunk chunk : chunkselection.get(player)) {
                                ChunkEditor ce = new ChunkEditor(chunk);
                                ce.moveHeight(height);
                            }
                            PlayerProfile.getProfile(player.getUniqueId()).sendMessage("chunksmovedup");
                        }else {
                            PlayerProfile.getProfile(player.getUniqueId()).sendMessage("nochunkselected");
                        }
                    }
                }else if(args[0].equalsIgnoreCase("down")) {
                    if(args[1].matches("[0-9]+")) {
                        int height = Integer.valueOf(args[1]);
                        if(chunkselection.containsKey(player)) {
                            for(org.bukkit.Chunk chunk : chunkselection.get(player)) {
                                ChunkEditor ce = new ChunkEditor(chunk);
                                ce.moveHeight(-height);
                            }
                            PlayerProfile.getProfile(player.getUniqueId()).sendMessage("chunksmoveddown");
                        }else {
                            PlayerProfile.getProfile(player.getUniqueId()).sendMessage("nochunkselected");
                        }
                    }
                }
            }
        }
        return false;
    }

    private Direction getDirectionNESW(Player player) {
        int degrees = (Math.round(player.getLocation().getYaw()) + 270) % 360;
        if(degrees <= 45) return Direction.WEST;
        if(degrees <= 135) return Direction.NORTH;
        if(degrees <= 225) return Direction.EAST;
        if(degrees <= 315) return Direction.SOUTH;
        if(degrees <= 360) return Direction.WEST;
        return null;
    }

    @SuppressWarnings("unused")
    private Direction getDirection(Player player) {
        int degrees = (Math.round(player.getLocation().getYaw()) + 270) % 360;
        if (degrees <= 22) return Direction.NORTH;
        if (degrees <= 67) return Direction.NORTHEAST;
        if (degrees <= 112) return Direction.EAST;
        if (degrees <= 157) return Direction.SOUTHEAST;
        if (degrees <= 202) return Direction.SOUTH;
        if (degrees <= 247) return Direction.SOUTHWEST;
        if (degrees <= 292) return Direction.WEST;
        if (degrees <= 337) return Direction.NORTHWEST;
        if (degrees <= 359) return Direction.NORTH;
        return null;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> tcomplete = new ArrayList<String>();
        if(sender instanceof Player) {
            if(args.length == 1) {
                List<String> check = new ArrayList<String>();
                check.add("reset");
                check.add("select");
                check.add("up");
                check.add("down");
                for(String s : check) if(s.startsWith(args[0])) tcomplete.add(s);
            }
        }
        return tcomplete;
    }
}