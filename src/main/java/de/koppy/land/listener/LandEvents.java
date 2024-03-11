package de.koppy.land.listener;

import java.util.ArrayList;
import java.util.UUID;

import de.koppy.basics.api.PlayerProfile;
import de.koppy.land.api.Flag;
import de.koppy.land.api.Land;
import de.koppy.lunaniasystem.LunaniaSystem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.*;

public class LandEvents implements Listener {

    @EventHandler
    public void onArmorStand(PlayerArmorStandManipulateEvent e) {
        Land land = new Land(e.getRightClicked().getLocation().getChunk());
        if(land.isClaimed()) {
            if(land.isOwner(e.getPlayer().getUniqueId()) == false && land.isMember(e.getPlayer().getUniqueId()) == false && e.getPlayer().hasPermission("server.land.bypass") == false) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemFrame(PlayerInteractEntityEvent e) {
        if(e.getRightClicked() instanceof ItemFrame || e.getRightClicked() instanceof GlowItemFrame) {
            Land land = new Land(e.getRightClicked().getLocation().getChunk());
            if(land.isClaimed()) {
                if(land.isOwner(e.getPlayer().getUniqueId()) == false && land.isMember(e.getPlayer().getUniqueId()) == false && e.getPlayer().hasPermission("server.land.bypass") == false) e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onChunkChange(PlayerMoveEvent e) {
        if(e.getTo().getWorld().getName().equals("world")) {
            if(e.getFrom().getChunk().equals(e.getTo().getChunk()) == false) {
                Player player = e.getPlayer();
                Land land = new Land(e.getTo().getChunk());
                PlayerProfile.getProfile(player.getUniqueId()).getScoreboard().updateLand(land);
                if(land.isClaimed() && land.isBanned(e.getPlayer().getUniqueId())) {
                    if(e.getPlayer().hasPermission("server.admin") == false) e.setCancelled(true);
                    e.getPlayer().setVelocity(e.getPlayer().getLocation().getDirection().multiply(-1d).setY(-1d));
                }
            }
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        PlayerProfile.getProfile(e.getPlayer().getUniqueId()).getScoreboard().updateLand(new Land(e.getTo().getChunk()));
    }

    @EventHandler
    public void onTeleport(PlayerChangedWorldEvent e) {
        PlayerProfile.getProfile(e.getPlayer().getUniqueId()).getScoreboard().updateLand(new Land(e.getPlayer().getLocation().getChunk()));
    }

    @EventHandler
    public void BlockPlace(BlockPlaceEvent e) {
        if(e.getBlock().getLocation().getWorld().getName().equals("world")) {
            Land land = new Land(e.getBlock().getLocation().getChunk());
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
            Land land = new Land(e.getBlock().getLocation().getChunk());
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
    public void BlockPlace(BlockExplodeEvent e) {
        if(e.getBlock().getType() == Material.RESPAWN_ANCHOR) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void BlockPlace(EntityExplodeEvent e) {
        if(e.getLocation().getWorld().getName().equals("world")) {
            Land originalland = new Land(e.getLocation().getChunk());
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
                Land land = new Land(block.getLocation().getChunk());
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
    public void onSpreadEvent(BlockSpreadEvent e) {
        Land land = new Land(e.getSource().getLocation().getChunk());
        if(!land.isClaimed()) return;
        if(land.getOwnerName().equalsIgnoreCase("Server")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onGrowEvent(BlockGrowEvent e) {
        Land land = new Land(e.getBlock().getLocation().getChunk());
        if(!land.isClaimed()) return;
        if(land.getOwnerName().equalsIgnoreCase("Server")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void BlockPlace(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof Player) {
            if(e.getEntity().getLocation().getWorld().getName().equals("world")) {
                Land land = new Land(e.getEntity().getLocation().getChunk());
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
            Land land = new Land(e.getEntity().getLocation().getChunk());
            if(land.isClaimed()) {
                if(land.getFlag(Flag.PVE) == false) {
                    if(e.getDamager().hasPermission("server.admin") == false && land.isOwner(e.getDamager().getUniqueId()) == false && land.isMember(e.getDamager().getUniqueId()) == false) e.setCancelled(true);
                }
            }
        }else if(e.getEntity() instanceof ItemFrame || e.getEntity() instanceof ArmorStand) {
            Land land = new Land(e.getEntity().getLocation().getChunk());
            if(land.isClaimed()) {
                if(e.getDamager().hasPermission("server.admin") == false && land.isOwner(e.getDamager().getUniqueId()) == false && land.isMember(e.getDamager().getUniqueId()) == false) e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void BlockPlace(BlockFromToEvent e) {
        if(e.getBlock().getLocation().getWorld().getName().equals("world")) {
            if(e.getBlock().getChunk().equals(e.getToBlock().getChunk()) == false) {
                Land land = new Land(e.getBlock().getLocation().getChunk());
                Land land2 = new Land(e.getToBlock().getLocation().getChunk());
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
            Land land = new Land(e.getTo().getChunk());
            if(land.isClaimed() && land.isBanned(e.getPlayer().getUniqueId())) {
                if(e.getPlayer().hasPermission("server.admin") == false) e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void BlockPlace(EntitySpawnEvent e) {
        if(e.getEntity() instanceof Monster) {
            if(e.getEntity().getLocation().getWorld().getName().equals("world")) {
                Land land = new Land(e.getEntity().getLocation().getChunk());
                if(land.isClaimed() && land.getOwnerName().equals("Server")) {
                    e.setCancelled(true);
                }
            }
        }
    }

    public static ArrayList<Player> scur = new ArrayList<Player>();
    @EventHandler
    public void landInteractEvent(final PlayerInteractEvent e) {
        if(e.getClickedBlock() == null) return;
        if(!e.getClickedBlock().getLocation().getWorld().getName().equals("world")) return;
        Land land = new Land(e.getClickedBlock().getLocation().getChunk());
        if(land.isClaimed()) {
            if(e.getPlayer().hasPermission("server.admin")) return;
            if(land.isOwner(e.getPlayer().getUniqueId()) || land.isMember(e.getPlayer().getUniqueId())) return;
            e.setCancelled(true);
            if(e.getClickedBlock().getState() instanceof Chest || e.getClickedBlock().getState() instanceof Barrel) {
                e.getPlayer().spawnParticle(Particle.SMOKE_NORMAL, e.getClickedBlock().getLocation().add(0.5, 1.1, 0.5), 10, 0.01D, 0.01D, 0.01D, 0.02D);
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