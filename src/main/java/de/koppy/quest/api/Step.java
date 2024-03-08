package de.koppy.quest.api;

import org.bukkit.entity.Player;

public class Step {
    private String name;
    private String description;

    public Step(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void callNewStep(Player player) {
        player.sendTitle("§3Next Step:", "§7"+name);
    }
}
