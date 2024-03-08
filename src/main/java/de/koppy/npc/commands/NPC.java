package de.koppy.npc.commands;

import java.util.ArrayList;
import java.util.List;

import de.koppy.npc.api.Npc;
import de.koppy.npc.api.NpcType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class NPC implements CommandExecutor, TabCompleter {

    public static String prefix = "§8[§cNPC§8] §r§7";

    public static ArrayList<Player> select = new ArrayList<Player>();
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return true;
        final Player p = (Player) sender;

        if(p.hasPermission("server.admin.npc")) {
            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("select")) {
                    p.sendMessage(prefix + "§7Hit an Npc to select it.");
                    select.add(p);
                }else if(args[0].equalsIgnoreCase("test")) {
                    Npc npc = Npc.selectednpc.get(p);
                    p.sendMessage("test executed." + npc.getPrefix());
                }else if(args[0].equalsIgnoreCase("getID")) {
                    Npc npc = Npc.selectednpc.get(p);
                    p.sendMessage("§7EntityID: " + npc.getEntityID());
                    p.sendMessage("§7ID: " + npc.getID());
                }else if(args[0].equalsIgnoreCase("list")) {
                    if(Npc.npcs.isEmpty()) {
                        p.sendMessage(prefix + "§7The list is empty.");
                        return false;
                    }
                    p.sendMessage(prefix + "§7List of npc's:");
                    for(Npc npc : Npc.npcs) {
                        p.sendMessage("§7"+npc.getName()+": "+npc.getDisplayname());
                    }
                }else if(args[0].equalsIgnoreCase("show")) {
                    Npc npc = Npc.selectednpc.get(p);
                    npc.show(p);
                    p.sendMessage(prefix + "§7The npc show to you now.");
                }else if(args[0].equalsIgnoreCase("lookatme")) {
                    Npc npc = Npc.selectednpc.get(p);
                    npc.look(p);
                    p.sendMessage(prefix + "§7The looks at you.");
                }else if(args[0].equalsIgnoreCase("look")) {
                    if(Npc.selectednpc.containsKey(p)) {
                        Npc npc = Npc.selectednpc.get(p);
                        npc.setLooking(!npc.isLooking());
                        p.sendMessage(prefix + "§7The npc is now looking towards you.");
                    }else {
                        p.sendMessage(prefix + "§cYou have no npc selected.");
                    }
                }else if(args[0].equalsIgnoreCase("delete")) {
                    if(Npc.selectednpc.containsKey(p)) {
                        Npc npc = Npc.selectednpc.get(p);
                        npc.delete();
                        p.sendMessage(prefix + "§7The npc got deleted.");
                    }else {
                        p.sendMessage(prefix + "§cYou have no npc selected.");
                    }
                }else if(args[0].equalsIgnoreCase("tp")) {
                    if(Npc.selectednpc.containsKey(p)) {
                        Npc npc = Npc.selectednpc.get(p);
                        p.teleport(npc.getLocation());
                        p.sendMessage(prefix + "§7You got teleported to the selected npc.");
                    }else {
                        p.sendMessage(prefix + "§cYou have no npc selected.");
                    }
                }else if(args[0].equalsIgnoreCase("move") || args[0].equalsIgnoreCase("teleporthere")) {
                    if(Npc.selectednpc.containsKey(p)) {
                        Npc npc = Npc.selectednpc.get(p);
                        npc.setLocation(p.getLocation());
                        p.sendMessage(prefix + "§7You teleported the npc to its new location.");
                    }else {
                        p.sendMessage(prefix + "§cYou have no npc selected.");
                    }
                }
            }else if(args.length == 2) {
                if(p.hasPermission("server.admin.npc")) {
                    if(args[0].equalsIgnoreCase("create")) {
                        String dname = args[1];
                        if(Npc.npclist.containsKey(dname) == false) {
                            final Npc npc = new Npc(dname, p.getLocation());
                            npc.show(p);
                            p.sendMessage(prefix + "§7You created the npc "+dname+".");
                        }else {
                            p.sendMessage(prefix + "§cThis npc already exists.");
                        }
                    }else if(args[0].equalsIgnoreCase("select")) {
                        String dname = args[1];
                        if(Npc.npclist.containsKey(dname)) {
                            Npc npc = Npc.npclist.get(dname);
                            Npc.selectednpc.put(p, npc);
                            p.sendMessage(prefix + "§7You selected the npc " + dname+".");
                        }else {
                            p.sendMessage(prefix + "§cThe npc doesn't exists.");
                        }
                    }else if(args[0].equalsIgnoreCase("setskin")) {
                        if(Npc.selectednpc.containsKey(p)) {
                            String dname = args[1];
                            Npc npc = Npc.selectednpc.get(p);
                            String[] name = Npc.getSkin(dname);
                            npc.setSkin(name[0], name[1]);
                            npc.showAll();
                            p.sendMessage(prefix + "§7You've set the §eskin §7of the npc.");
                        }else {
                            p.sendMessage(prefix + "§cYou have no npc selected.");
                        }
                    }else if(args[0].equalsIgnoreCase("settype")) {
                        if(Npc.selectednpc.containsKey(p)) {
                            Npc npc = Npc.selectednpc.get(p);
                            NpcType type = NpcType.fromString(args[1]);
                            npc.setType(type);
                            p.sendMessage(prefix + "§7You've set the type to §e" + type.toString()+"§7.");
                        }else {
                            p.sendMessage(prefix + "§cYou have no npc selected.");
                        }
                    }else if(args[0].equalsIgnoreCase("settypecontent")) {
                        if(Npc.selectednpc.containsKey(p)) {
                            Npc npc = Npc.selectednpc.get(p);
                            String content = args[1];
                            npc.setTypeContent(content);
                            p.sendMessage(prefix + "§7You've changed the content for the type of the npc.");
                        }else {
                            p.sendMessage(prefix + "§cYou have no npc selected.");
                        }
                    }else if(args[0].equalsIgnoreCase("setdisplayname")) {
                        if(Npc.selectednpc.containsKey(p)) {
                            Npc npc = Npc.selectednpc.get(p);
                            String displayname = args[1];
                            npc.setDisplayname(displayname);
                            p.sendMessage(prefix + "§7You have changed the displayname to " + displayname + "§7.");
                        }else {
                            p.sendMessage(prefix + "§cYou have no npc selected.");
                        }
                    }else if(args[0].equalsIgnoreCase("setprefix")) {
                        if(Npc.selectednpc.containsKey(p)) {
                            Npc npc = Npc.selectednpc.get(p);
                            String prefix = args[1];
                            npc.setPrefix(prefix);
                            p.sendMessage(NPC.prefix + "§7You have changed the prefix to " + prefix + "§7.");
                        }else {
                            p.sendMessage(prefix + "§cYou have no npc selected.");
                        }
                    }

                }
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> tcomplete = new ArrayList<String>();
        if(sender instanceof Player) {
            if(args.length == 1) {
                List<String> check = new ArrayList<String>();
                check.add("getID");
                check.add("select");
                check.add("list");
                check.add("show");
                check.add("look");
                check.add("lookatme");
                check.add("delete");
                check.add("tp");
                check.add("move");
                check.add("setskin");
                check.add("settype");
                check.add("settypecontent");
                check.add("setdisplayname");
                check.add("setprefix");
                for(String s : check) if(s.startsWith(args[0])) tcomplete.add(s);
            }
        }
        return tcomplete;
    }
}