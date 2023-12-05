package de.koppy.server;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import de.koppy.lunaniasystem.LunaniaSystem;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class MySQL {

    private String host = "localhost";
    private String port = "3306";
    private String database = "lunania";
    private String username = "root";
    private String password = "";
    private Connection con;

    public void connect() {
        if (!isConnected()) {
            try {
                this.con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
                Bukkit.getConsoleSender().sendMessage("§3Lunania §8| §7MySQL-Connection §aestablished§7.");
            } catch (SQLException e) {
                Bukkit.getConsoleSender().sendMessage("§8[§4CRITICAL§8] §cMySQL connection failed.");
            }
        }
    }

    public void readFile() {
        File file = new File("plugins/Lunania", "mysql.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        if(!file.exists()) {
            cfg.set("host", this.host);
            cfg.set("port", this.port);
            cfg.set("database", this.database);
            cfg.set("username", this.username);
            cfg.set("password", this.password);
            try {
                cfg.save(file);
            } catch (IOException e) {
                Bukkit.getConsoleSender().sendMessage("§8[§4CRITICAL§8] §cCritical MySQL Error (File).");
            }
        }

        host = cfg.getString("host");
        port = cfg.getString("port");
        database = cfg.getString("database");
        username = cfg.getString("username");
        password = cfg.getString("password");
    }

    public void sendStatement(PreparedStatement ps) {
        try {
            ps.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage("§8[§4CRITICAL§8] §cCritical MySQL Error.");
        }
    }

    public void sendStatement(String statement) {
        try {
            PreparedStatement ps = con.prepareStatement(statement);
            ps.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage("§8[§4CRITICAL§8] §cCritical MySQL Error.");
        }
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                con.close();
            } catch (SQLException e) {
                Bukkit.getConsoleSender().sendMessage("§8[§4CRITICAL§8] §cCritical MySQL Error (disconnect).");
            }
        }
    }

    public boolean isConnected() {
        return (con != null);
    }

    public Connection getConnection() {
        return con;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return this.host + ":" + this.port + " Database: " + this.database + " Username: " + this.username + " Password: " + this.password;
    }

}