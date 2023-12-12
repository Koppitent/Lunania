package de.koppy.world.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;

public class WorldListener implements Listener {

    @EventHandler
    public void onWorldInitialisation(WorldInitEvent e) {
        //* removes most lag from creating a world
        e.getWorld().setKeepSpawnInMemory(false);
    }

}