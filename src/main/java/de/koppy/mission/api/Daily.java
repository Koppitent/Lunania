package de.koppy.mission.api;

import org.bukkit.inventory.ItemStack;

public class Daily extends Mission {

    public Daily(String identifier, String name, String description, ItemStack itemshown, int stage, ItemStack[] rewards) {
        super(identifier, name, description, itemshown, Type.DAILY, stage, rewards);
        MissionHandler.dailies.add(this);
    }

}
