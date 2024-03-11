package de.koppy.warp;

import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.mysql.api.Column;
import de.koppy.mysql.api.ColumnType;
import de.koppy.mysql.api.Table;
import de.koppy.server.SubSystem;
import de.koppy.server.SubSystem;
import de.koppy.warp.commands.Warp;

public class WarpSystem implements SubSystem {

    private static Table table;

    @Override
    public void loadListener() {

    }

    @Override
    public void loadCommands() {
        LunaniaSystem.registerCommand("warp", new Warp());
    }

    @Override
    public void loadClasses() {

        table = new Table("warps", new Column("name", ColumnType.VARCHAR, 200));
        table.addColumn(new Column("location", ColumnType.VARCHAR, 200));
        table.addColumn(new Column("owner", ColumnType.VARCHAR, 200));
        table.addColumn(new Column("message", ColumnType.VARCHAR, 200));
        table.addColumn(new Column("server", ColumnType.VARCHAR, 200));
        table.addColumn(new Column("accepted", ColumnType.BOOL, 200));
        table.createTable();

        loadCommands();
        loadListener();
    }

    public static String getPrefix() {
        return "§2Warp §8| §r";
    }

    public static Table getTable() {
        return table;
    }

}
