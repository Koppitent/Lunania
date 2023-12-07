package de.koppy.mission.api;

import org.bukkit.inventory.ItemStack;

public class Weekly extends Mission {

    public Weekly(String identifier, String name, String description, ItemStack itemshown, int stage, ItemStack[] rewards) {
        super(identifier, name, description, itemshown, Type.WEEKLY, stage, rewards);
        MissionHandler.weeklies.add(this);
    }

}
