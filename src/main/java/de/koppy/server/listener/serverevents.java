package de.koppy.server.listener;

import de.koppy.lunaniasystem.LunaniaSystem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class serverevents implements Listener {

    @EventHandler
    public void onMOTD(ServerListPingEvent e) {
        e.setMotd(LunaniaSystem.getServerInstance().getMotd());
    }

}