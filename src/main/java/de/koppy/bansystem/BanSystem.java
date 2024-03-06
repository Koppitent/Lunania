package de.koppy.bansystem;

import de.koppy.bansystem.commands.Ban;
import de.koppy.bansystem.commands.Mute;
import de.koppy.bansystem.commands.Unban;
import de.koppy.bansystem.commands.Unmute;
import de.koppy.bansystem.listener.InventoryListener;
import de.koppy.bansystem.listener.JoinListener;
import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.mysql.api.Column;
import de.koppy.mysql.api.ColumnType;
import de.koppy.server.SubSystem;
import de.koppy.mysql.api.Table;

public class BanSystem implements SubSystem {

    private static Table table;
    private static Table mutetable;

    @Override
    public void loadListener() {
        LunaniaSystem.registerListener(new JoinListener());
        LunaniaSystem.registerListener(new InventoryListener());
    }

    @Override
    public void loadCommands() {
        LunaniaSystem.registerCommand("ban", new Ban());
        LunaniaSystem.registerCommand("unban", new Unban());
        LunaniaSystem.registerCommand("mute", new Mute());
        LunaniaSystem.registerCommand("unmute", new Unmute());
    }

    @Override
    public void loadClasses() {
        loadCommands();
        loadListener();
        table = new Table("bantable", new Column("uuid", ColumnType.VARCHAR, 200));
        table.addColumn(new Column("expiredate", ColumnType.VARCHAR, 2000));
        table.addColumn(new Column("reason", ColumnType.VARCHAR, 1000));
        table.addColumn(new Column("bannedbyuuid", ColumnType.VARCHAR, 200));
        table.addColumn(new Column("history", ColumnType.TEXT, 40000));

        mutetable = new Table("mutetable", new Column("uuid", ColumnType.VARCHAR, 200));
        mutetable.addColumn(new Column("expiredate", ColumnType.VARCHAR, 2000));
        mutetable.addColumn(new Column("reason", ColumnType.VARCHAR, 1000));
        mutetable.addColumn(new Column("mutedbyuuid", ColumnType.VARCHAR, 200));
        mutetable.addColumn(new Column("history", ColumnType.TEXT, 40000));

        table.createTable();
        mutetable.createTable();
    }

    public static Table getTable() {
        return table;
    }


    public static Table getMutetable() {
        return mutetable;
    }

    public static String getPrefix() {
        return "§cBan §8| §r";
    }
}
