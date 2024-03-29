package de.koppy.economy;

import de.koppy.economy.commands.Banks;
import de.koppy.economy.commands.Economy;
import de.koppy.economy.listener.MenuEvents;
import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.mysql.api.Column;
import de.koppy.mysql.api.ColumnType;
import de.koppy.server.SubSystem;
import de.koppy.mysql.api.Table;

import java.text.NumberFormat;

public class EconomySystem implements SubSystem {

    private static Table ecotable;
    private static Table banktable;
    private static String ecosymbol = " §6§r";
    private static String prefix = "§5Economy §8| §r";
    private static final NumberFormat f = NumberFormat.getInstance();

    @Override
    public void loadListener() {
        LunaniaSystem.registerListener(new MenuEvents());
    }

    @Override
    public void loadCommands() {
        LunaniaSystem.registerCommand("eco", new Economy());
        LunaniaSystem.registerCommand("money", new Economy());
        LunaniaSystem.registerCommand("balance", new Economy());
        LunaniaSystem.registerCommand("bank", new Banks());
    }

    @Override
    public void loadClasses() {
        loadCommands();
        loadListener();

        ecotable = new Table("economy", new Column("uuid", ColumnType.VARCHAR, 200));
        ecotable.addColumn(new Column("balance", ColumnType.DOUBLE, 200));
        ecotable.addColumn(new Column("visible", ColumnType.BOOL, 200));
        ecotable.addColumn(new Column("bankaccounts", ColumnType.VARCHAR, 200));
        ecotable.addColumn(new Column("logs", ColumnType.TEXT, 40000));

        banktable = new Table("bankaccounts", new Column("accountname", ColumnType.VARCHAR, 200));
        banktable.addColumn(new Column("balance",  ColumnType.DOUBLE, 200));
        banktable.addColumn(new Column("members", ColumnType.VARCHAR, 200));
        banktable.addColumn(new Column("invitemembers", ColumnType.VARCHAR, 200));
        banktable.addColumn(new Column("logs", ColumnType.TEXT, 40000));

        ecotable.createTable();
        banktable.createTable();
    }

    public static String getEcosymbol() {
        return ecosymbol;
    }

    public static Table getBanktable() {
        return banktable;
    }

    public static Table getEcotable() {
        return ecotable;
    }

    public static String getPrefix() {
        return prefix;
    }

    public static NumberFormat getFormat() {
        return f;
    }
}
