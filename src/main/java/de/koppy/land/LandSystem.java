package de.koppy.land;

import de.koppy.land.commands.Lands;
import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.server.SubSystem;
import de.lunania.land.commands.Chunk;
import de.lunania.land.listener.LandEvents;

public class LandSystem implements SubSystem {

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
    }
}
