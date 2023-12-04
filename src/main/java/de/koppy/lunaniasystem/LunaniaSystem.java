package de.koppy.lunaniasystem;

import de.koppy.server.MySQL;
import de.koppy.server.Server;
import de.koppy.server.commands.test;
import de.koppy.server.listener.serverevents;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class LunaniaSystem extends JavaPlugin {

    private static Server server;
    private static MySQL mysql = new MySQL();

    private static LunaniaSystem plugin;

    @Override
    public void onEnable() {
        plugin = this;
        server = new Server();

        mysql.readFile();
        mysql.connect();

        registerCommand("test", new test());
        registerListener(new serverevents());
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
