package de.koppy.basics.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.koppy.basics.api.PlayerProfile;
import de.koppy.lunaniasystem.LunaniaSystem;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class Gamemode implements CommandExecutor, TabCompleter {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player))
            return false;

        Player player = (Player) sender;
        PlayerProfile profile = PlayerProfile.getProfile(player.getUniqueId());
        if(player.hasPermission("server.gamemode")) {
            if(args.length == 0) {

            }else if(args.length == 1) {
                if(args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("0")) {
                    player.setGameMode(GameMode.SURVIVAL);
                }else if(args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("1")) {
                    player.setGameMode(GameMode.CREATIVE);
                }else if(args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("2")) {
                    player.setGameMode(GameMode.ADVENTURE);
                }else if(args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("spec") || args[0].equalsIgnoreCase("3")) {
                    player.setGameMode(GameMode.SPECTATOR);
                }else {

                }
            }else if(args.length == 2) {
                if(args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("0")) {
                    if(args[1].equalsIgnoreCase("@a") || args[1].equalsIgnoreCase("@e")) {
                        player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + profile.getMessage("changedgamemodefor").replace("%player%", "all").replace("%gamemode%", GameMode.SURVIVAL.toString()));
                        for(Player t : Bukkit.getOnlinePlayers()) {
                            t.setGameMode(GameMode.SURVIVAL);
                        }
                    }else if(args[1].equalsIgnoreCase("@p") || args[1].equalsIgnoreCase("@s")) {
                        player.performCommand("gamemode survival");
                    }else if(args[1].equalsIgnoreCase("@r")) {
                        Random rndm = new Random();
                        int rndmnum = rndm.nextInt(Bukkit.getOnlinePlayers().size());
                        int i = 0;
                        for(Player target : Bukkit.getOnlinePlayers()) {
                            if(i == rndmnum) {
                                player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + profile.getMessage("changedgamemodefor").replace("%player%", target.getName()).replace("%gamemode%", GameMode.SURVIVAL.toString()));
                                target.setGameMode(GameMode.SURVIVAL);
                                return false;
                            }else {
                                i++;
                            }
                        }
                    }else {
                        if(Bukkit.getPlayer(args[1]) != null) {
                            Player target = Bukkit.getPlayer(args[1]);
                            player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + profile.getMessage("changedgamemodefor").replace("%player%", target.getName()).replace("%gamemode%", GameMode.SURVIVAL.toString()));
                            target.setGameMode(GameMode.SURVIVAL);
                        }else {
                            player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + profile.getMessage("playernotonline"));
                        }
                    }
                }else if(args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("1")) {
                    if(args[1].equalsIgnoreCase("@a") || args[1].equalsIgnoreCase("@e")) {
                        player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + profile.getMessage("changedgamemodefor").replace("%player%", "all").replace("%gamemode%", GameMode.CREATIVE.toString()));
                        for(Player t : Bukkit.getOnlinePlayers()) {
                            t.setGameMode(GameMode.CREATIVE);
                        }
                    }else if(args[1].equalsIgnoreCase("@p") || args[1].equalsIgnoreCase("@s")) {
                        player.performCommand("gamemode creative");
                    }else if(args[1].equalsIgnoreCase("@r")) {
                        Random rndm = new Random();
                        int rndmnum = rndm.nextInt(Bukkit.getOnlinePlayers().size());
                        int i = 0;
                        for(Player target : Bukkit.getOnlinePlayers()) {
                            if(i == rndmnum) {
                                player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + profile.getMessage("changedgamemodefor").replace("%player%", target.getName()).replace("%gamemode%", GameMode.CREATIVE.toString()));
                                target.setGameMode(GameMode.CREATIVE);
                                return false;
                            }else {
                                i++;
                            }
                        }
                    }else {
                        if(Bukkit.getPlayer(args[1]) != null) {
                            Player target = Bukkit.getPlayer(args[1]);
                            player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + profile.getMessage("changedgamemodefor").replace("%player%", target.getName()).replace("%gamemode%", GameMode.CREATIVE.toString()));
                            target.setGameMode(GameMode.CREATIVE);
                        }else {
                            player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + profile.getMessage("playernotonline"));
                        }
                    }
                }else if(args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("2")) {
                    if(args[1].equalsIgnoreCase("@a") || args[1].equalsIgnoreCase("@e")) {
                        player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + profile.getMessage("changedgamemodefor").replace("%player%", "all").replace("%gamemode%", GameMode.ADVENTURE.toString()));
                        for(Player t : Bukkit.getOnlinePlayers()) {
                            t.setGameMode(GameMode.ADVENTURE);
                        }
                    }else if(args[1].equalsIgnoreCase("@p") || args[1].equalsIgnoreCase("@s")) {
                        player.performCommand("gamemode ADVENTURE");
                    }else if(args[1].equalsIgnoreCase("@r")) {
                        Random rndm = new Random();
                        int rndmnum = rndm.nextInt(Bukkit.getOnlinePlayers().size());
                        int i = 0;
                        for(Player target : Bukkit.getOnlinePlayers()) {
                            if(i == rndmnum) {
                                player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + profile.getMessage("changedgamemodefor").replace("%player%", target.getName()).replace("%gamemode%", GameMode.ADVENTURE.toString()));
                                target.setGameMode(GameMode.ADVENTURE);
                                return false;
                            }else {
                                i++;
                            }
                        }
                    }else {
                        if(Bukkit.getPlayer(args[1]) != null) {
                            Player target = Bukkit.getPlayer(args[1]);
                            player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + profile.getMessage("changedgamemodefor").replace("%player%", target.getName()).replace("%gamemode%", GameMode.ADVENTURE.toString()));
                            target.setGameMode(GameMode.ADVENTURE);
                        }else {
                            player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + profile.getMessage("playernotonline"));
                        }
                    }
                }else if(args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("spec") || args[0].equalsIgnoreCase("3")) {
                    if(args[1].equalsIgnoreCase("@a") || args[1].equalsIgnoreCase("@e")) {
                        player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + profile.getMessage("changedgamemodefor").replace("%player%", "all").replace("%gamemode%", GameMode.SPECTATOR.toString()));
                        for(Player t : Bukkit.getOnlinePlayers()) {
                            t.setGameMode(GameMode.SPECTATOR);
                        }
                    }else if(args[1].equalsIgnoreCase("@p") || args[1].equalsIgnoreCase("@s")) {
                        player.performCommand("gamemode SPECTATOR");
                    }else if(args[1].equalsIgnoreCase("@r")) {
                        Random rndm = new Random();
                        int rndmnum = rndm.nextInt(Bukkit.getOnlinePlayers().size());
                        int i = 0;
                        for(Player target : Bukkit.getOnlinePlayers()) {
                            if(i == rndmnum) {
                                player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + profile.getMessage("changedgamemodefor").replace("%player%", target.getName()).replace("%gamemode%", GameMode.SPECTATOR.toString()));
                                target.setGameMode(GameMode.SPECTATOR);
                                return false;
                            }else {
                                i++;
                            }
                        }
                    }else {
                        if(Bukkit.getPlayer(args[1]) != null) {
                            Player target = Bukkit.getPlayer(args[1]);
                            player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + profile.getMessage("changedgamemodefor").replace("%player%", target.getName()).replace("%gamemode%", GameMode.SPECTATOR.toString()));
                            target.setGameMode(GameMode.SPECTATOR);
                        }else {
                            player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + profile.getMessage("playernotonline"));
                        }
                    }
                }
            }
        }

        return false;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> out = new ArrayList<String>();
        if(args.length == 1) {
            for(GameMode gamemodes : GameMode.values()) {
                if(gamemodes.toString().toLowerCase().startsWith(args[0].toLowerCase())) {
                    out.add(gamemodes.toString().toLowerCase());
                }
            }
        }else if(args.length == 2) {
            List<String> check = new ArrayList<String>();
            check.add("@a");
            check.add("@e");
            check.add("@p");
            check.add("@r");
            check.add("@s");
            for(Player players : Bukkit.getOnlinePlayers()) {
                check.add(players.getName());
            }
            for(String asd : check) if(asd.toLowerCase().startsWith(args[1].toLowerCase())) out.add(asd);
        }
        return out;
    }

}