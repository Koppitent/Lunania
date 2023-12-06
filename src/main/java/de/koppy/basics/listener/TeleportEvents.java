package de.koppy.basics.listener;

import de.koppy.basics.commands.Back;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportEvents implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Back.back.put(e.getEntity(), e.getEntity().getLocation());
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        if(Back.inback.contains(e.getPlayer())) {
            Back.inback.remove(e.getPlayer());
            return;
        }else {
            Back.back.put(e.getPlayer(), e.getFrom());
        }
    }

}
