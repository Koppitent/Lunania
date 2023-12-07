package de.koppy.mysql;

import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.mysql.commands.MySQL;
import de.koppy.server.SubSystem;

public class MysqlSystem implements SubSystem {


    @Override
    public void loadListener() {

    }

    @Override
    public void loadCommands() {
        LunaniaSystem.registerCommand("mysql", new MySQL());
    }

    @Override
    public void loadClasses() {
        loadCommands();
        loadListener();
    }
}
