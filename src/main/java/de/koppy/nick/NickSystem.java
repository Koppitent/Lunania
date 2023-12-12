package de.koppy.nick;

import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.nick.commands.Nick;
import de.koppy.nick.commands.Skin;
import de.koppy.nick.commands.Unnick;
import de.koppy.server.SubSystem;

import java.util.ArrayList;

public class NickSystem implements SubSystem {

    private static final ArrayList<String> predefinednicknames = new ArrayList<String>();

    @Override
    public void loadListener() { /* Ich bin ganz allein UwU */ }

    @Override
    public void loadCommands() {
        LunaniaSystem.registerCommand("nick", new Nick());
        LunaniaSystem.registerCommand("skin", new Skin());
        LunaniaSystem.registerCommand("unnick", new Unnick());
    }

    @Override
    public void loadClasses() {
        predefinednicknames.add("xXGamerHDXx");
        predefinednicknames.add("SchwarzerPeter");

        loadCommands();
        loadListener();
    }

    public static ArrayList<String> getPredefinednicknames() {
        return predefinednicknames;
    }
}