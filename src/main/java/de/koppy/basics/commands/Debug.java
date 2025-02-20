package de.koppy.basics.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.koppy.basics.api.PlayerProfile;
import de.koppy.land.api.Land;
import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.npc.api.Packet;
import de.koppy.server.WorldManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Debug implements CommandExecutor, TabCompleter {

    public static boolean consoledebug = false;

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player))
            return true;

        Player p = (Player) sender;
        if(p.hasPermission("server.admin.debug")) {
            if(args.length == 0) {

                Location loc = p.getLocation().add(0, 3, 0);
                Block block = loc.getBlock();
                block.setType(Material.OAK_SIGN);
                p.openSign((Sign) block.getState());
                return false;

            }else if(args.length == 1) {
                if(args[0].equalsIgnoreCase("backup")) {

                    File source = Bukkit.getWorld("world").getWorldFolder();
                    new WorldManager().backupWorld(source);
                    p.sendMessage("World copied.");

                }else if(args[0].equalsIgnoreCase("test")) {
                    new Packet(p).setViewdistance(32);
                    p.sendMessage("Viewdistance changed");
                }else {
                    new WorldManager().createWorld(args[0], p);
                }
                return false;
            }else if(args.length == 2) {
                if(args[0].equalsIgnoreCase("tpworld")) {
                    new WorldManager().teleportWorld(args[1], p);
                    return false;
                }else if(args[0].equalsIgnoreCase("scoreboard")) {
                    if(args[1].equalsIgnoreCase("hide")) {
                        PlayerProfile profile = PlayerProfile.getProfile(p.getUniqueId());
                        profile.getScoreboard().hideScoreboard();
                    }else if(args[1].equalsIgnoreCase("show")) {
                        PlayerProfile profile = PlayerProfile.getProfile(p.getUniqueId());
                        profile.getScoreboard().showScoreboard();
                    }else if(args[1].equalsIgnoreCase("update")) {
                        PlayerProfile profile = PlayerProfile.getProfile(p.getUniqueId());
                        profile.getScoreboard().updateLand(new Land(p.getLocation().getChunk()));
                    }
                }else if(args[0].equalsIgnoreCase("console")) {
                    if(args[1].equalsIgnoreCase("on")) {
                        consoledebug = true;
                    }else if(args[1].equalsIgnoreCase("off")) {
                        consoledebug = false;
                    }
                }else if(args[0].equalsIgnoreCase("usetp")) {
                    if(args[1].equalsIgnoreCase("on")) {
                        PlayerProfile.getProfile(p.getUniqueId()).setUseTP(true);
                    }else if(args[1].equalsIgnoreCase("off")) {
                        PlayerProfile.getProfile(p.getUniqueId()).setUseTP(false);
                    }
                }
                p.sendMessage("§5Debug: §dSet §a" + args[0] + " §dto §e" + args[1]);
            }
        }else {
            p.sendMessage("§4ERROR" + "§cOnly an Admin is allowed to use this Command.");
        }

        return false;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> tcomplete = new ArrayList<String>();
        if(sender instanceof Player) {
            if(((Player)sender).hasPermission("server.admin")) {
                if(args.length == 1) {
                    List<String> check = new ArrayList<String>();
                    check.add("console");
                    check.add("scoreboard");
                    check.add("backup");
                    check.add("usetp");
                    for(String s : check) if(s.startsWith(args[0])) tcomplete.add(s);
                }else if(args.length == 2) {
                    List<String> check = new ArrayList<String>();
                    if(args[0].equalsIgnoreCase("console") || args[0].equalsIgnoreCase("usetp")) {
                        check.add("on");
                        check.add("off");
                    }else if(args[0].equalsIgnoreCase("scoreboard")) {
                        check.add("hide");
                        check.add("show");
                    }
                    for(String s : check) if(s.startsWith(args[1])) tcomplete.add(s);
                }
            }
        }
        return tcomplete;
    }

}