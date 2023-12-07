package de.koppy.mission.api;

import org.bukkit.inventory.ItemStack;

public class Seasonal extends Mission {

    public Seasonal(String identifier, String name, String description, ItemStack itemshown, int stage, ItemStack[] rewards) {
        super(identifier, name, description, itemshown, Type.SEASONAL, stage, rewards);
        MissionHandler.seasonal.add(this);
    }

}
