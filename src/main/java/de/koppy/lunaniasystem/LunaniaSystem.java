package de.koppy.lunaniasystem;

import de.koppy.server.Server;
import org.bukkit.plugin.java.JavaPlugin;

public final class LunaniaSystem extends JavaPlugin {

    private static Server server;

    @Override
    public void onEnable() {
        server = new Server();
        server.setMaxplayer(50);

    }

    @Override
    public void onDisable() {

    }

    public static Server getServerInstance() {
        return server;
    }

}
