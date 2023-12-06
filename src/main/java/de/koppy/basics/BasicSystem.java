package de.koppy.basics;

import de.koppy.basics.commands.*;
import de.koppy.basics.listener.*;
import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.server.Column;
import de.koppy.server.ColumnType;
import de.koppy.server.SubSystem;
import de.koppy.server.Table;

public class BasicSystem implements SubSystem {

    private static Table table;
    private static Table langtable;

    @Override
    public void loadListener() {
        LunaniaSystem.registerListener(new JoinListener());
        LunaniaSystem.registerListener(new TeleportEvents());
        LunaniaSystem.registerListener(new InventoryEvents());
        LunaniaSystem.registerListener(new ServerManageListener());
        LunaniaSystem.registerListener(new FarmworldListener());
    }

    @Override
    public void loadCommands() {
        LunaniaSystem.registerCommand("home", new Home());
        LunaniaSystem.registerCommand("help", new Help());
        LunaniaSystem.registerCommand("playtime", new Playtime());
        LunaniaSystem.registerCommand("back", new Back());
        LunaniaSystem.registerCommand("changelog", new Changelog());
        LunaniaSystem.registerCommand("collect", new Collect());
        LunaniaSystem.registerCommand("discord", new Discord());
        LunaniaSystem.registerCommand("gamemode", new Gamemode());
        LunaniaSystem.registerCommand("heal", new Heal());
        LunaniaSystem.registerCommand("language", new Language());
        LunaniaSystem.registerCommand("languageset", new Languageset());
        LunaniaSystem.registerCommand("tpa", new Tpa());
        LunaniaSystem.registerCommand("tpaccept", new Tpaccept());
        LunaniaSystem.registerCommand("tpdeny", new Tpadeny());
        LunaniaSystem.registerCommand("tptoggle", new Tpatoggle());
        LunaniaSystem.registerCommand("msg", new Msg());
        LunaniaSystem.registerCommand("r", new R());
    }

    @Override
    public void loadClasses() {
        loadCommands();
        loadListener();
        table = new Table("basics", new Column("uuid", ColumnType.VARCHAR, 200));
        table.addColumn(new Column("food", ColumnType.INT, 200));
        table.addColumn(new Column("hearts", ColumnType.DOUBLE, 200));
        table.addColumn(new Column("homes", ColumnType.VARCHAR, 15000));
        table.addColumn(new Column("maxhomes", ColumnType.INT, 200));
        table.addColumn(new Column("playtime", ColumnType.INT, 200));
        table.addColumn(new Column("language", ColumnType.VARCHAR, 200));
        table.addColumn(new Column("tptoggle", ColumnType.BOOL, 200));
        table.addColumn(new Column("msgtoggle", ColumnType.BOOL, 200));
        table.addColumn(new Column("usetexturepack", ColumnType.BOOL, 200));
        table.createTable();

        langtable = new Table("language", new Column("abbreviation", ColumnType.VARCHAR, 200));
        for(de.koppy.basics.api.Language lang : de.koppy.basics.api.Language.values()) {
            langtable.addColumn(new Column(lang.toString().toLowerCase(), ColumnType.VARCHAR,200));
        }
        langtable.createTable();

    }

    public static Table getLangtable() { return langtable; }

    public static Table getTable() {
        return table;
    }

    public static String getPrefix() {
        return "§3Basics §8| §r";
    }
}
