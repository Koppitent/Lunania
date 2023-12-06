package de.koppy.basics.listener;

import de.koppy.lunaniasystem.LunaniaSystem;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class FarmworldListener implements Listener {

    @EventHandler
    public void onFarmbreak(BlockBreakEvent e) {
        if(e.getBlock().getWorld().getName().equals("farmworld")) {
            if(!e.getPlayer().hasPermission("server.admin.bypassfarmwelt")) {
                Location spawn = e.getBlock().getWorld().getSpawnLocation();
                spawn.setY(e.getPlayer().getLocation().getY());
                int distance = (int) e.getBlock().getLocation().distance(spawn);
                if(distance < 100) {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§cDu kannst in "+(100-distance)+" Blöcken abbauen");
                }
            }
        }
    }

    @EventHandler
    public void onFarmbreak(EntityDamageEvent e) {
        if(e.getEntity() instanceof Player) {
            if(e.getEntity().getWorld().getName().equals("farmworld")) {
                Location spawn = e.getEntity().getWorld().getSpawnLocation();
                spawn.setY(e.getEntity().getLocation().getY());
                int distance = (int) e.getEntity().getLocation().distance(spawn);
                if(distance < 100) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onFarmbreak(BlockPlaceEvent e) {
        if(e.getBlock().getWorld().getName().equals("farmworld")) {
            if(!e.getPlayer().hasPermission("server.admin.bypassfarmwelt")) {
                Location spawn = e.getBlock().getWorld().getSpawnLocation();
                spawn.setY(e.getPlayer().getLocation().getY());
                int distance = (int) e.getBlock().getLocation().distance(spawn);
                if(distance < 100) {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§cDu kannst in "+(100-distance)+" Blöcken abbauen");
                }
            }
        }
    }

}
