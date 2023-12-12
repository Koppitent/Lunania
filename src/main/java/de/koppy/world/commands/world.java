package de.koppy.world.commands;

import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.server.WorldManager;
import de.koppy.world.api.World;
import org.bukkit.Bukkit;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class world implements CommandExecutor, TabCompleter {

    public static final String prefix = "§2World §8| §r";
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        Player player = (Player) sender;
        if(!player.hasPermission("server.admin.world")) {
            player.sendMessage(prefix + "§cKeine Rechte du Knecht.");
            return false;
        }

        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("list")) {
                File file = new File("plugins/Lunania", "worlds.yml");
                if(!file.exists()) {
                    player.sendMessage("§cKeine Welten vorhanden.");
                    return false;
                }
                FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
                if(cfg.getKeys(false).isEmpty()) {
                    player.sendMessage("§cKeine Welten vorhanden.");
                    return false;
                }
                player.sendMessage(prefix + "§7Liste aller Welten im Filesystem:");
                for(String worldname : cfg.getKeys(false)) {
                    player.sendMessage("§3" + worldname + ": §7x" + new World(worldname).getSpawnlocation().getX());
                }
            }
        }else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("info")) {
                String worldname = args[1];
                World world = new World(worldname);
                if(Bukkit.getWorld(worldname) != null) {
                    //* Info from loaded World
                    player.sendMessage("§7Status: §aloaded");
                }else {
                    player.sendMessage("§7Status: §cunloaded");
                }

                if(world.existWorld()) {
                    //* Info from File
                    player.sendMessage("§7File: §aexist");
                }else {
                    player.sendMessage("§7File: §cnot exist");
                }

            }else if(args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("teleport")) {
                String worldname = args[1];
                if(Bukkit.getWorld(worldname) == null) {
                    player.sendMessage(prefix + "§cDiese Welt existiert nicht.");
                    return false;
                }
                player.teleport(Bukkit.getWorld(worldname).getSpawnLocation());
                player.sendMessage(prefix + "§7Du wurdest zur Welt " + worldname + " teleportiert.");
            }else if(args[0].equalsIgnoreCase("create")) {
                String worldname = args[1];
                World world = new World(worldname);
                if(world.existWorld()) {
                    player.sendMessage(prefix + "§cDiese Welt existiert bereits.");
                    return false;
                }
                if(world.existFile()) {
                    player.sendMessage(prefix + "§cDiese Welt existiert bereits als File.");
                    player.sendMessage(prefix + "§7Wenn du sie importieren möchtest, dann verwende '/world import " + args[1] + "'.");
                    return false;
                }
                player.sendMessage(prefix + "§7Starte Erstellung von Welt " + worldname + "...");
                world.createWorld();
                player.sendMessage(prefix + "§7Welt §3" + worldname + " §7wurde §aerstellt§7!");
            }else if(args[0].equalsIgnoreCase("load")) {
                String worldname = args[1];
                World world = new World(worldname);
                if(!world.existWorld()) {
                    player.sendMessage(prefix + "§cDiese Welt existiert nicht in den Files.");
                    return false;
                }
                if(Bukkit.getWorld(worldname) != null) {
                    player.sendMessage(prefix + "§cDiese Welt ist bereits geladen.");
                    return false;
                }
                player.sendMessage(prefix + "§7Welt wird geladen...");
                world.loadWorld();
                player.sendMessage(prefix + "§7Welt " + worldname + " wurde §aerfolgreich §7geladen.");
            }else if(args[0].equalsIgnoreCase("unload")) {
                String worldname = args[1];
                World world = new World(worldname);
                if(Bukkit.getWorld(worldname) == null) {
                    player.sendMessage(prefix + "§cDiese Welt ist nicht geladen.");
                    return false;
                }
                world.unloadWorld();
                player.sendMessage(prefix + "§7Welt " + worldname + " wurde §aerfolgreich §7entladen.");
            }else if(args[0].equalsIgnoreCase("delete")) {
                String worldname = args[1];
                World world = new World(worldname);
                if(!world.existWorld()) {
                    player.sendMessage(prefix + "§cDiese Welt existiert nicht.");
                    return false;
                }
                world.deleteWorld();
                player.sendMessage(prefix + "§7Welt §3" + worldname + " §7wurde §cgelöscht§7!");
            }else if(args[0].equalsIgnoreCase("import")) {
                String worldname = args[1];
                World world = new World(worldname);
                if(world.existWorld()) {
                    player.sendMessage(prefix + "§cDiese Welt existiert bereits.");
                    return false;
                }
                if(!world.existFile()) {
                    player.sendMessage(prefix + "§cDiese Welt existiert so nicht. Hast du alles richtig geschrieben?");
                    return false;
                }
                player.sendMessage(prefix + "§7Starte Import von Welt " + worldname + "...");
                world.importWorld();
                player.sendMessage(prefix + "§7Welt §3" + worldname + " §7wurde §3importiert§7!");
            }else if(args[0].equalsIgnoreCase("deletefile")) {
                String worldname = args[1];
                File file = new File(worldname);
                if(worldname.equals("world")) return false;
                if(!file.exists()) {
                    player.sendMessage(prefix + "§cDieser Weltenordner existiert nicht!");
                    return false;
                }
                new World(worldname).deleteFile();
                player.sendMessage(prefix + "§7Weltenordner wurde erfolgreich gelöscht!");
            }
        }else if(args.length == 3) {
            if(args[0].equalsIgnoreCase("clone")) {
                String worldtocopy = args[1];
                String newworld = args[2];
                World wworldtocopy = new World(worldtocopy);
                File file = new File(newworld);
                if(file.exists()) {
                    player.sendMessage(prefix + "§cEs gibt bereits eine Welt mit diesem Namen im Ordner.");
                    return false;
                }
                if(!wworldtocopy.existFile()) {
                    player.sendMessage(prefix + "§cDie Welt die du kopieren möchtest existiert nicht als File.");
                }

                player.sendMessage(prefix + "§7Die Welt wird kopiert...");
                new WorldManager().copyFileStructure(new File(worldtocopy), file);
                player.sendMessage(prefix + "§7Die Welt wurde §aerfolgreich §7kopiert!");
            }else if(args[0].equalsIgnoreCase("create")) {
                WorldType wtype = WorldType.NORMAL;
                if(WorldType.getByName(args[2]) != null){
                    wtype = WorldType.getByName(args[2]);
                }
                String worldname = args[1];
                World world = new World(worldname);
                if(world.existWorld()) {
                    player.sendMessage(prefix + "§cDiese Welt existiert bereits.");
                    return false;
                }
                if(world.existFile()) {
                    player.sendMessage(prefix + "§cDiese Welt existiert bereits als File.");
                    player.sendMessage(prefix + "§7Wenn du sie importieren möchtest, dann verwende '/world import " + args[1] + "'.");
                    return false;
                }
                player.sendMessage(prefix + "§7Starte Erstellung von Welt " + worldname + "...");
                world.createWorld();
                player.sendMessage(prefix + "§7Welt §3" + worldname + " §7wurde §aerstellt§7!");
            }
        }else if(args.length == 4) {
            if(args[0].equalsIgnoreCase("create")) {
                org.bukkit.World.Environment env = org.bukkit.World.Environment.NORMAL;
                if(org.bukkit.World.Environment.valueOf(args[3]) != null) {
                    env = org.bukkit.World.Environment.valueOf(args[3]);
                }
                WorldType wtype = WorldType.NORMAL;
                if(WorldType.getByName(args[2]) != null){
                    wtype = WorldType.getByName(args[2]);
                }
                String worldname = args[1];
                World world = new World(worldname);
                if(world.existWorld()) {
                    player.sendMessage(prefix + "§cDiese Welt existiert bereits.");
                    return false;
                }
                if(world.existFile()) {
                    player.sendMessage(prefix + "§cDiese Welt existiert bereits als File.");
                    player.sendMessage(prefix + "§7Wenn du sie importieren möchtest, dann verwende '/world import " + args[1] + "'.");
                    return false;
                }
                player.sendMessage(prefix + "§7Starte Erstellung von Welt " + worldname + "...");
                world.createWorld();
                player.sendMessage(prefix + "§7Welt §3" + worldname + " §7wurde §aerstellt§7!");
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        List<String> out = new ArrayList<>();
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(!player.hasPermission("server.admin.world")) return out;
            if(args.length == 1) {
                out.add("list");
                out.add("clone");
                out.add("create");
                out.add("delete");
                out.add("load");
                out.add("unload");
                out.add("import");
                out.add("info");
                out.add("tp");
                out.add("deletefile");
            }else if(args.length == 2) {
                if(args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("delete")
                || args[0].equalsIgnoreCase("load") || args[0].equalsIgnoreCase("unload")
                || args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("info")) {
                    out = World.getAllWorlds();
                }
            }else if(args.length == 3) {
                if(args[0].equalsIgnoreCase("create")) {
                    out.add("[normal]");
                    out.add("[nether]");
                    out.add("[end]");
                }
            }else if(args.length == 4) {
                if(args[0].equalsIgnoreCase("create")) {
                    out.add("[normal]");
                    out.add("[flat]");
                    out.add("[amplified]");
                }
            }
        }
        return out;
    }
}
