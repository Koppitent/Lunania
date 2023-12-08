package de.koppy.land;

import de.koppy.land.api.Flag;
import de.koppy.land.commands.Lands;
import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.mysql.api.Column;
import de.koppy.mysql.api.ColumnType;
import de.koppy.mysql.api.Table;
import de.koppy.server.SubSystem;
import de.lunania.land.commands.Chunk;
import de.lunania.land.listener.LandEvents;

public class LandSystem implements SubSystem {

    private static Table table;
    @Override
    public void loadListener() {
        LunaniaSystem.registerListener(new LandEvents());
    }

    @Override
    public void loadCommands() {
        LunaniaSystem.registerCommand("chunk", new Chunk());
        LunaniaSystem.registerCommand("land", new Lands());
    }

    @Override
    public void loadClasses() {
        loadCommands();
        loadListener();

        table = new Table("landsystem", new Column("lands", ColumnType.VARCHAR, 200));
        table.addColumn(new Column("member", ColumnType.TEXT, 10000));
        table.addColumn(new Column("banned", ColumnType.TEXT, 10000));
        table.addColumn(new Column("owner", ColumnType.VARCHAR, 200));

        for(Flag flag : Flag.values()) {
            table.addColumn(new Column(flag.toString().toLowerCase(), ColumnType.BOOL, 200));
        }

        table.createTable();

    }

    public static Table getTable() {
        return table;
    }
}
