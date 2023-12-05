package de.koppy.basics;

import de.koppy.basics.listener.JoinListener;
import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.server.Column;
import de.koppy.server.ColumnType;
import de.koppy.server.SubSystem;
import de.koppy.server.Table;

public class BasicSystem implements SubSystem {

    private static Table table;

    @Override
    public void loadListener() {
        LunaniaSystem.registerListener(new JoinListener());
    }

    @Override
    public void loadCommands() {

    }

    @Override
    public void loadClasses() {
        loadCommands();
        loadListener();
        table = new Table("basics", new Column("uuid", ColumnType.VARCHAR, 200));
        table.addColumn(new Column("food", ColumnType.INT, 200));
        table.addColumn(new Column("hearts", ColumnType.DOUBLE, 200));
        table.addColumn(new Column("homes", ColumnType.VARCHAR, 60000));
        table.addColumn(new Column("maxhomes", ColumnType.INT, 200));
        table.createTable();
    }

    public static Table getTable() {
        return table;
    }

    public static String getPrefix() {
        return "§3Basics §8| §r";
    }
}
