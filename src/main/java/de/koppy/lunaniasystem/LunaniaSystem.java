package de.koppy.lunaniasystem;

import de.koppy.mysql.api.MySQL;
import de.koppy.server.Server;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class LunaniaSystem extends JavaPlugin {

    private static Server server;
    private static MySQL mysql;

    private static LunaniaSystem plugin;

    @Override
    public void onEnable() {
        plugin = this;

        mysql = new MySQL();
        mysql.readFile();
        mysql.connect();

        server = new Server();

    }

    @Override
    public void onDisable() {
        plugin = null;
    }

    public static LunaniaSystem getPlugin() { return plugin; }

    public static MySQL getMySQLInstance() { return mysql; }

    public static Server getServerInstance() {
        return server;
    }

    public static void registerCommand(String command, CommandExecutor executorinstance) {
        plugin.getCommand(command).setExecutor(executorinstance);
    }

    public static void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

}
