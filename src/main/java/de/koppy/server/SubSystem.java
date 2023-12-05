package de.koppy.server;

import de.koppy.lunaniasystem.LunaniaSystem;

public interface SubSystem {
    void loadListener();
    void loadCommands();
    void loadClasses();
    static String getPrefix(){
        return LunaniaSystem.getServerInstance().getPrefix();
    };
}
