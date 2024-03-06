package de.koppy.quest.api;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Quest {

    String name;
    String displayname;
    int stages;
    Material material = Material.BOOK;

    public static List<Quest> list = new ArrayList<Quest>();

    public Quest(String name, String displayname, int stages) {
        this.name = name;
        this.displayname = displayname;
        this.stages = stages;
        list.add(this);
    }

    public Material getMaterial() {
        return material;
    }

    public String getDisplayname() {
        return displayname;
    }

    public String getName() {
        return name;
    }

    public int getStages() {
        return stages;
    }

}