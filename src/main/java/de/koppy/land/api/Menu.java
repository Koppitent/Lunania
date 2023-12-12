package de.koppy.land.api;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class Menu {

    private String name;
    private int size;
    private Inventory inventory;
    public Menu(String name, int size) {
        this.inventory = Bukkit.createInventory(null, size, name);
    }

    public Inventory getInventory() {
        return inventory;
    }
}
