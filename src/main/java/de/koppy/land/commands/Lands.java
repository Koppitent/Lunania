package de.koppy.land.commands;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import de.koppy.basics.api.PlayerProfile;
import de.koppy.economy.EconomySystem;
import de.koppy.economy.api.PlayerAccount;
import de.koppy.land.api.*;
import de.koppy.land.listener.InventoryEvents;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class Lands implements CommandExecutor, TabCompleter {

    public static double PRICE_PER_LAND = 10000d;
    public static ArrayList<LunaniaChunk> sellconfirmation = new ArrayList<LunaniaChunk>();
    public static HashMap<Player, ArrayList<LunaniaChunk>> chunkselected = new HashMap<Player, ArrayList<LunaniaChunk>>();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player))
            return false;
        Player player = (Player) sender;
        PlayerProfile profile = PlayerProfile.getProfile(player.getUniqueId());

        if(player.getLocation().getWorld().getName().equals("world")) {
            if(args.length == 0) {
                Land land = new Land((int) player.getLocation().getX()/16, (int) player.getLocation().getZ()/16);
                LandUI landui = new LandUI(player.getUniqueId(), land.getChunk());
                landui.getMainPage();
                player.openInventory(landui.getInventory());
            }else if(args.length == 1) {
                if(args[0].equalsIgnoreCase("test")) {
                    player.sendMessage("Maxlands: "+profile.getMaxLands());
                }else if(args[0].equalsIgnoreCase("list")) {
                    player.sendMessage(profile.getLands().size()+"");
                    for(String s : profile.getLands()) {
                        player.sendMessage("§7> §3x" + s.split("I")[0] + " z" + s.split("I")[1]);
                    }
                }else if(args[0].equalsIgnoreCase("delete")) {

                    Land land = new Land(player.getLocation().getChunk());
                    if(!player.hasPermission("server.admin.land")) { profile.sendMessage("noperms"); return false; }
                    if(!land.isClaimed()) { profile.sendMessage("notclaimed"); return false; }

                    land.unclaim();
                    profile.sendMessage("landdeleted");
                    new ChunkEditor(land.getChunk()).setRandParticle(Particle.ANGRY_VILLAGER, 7, player, 5);

                }else if(args[0].equalsIgnoreCase("sell")) {

                    Land land = new Land(player.getLocation().getChunk());
                    if(!land.isClaimed()) { profile.sendMessage("notclaimed"); return false; }
                    if(!land.isOwner(player.getUniqueId())) { profile.sendMessage("mustbeowner"); return false;}
                    if(!sellconfirmation.contains(land.getChunk())) { profile.sendMessage("confirmsell"); sellconfirmation.add(land.getChunk()); player.sendMessage("§7The value of ur land is " + new DecimalFormat("#").format((PRICE_PER_LAND * 0.1)) + EconomySystem.getEcosymbol() + "§7."); return false; }

                    sellconfirmation.remove(land.getChunk());
                    land.unclaim();
                    PlayerAccount pa = new PlayerAccount(player.getUniqueId());
                    pa.addMoney((PRICE_PER_LAND * 0.1), "from Server", "Land "+land.getChunk().getX()+":"+land.getChunk().getZ()+" sold");
                    player.sendMessage("§7You sold ur land for " + new DecimalFormat("#").format((PRICE_PER_LAND * 0.1)) + EconomySystem.getEcosymbol() + "§7.");
                    PlayerProfile.getProfile(player.getUniqueId()).getScoreboard().updateLand(land);
                    new ChunkEditor(land.getChunk()).setRandParticle(Particle.ANGRY_VILLAGER, 7, player, 5);
                    new ChunkEditor(land.getChunk()).reset();

                }else if(args[0].equalsIgnoreCase("claim")) {

                    Land land = new Land(player.getLocation().getChunk());
                    if(land.isClaimed()) { profile.sendMessage("alreadyclaimed"); return false; }
                    if(profile.getLands().size() >= profile.getMaxLands() && player.hasPermission("server.admin.land.bypass") == false) { profile.sendMessage("maxlandsreached"); return false; }
                    PlayerAccount pa = new PlayerAccount(player.getUniqueId());
                    double bal = pa.getMoney();
                    double amount = bal;
                    if(amount < PRICE_PER_LAND) { player.sendMessage(profile.getMessage("notenoughmoney").replace("%missing%", new DecimalFormat("#").format((PRICE_PER_LAND -amount)))); return false; }

//				new ChunkEditor(player.getLocation().getChunk()).setEcken(Material.TORCH);
                    new ChunkEditor(player.getLocation().getChunk()).setRandParticle(Particle.ANGRY_VILLAGER, 7, player, 5);
                    new ChunkEditor(player.getLocation().getChunk()).showSlime(player);
                    pa.removeMoney(PRICE_PER_LAND, "sendto Server", "bought Land "+land.getChunk().getX()+":"+land.getChunk().getZ());
                    land.claim(player.getUniqueId());
                    profile.sendMessage("landclaimed");
                    profile.getScoreboard().updateLand(land);

                }else if(args[0].equalsIgnoreCase("claimselected")) {

                    if(!chunkselected.containsKey(player)) { profile.sendMessage("nochunksselected"); return false; }
                    ArrayList<LunaniaChunk> chunks = chunkselected.get(player);
                    if(!isUnclaimed(chunks)) { profile.sendMessage("somechunksclaimed"); return false; }
                    if(profile.getLands().size()+chunks.size()-1 >= profile.getMaxLands() && player.hasPermission("server.admin.land.bypass") == false) { profile.sendMessage("maxlandsreached"); return false; }
                    PlayerAccount pa = new PlayerAccount(player.getUniqueId());
                    double bal = pa.getMoney();
                    double amount = bal;
                    double price = countPrice(chunks);
                    if(amount < price) { profile.getMessage("notenoughmoney").replace("%missing%", new DecimalFormat("#").format((price-amount))); return false; }

                    pa.removeMoney(price, "sendto Server", "bought Lands");
                    claimAll(chunks, player);
                    profile.sendMessage("chunksclaimed");
                    profile.getScoreboard().updateLand(new Land(player.getLocation().getChunk()));

                }else if(args[0].equalsIgnoreCase("info")) {
                    final Land land = new Land(player.getLocation().getChunk());
                    if(!land.isClaimed()) { profile.sendMessage("chunknotclaimed"); return false; }

                    new ChunkEditor(land.getChunk()).setRandParticle(Particle.FALLING_WATER, 7, player, 5);
                    player.sendMessage("§7§m---------§r§7[ §2Land §7]§r§7---------");
                    player.sendMessage("§7Chunk: §5x"+land.getChunk().getX()+",z"+land.getChunk().getZ());
                    player.sendMessage("§7");
                    player.sendMessage("§7Owner: §3" + land.getOwnerName());

                    List<String> memberuuids = land.getMemberUUIDs();
                    String memberout = "";
                    for(String memberuuid : memberuuids) {
                        UUID uuid = UUID.fromString(memberuuid);
                        memberout = memberout + Bukkit.getOfflinePlayer(uuid).getName() + ", ";
                    }
                    if(memberout.equals("")) {
                        memberout = "§cnone";
                    }else {
                        memberout = memberout.substring(0, memberout.length()-2);
                    }
                    player.sendMessage("§7Members: §3" + memberout);

                    List<String> banneduuids = land.getBannedUUIDs();
                    String bannedout = "";
                    if(land.getBanall()){
                        bannedout = "§7Everyone";
                    }else {
                        for (String banneduuid : banneduuids) {
                            UUID uuid = UUID.fromString(banneduuid);
                            bannedout = bannedout + Bukkit.getOfflinePlayer(uuid).getName() + ", ";
                        }
                        if (bannedout.equals("")) {
                            bannedout = "§cnone";
                        } else {
                            bannedout = bannedout.substring(0, bannedout.length() - 2);
                        }
                    }
                    player.sendMessage("§7Banned: §3" + bannedout);
                    player.sendMessage("§7");
                    player.sendMessage("§7Flags: PVP="+land.getFlag(Flag.PVP)+" PVE="+land.getFlag(Flag.PVE)+" TNT="+land.getFlag(Flag.TNT));
                    player.sendMessage("§7");

                }else if(args[0].equalsIgnoreCase("claimserver")) {

                    if(!player.hasPermission("server.land.claimserver")) { profile.sendMessage("noperms"); return false; }
                    if(!chunkselected.containsKey(player)) { profile.sendMessage("nochunksselected"); return false; }
                    List<LunaniaChunk> chunks = chunkselected.get(player);
                    for(LunaniaChunk chunk : chunks) {
                        Land land = new Land(chunk);
                        land.claimServer();
                    }
                    profile.sendMessage("chunksclaimedforserver");
                }
            }else if(args.length == 2) {
                if(args[0].equalsIgnoreCase("select")) {
                    String[] argsplit = args[1].split("x");
                    if(argsplit[0].matches("[0-9]+") == false || argsplit[1].matches("[0-9]+") == false) { profile.sendMessage("wrongsyntaxselect"); return false; }
                    int xi = Integer.valueOf(argsplit[0]);
                    int zi = Integer.valueOf(argsplit[1]);

                    chunkselected.put(player, getChunks(player, xi, zi));
                    player.sendMessage(profile.getMessage("chunksselected").replace("%amount%", ""+(xi*zi)));

                }else if(args[0].equalsIgnoreCase("setowner")) {
                    Land land = new Land(player.getLocation().getChunk());
                    if(land.isOwner(player.getUniqueId()) == false && player.hasPermission("server.admin.land.setowner.bypass") == false) { profile.sendMessage("mustbeowner"); return false; }
                    String playername = args[1];
                    @SuppressWarnings("deprecation")
                    OfflinePlayer op = Bukkit.getOfflinePlayer(playername);
                    UUID uuidt = op.getUniqueId();
                    if(uuidt.equals(player.getUniqueId())) return false;
                    if(op.getFirstPlayed() == 0) { profile.sendMessage("playerneveronline"); return false; }
                    PlayerProfile offlineprofile = new PlayerProfile(uuidt);
                    if(offlineprofile.getLands().size() >= offlineprofile.getMaxLands()) { profile.sendMessage("maxlandsreached"); return false; }

                    land.setOwner(uuidt);
                    land.resetBanned();
                    land.resetMember();
                    player.sendMessage("§7"+op.getName()+" §7is now the new owner of that plot.");
                    profile.getScoreboard().updateLand(land);

                }
            }else if(args.length == 3) {
                if(args[0].equalsIgnoreCase("setflag")) {
                    Land land = new Land(player.getLocation().getChunk());
                    if(!land.isOwner(player.getUniqueId())) { profile.sendMessage("mustbeowner"); return false; }
                    String flags = args[1];
                    if(!Flag.isFlag(flags)) { profile.sendMessage("flagdoesntexist"); return false; }
                    Flag flag = Flag.getFlag(flags);

                    if(args[2].equalsIgnoreCase("true")) {
                        land.setFlag(flag, true);
                        player.sendMessage(profile.getMessage("flagset").replace("%flag%", flag.toString().toLowerCase()).replace("%value%", "true"));
                    }else if(args[2].equalsIgnoreCase("false")) {
                        land.setFlag(flag, false);
                        player.sendMessage(profile.getMessage("flagset").replace("%flag%", flag.toString().toLowerCase()).replace("%value%", "false"));
                    }else {
                        profile.sendMessage("onlytrueorfalse");
                    }

                }else if(args[0].equalsIgnoreCase("member")) {
                    String playername = args[2];
                    @SuppressWarnings("deprecation")
                    OfflinePlayer op = Bukkit.getOfflinePlayer(playername);
                    UUID uuidt = op.getUniqueId();
                    Land land = new Land(player.getLocation().getChunk());
                    if(!land.isOwner(player.getUniqueId())) { profile.sendMessage("mustbeowner"); return false; }
                    if(op.getFirstPlayed() == 0) { profile.sendMessage("playerneveronline"); return false; }
                    List<String> memberlist = land.getMemberUUIDs();

                    if(args[1].equalsIgnoreCase("add")) {
                        if(memberlist.contains(uuidt.toString()) == true) { player.sendMessage("§cPlayer is already member of the plot."); return false; }
                        land.addMember(uuidt);
                        player.sendMessage(profile.getMessage("playeraddedtoland").replace("%player%", op.getName()));
                    }else if(args[1].equalsIgnoreCase("remove")) {
                        if(memberlist.contains(uuidt.toString()) == false) { player.sendMessage("§cPlayer is not a member of the plot."); return false; }
                        land.removeMember(uuidt);
                        player.sendMessage(profile.getMessage("playerremovedtoland").replace("%player%", op.getName()));
                    }else {
                        profile.sendMessage("onlyremoveadd");
                    }

                }else if(args[0].equalsIgnoreCase("ban")) {
                    String playername = args[2];
                    Land land = new Land(player.getLocation().getChunk());
                    if(playername.equalsIgnoreCase("*")) {
                        if(args[1].equalsIgnoreCase("add")) {
                            if(land.getBanall()) { profile.sendMessage("everyonealreadybanned"); return false; }
                            land.setBanall(true);
                            profile.sendMessage("bannedeveryonefromland");
                            return false;
                        }else if(args[1].equalsIgnoreCase("remove")) {
                            if(land.getBanall() == false) { profile.sendMessage("everyonealreadyunbanned"); return false; }
                            land.setBanall(false);
                            profile.sendMessage("unbannedeveryonefromland");
                            return false;
                        }else {
                            profile.sendMessage("onlyremoveadd");
                        }
                    }
                    @SuppressWarnings("deprecation")
                    OfflinePlayer op = Bukkit.getOfflinePlayer(playername);
                    UUID uuidt = op.getUniqueId();
                    if(!land.isOwner(player.getUniqueId())) { profile.sendMessage("mustbeowner"); return false; }
                    if(op.getFirstPlayed() == 0) { profile.sendMessage("playerneveronline"); return false; }
                    List<String> bannedlist = land.getBannedUUIDs();
                    if(args[1].equalsIgnoreCase("add")) {
                        if(bannedlist.contains(uuidt.toString())) { player.sendMessage(profile.getMessage("playeralreadybannedland").replace("%player%", op.getName())); return false; }
                        land.addBanned(uuidt);
                        player.sendMessage(profile.getMessage("playerbannedland").replace("%player%", op.getName()));
                    }else if(args[1].equalsIgnoreCase("remove")) {
                        if(bannedlist.contains(uuidt.toString()) == false) { player.sendMessage(profile.getMessage("playernotbannedland").replace("%player%", op.getName())); return false; }
                        land.removeBanned(uuidt);
                        player.sendMessage(profile.getMessage("playerunbannedland").replace("%player%", op.getName()));
                    }else {
                        profile.sendMessage("onlyremoveadd");
                    }
                }
            }
        }else {
            profile.sendMessage("notinlandworld");
        }
        return false;
    }

    public boolean isUnclaimed(ArrayList<LunaniaChunk> chunks) {
        for(LunaniaChunk chunk : chunks) {
            Land land = new Land(chunk);
            if(land.isClaimed()) {
                return false;
            }
        }
        return true;
    }

    public void claimAll(ArrayList<LunaniaChunk> chunks, Player player) {
        for(LunaniaChunk chunk : chunks) {
            Land land = new Land(chunk);
            land.claim(player.getUniqueId());
//			new ChunkEditor(chunk).setEcken(Material.TORCH);
            new ChunkEditor(chunk).setRandParticle(Particle.HAPPY_VILLAGER, 7, player, 5);
            new ChunkEditor(chunk).showSlime(player);
        }
    }

    public double countPrice(ArrayList<LunaniaChunk> chunks) {
        return ((double) chunks.size()) * PRICE_PER_LAND;
    }

    public ArrayList<LunaniaChunk> getChunks(Player player, int xi, int zi) {
        ArrayList<LunaniaChunk> chunks = new ArrayList<LunaniaChunk>();
        Direction direction = getDirectionNESW(player);
        int x = player.getLocation().getChunk().getX();
        int z = player.getLocation().getChunk().getZ();
        if(direction == Direction.NORTH) {
            //z -
            //x +
            for(int xs=0;xs<xi;xs++) {
                for(int zs=0;zs<zi;zs++) {
                    LunaniaChunk chunk = new LunaniaChunk(x+xs, z-zs);
                    chunks.add(chunk);
                    new ChunkEditor(chunk).showSlime(player);
                }
            }
        }else if(direction == Direction.SOUTH) {
            //z +
            //x -
            for(int xs=0;xs<xi;xs++) {
                for(int zs=0;zs<zi;zs++) {
                    LunaniaChunk chunk = new LunaniaChunk(x-xs, z+zs);
                    chunks.add(chunk);
                    new ChunkEditor(chunk).showSlime(player);
                }
            }
        }else if(direction == Direction.WEST) {
            //z -
            //x -
            for(int xs=0;xs<xi;xs++) {
                for(int zs=0;zs<zi;zs++) {
                    LunaniaChunk chunk = new LunaniaChunk(x-xs, z-zs);
                    chunks.add(chunk);
                    new ChunkEditor(chunk).showSlime(player);
                }
            }
        }else if(direction == Direction.EAST) {
            //z +
            //x +
            for(int xs=0;xs<xi;xs++) {
                for(int zs=0;zs<zi;zs++) {
                    LunaniaChunk chunk = new LunaniaChunk(x+xs, z+zs);
                    chunks.add(chunk);
                    new ChunkEditor(chunk).showSlime(player);
                }
            }
        }
        return chunks;
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
                check.add("claim");
                check.add("claimselected");
                check.add("sell");
                check.add("select");
                check.add("buy");
                check.add("ban");
                check.add("list");
                check.add("setalias");
                check.add("setflag");
                check.add("setowner");
                check.add("member");
                for(String s : check) if(s.startsWith(args[0])) tcomplete.add(s);
            }else if(args.length == 2) {
                List<String> check = new ArrayList<String>();
                if(args[0].equalsIgnoreCase("member") || args[0].equalsIgnoreCase("ban")) {
                    check.add("add");
                    check.add("remove");
                }
                for(String s : check) if(s.startsWith(args[1])) tcomplete.add(s);
            }
        }
        return tcomplete;
    }

}