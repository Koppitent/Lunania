package de.koppy.npc;

import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.npc.commands.NPC;
import de.koppy.npc.listener.NpcEvents;
import de.koppy.server.SubSystem;

public class NpcSystem implements SubSystem {

    @Override
    public void loadListener() {
        LunaniaSystem.registerListener(new NpcEvents());
    }

    @Override
    public void loadCommands() {
        LunaniaSystem.registerCommand("npc", new NPC());
    }

    @Override
    public void loadClasses() {
        loadCommands();
        loadListener();
    }
}
