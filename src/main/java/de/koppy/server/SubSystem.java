package de.koppy.server;

public interface SubSystem {
    void loadListener();
    void loadCommands();
    void loadClasses();
    String getPrefix();
}
