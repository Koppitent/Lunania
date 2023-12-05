package de.koppy.bansystem;

import de.koppy.bansystem.commands.Ban;
import de.koppy.bansystem.commands.Unban;
import de.koppy.bansystem.listener.JoinListener;
import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.server.Column;
import de.koppy.server.ColumnType;
import de.koppy.server.SubSystem;
import de.koppy.server.Table;

public class BanSystem implements SubSystem {

    private static Table table;

    @Override
    public void loadListener() {
        LunaniaSystem.registerListener(new JoinListener());
    }

    @Override
    public void loadCommands() {
        LunaniaSystem.registerCommand("ban", new Ban());
        LunaniaSystem.registerCommand("unban", new Unban());
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
        table.createTable();
    }

    public static Table getTable() {
        return table;
    }


    public static String getPrefix() {
        return "§cBan §8| §r";
    }
}
