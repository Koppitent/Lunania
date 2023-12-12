package de.koppy.server;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class WorldManager {

    public void createWorld(String name, Player player) {
        player.sendMessage("World " + name + " starts loading...");
        WorldCreator wc = new WorldCreator(name);
        wc.createWorld();
        player.sendMessage("World " + name + " finished loading!");
    }

    public void cloneWorld(String name, String copyname) {
        if(Bukkit.getWorld(name) != null) {
            World world = Bukkit.getWorld(name);
            copyFileStructure(world.getWorldFolder(), new File(copyname));
        }
    }

    public void teleportWorld(String name, Player player) {
        if(Bukkit.getWorld(name) != null) {
            player.teleport(Bukkit.getWorld(name).getSpawnLocation());
            player.sendMessage("World teleport sucessfull.");
        }else {
            player.sendMessage("World not loaded.");
        }
    }

    public void copyFileStructure(File source, File target) {
        try {
            ArrayList<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.lock"));
            if (!ignore.contains(source.getName())) {
                if (source.isDirectory()) {
                    if (!target.exists())
                        if (!target.mkdirs())
                            throw new IOException("Couldn't create world directory!");
                    String files[] = source.list();
                    for (String file : files) {
                        File srcFile = new File(source, file);
                        File destFile = new File(target, file);
                        copyFileStructure(srcFile, destFile);
                    }
                } else {
                    InputStream in = new FileInputStream(source);
                    OutputStream out = new FileOutputStream(target);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0)
                        out.write(buffer, 0, length);
                    in.close();
                    out.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File getNextFile(String name, int i) {
        File targetdir = new File("/Mirror/Backups/");
        int amount = 1;
        if (targetdir.list() != null) {
            amount = targetdir.list().length + 1;
        }
        System.out.println(amount);
        File target = new File("/Mirror/Backups/" + name + amount);
        return target;
    }

    public void backupWorld(File source) {
        Bukkit.getConsoleSender().sendMessage("§7Starting backup of World §a" + source.getName() + "§7...");
        File target = getNextFile(source.getName(), 1);
        copyFileStructure(source, target);
    }

}