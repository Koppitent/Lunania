package de.lunania.land.listener;

import java.util.ArrayList;
import java.util.UUID;

import de.koppy.basics.api.PlayerProfile;
import de.koppy.land.api.Flag;
import de.koppy.land.api.LandFileSystem;
import de.koppy.lunaniasystem.LunaniaSystem;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class LandEvents implements Listener {

    @EventHandler
    public void onChunkChange(PlayerMoveEvent e) {
        if(e.getTo().getWorld().getName().equals("world")) {
            if(e.getFrom().getChunk().equals(e.getTo().getChunk()) == false) {
                Player player = e.getPlayer();
                LandFileSystem land = new LandFileSystem(e.getTo().getChunk());
                PlayerProfile.getProfile(player.getUniqueId()).getScoreboard().updateLand(land);
                if(land.isClaimed() && land.isBanned(e.getPlayer().getUniqueId())) {
                    if(e.getPlayer().hasPermission("server.admin") == false) e.setCancelled(true);
                    e.getPlayer().setVelocity(e.getPlayer().getLocation().getDirection().multiply(-1d).setY(-1d));
                }
            }
        }
    }

    @EventHandler
    public void BlockPlace(BlockPlaceEvent e) {
        if(e.getBlock().getLocation().getWorld().getName().equals("world")) {
            LandFileSystem land = new LandFileSystem(e.getBlock().getLocation().getChunk());
            if(land.isClaimed()) {
                if(land.isOwner(e.getPlayer().getUniqueId()) || land.isMember(e.getPlayer().getUniqueId())) {
                    if(e.getPlayer().hasPermission("server.admin") == false) e.setCancelled(false);
                }else {
                    if(e.getPlayer().hasPermission("server.admin") == false) e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void BlockPlace(BlockBreakEvent e) {
        if(e.getBlock().getLocation().getWorld().getName().equals("world")) {
            LandFileSystem land = new LandFileSystem(e.getBlock().getLocation().getChunk());
            if(land.isClaimed()) {
                if(land.isOwner(e.getPlayer().getUniqueId()) || land.isMember(e.getPlayer().getUniqueId())) {
                    if(e.getPlayer().hasPermission("server.admin") == false) e.setCancelled(false);
                }else {
                    if(e.getPlayer().hasPermission("server.admin") == false) e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void BlockPlace(EntityExplodeEvent e) {
        if(e.getLocation().getWorld().getName().equals("world")) {
            LandFileSystem originalland = new LandFileSystem(e.getLocation().getChunk());
            if(originalland.getOwnerName().equals("Server")) {
                e.setCancelled(true);
                return;
            }
            String ouuid = "";
            if(originalland.isClaimed()) {
                ouuid = originalland.getOwnerUUID();
            }
            UUID uuid = UUID.randomUUID();
            if(ouuid.equals("") == false) uuid = UUID.fromString(ouuid);
            ArrayList<Block> blocknotexplode = new ArrayList<Block>();
            for(Block block : e.blockList()) {
                LandFileSystem land = new LandFileSystem(block.getLocation().getChunk());
                if(land.isClaimed() && land.isOwner(uuid) == false) {
                    blocknotexplode.add(block);
                }else {
                    if(land.isClaimed() && land.getFlag(Flag.TNT) == false) {
                        blocknotexplode.add(block);
                    }
                }
            }
            e.blockList().removeAll(blocknotexplode);
        }
    }

    @EventHandler
    public void BlockPlace(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof Player) {
            if(e.getEntity().getLocation().getWorld().getName().equals("world")) {
                LandFileSystem land = new LandFileSystem(e.getEntity().getLocation().getChunk());
                if(land.isClaimed()) {
                    if(e.getDamager() instanceof Player) {
                        if(land.getFlag(Flag.PVP) == false) {
                            if(e.getDamager().hasPermission("server.admin") == false) e.setCancelled(true);
                        }
                    }else if(e.getDamager() instanceof Arrow) {
                        if(((Arrow) e.getDamager()).getShooter() instanceof Player) {
                            if(land.getFlag(Flag.PVP) == false) {
                                if(((Player)((Arrow) e.getDamager()).getShooter()).hasPermission("server.admin") == false) e.setCancelled(true);
                            }
                        }
                    }else if(e.getDamager() instanceof Trident) {
                        if(((Trident) e.getDamager()).getShooter() instanceof Player) {
                            if(land.getFlag(Flag.PVP) == false) {
                                if(((Player)((Trident) e.getDamager()).getShooter()).hasPermission("server.admin") == false) e.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }else if(e.getEntity() instanceof Animals) {
            LandFileSystem land = new LandFileSystem(e.getEntity().getLocation().getChunk());
            if(land.isClaimed()) {
                if(land.getFlag(Flag.PVE) == false) {
                    if(e.getDamager().hasPermission("server.admin") == false) e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void BlockPlace(BlockFromToEvent e) {
        if(e.getBlock().getLocation().getWorld().getName().equals("world")) {
            if(e.getBlock().getChunk().equals(e.getToBlock().getChunk()) == false) {
                LandFileSystem land = new LandFileSystem(e.getBlock().getLocation().getChunk());
                LandFileSystem land2 = new LandFileSystem(e.getToBlock().getLocation().getChunk());
                if(land2.isClaimed() && land.isClaimed()) {
                    if(land.getOwnerUUID().equals(land2.getOwnerUUID()) == false) {
                        e.setCancelled(true);
                    }
                }else {
                    if(land2.isClaimed()) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void BlockPlace(PlayerTeleportEvent e) {
        if(e.getTo().getWorld().getName().equals("world")) {
            LandFileSystem land = new LandFileSystem(e.getTo().getChunk());
            if(land.isClaimed() && land.isBanned(e.getPlayer().getUniqueId())) {
                if(e.getPlayer().hasPermission("server.admin") == false) e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void BlockPlace(EntitySpawnEvent e) {
        if(e.getEntity() instanceof Monster) {
            if(e.getEntity().getLocation().getWorld().getName().equals("world")) {
                LandFileSystem land = new LandFileSystem(e.getEntity().getLocation().getChunk());
                if(land.isClaimed() && land.getOwnerName().equals("Server")) {
                    e.setCancelled(true);
                }
            }
        }
    }

    public static ArrayList<Player> scur = new ArrayList<Player>();
    @EventHandler
    public void BlockPlace(final PlayerInteractEvent e) {
        if(e.getClickedBlock() == null) return;
        if(!e.getClickedBlock().getLocation().getWorld().getName().equals("world")) return;
        LandFileSystem land = new LandFileSystem(e.getClickedBlock().getLocation().getChunk());
        if(land.isClaimed()) {
            if(e.getPlayer().hasPermission("server.admin")) return;
            if(land.isOwner(e.getPlayer().getUniqueId()) || land.isMember(e.getPlayer().getUniqueId())) return;
            e.setCancelled(true);
            if(e.getClickedBlock().getState() instanceof Chest || e.getClickedBlock().getState() instanceof Barrel) {
                e.getPlayer().spawnParticle(Particle.SMOKE_NORMAL, e.getPlayer().getLocation(), 10, 0);
                if(scur.contains(e.getPlayer())) return;
                scur.add(e.getPlayer());
                e.getPlayer().sendMessage("§cYou cant use this Chest!");
                Bukkit.getScheduler().runTaskLater(LunaniaSystem.getPlugin(), new Runnable() {
                    public void run() {
                        scur.remove(e.getPlayer());
                    }
                }, 20*2);
            }else if(e.getClickedBlock().getState() instanceof Door || e.getClickedBlock().getState() instanceof TrapDoor) {
                e.getPlayer().spawnParticle(Particle.BLOCK_MARKER, e.getPlayer().getLocation(), 1, "barrier");
                if(scur.contains(e.getPlayer())) return;
                scur.add(e.getPlayer());
                e.getPlayer().sendMessage("§cYou cant use this door!");
                Bukkit.getScheduler().runTaskLater(LunaniaSystem.getPlugin(), new Runnable() {
                    public void run() {
                        scur.remove(e.getPlayer());
                    }
                }, 20*2);
            }
        }
    }

}