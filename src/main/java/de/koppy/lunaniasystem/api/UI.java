package de.koppy.lunaniasystem.api;

import de.koppy.basics.api.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class UI {

    public static HashMap<Inventory, UI> inventories = new HashMap<>();

    public Inventory inventory;

    public UI(String name, int size) {
        this.inventory = Bukkit.createInventory(null, size, name);
        inventories.put(inventory, this);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void closeInventory() {
        inventories.remove(inventory, this);
    }

    public void setGlassRand(ItemStack itemStack) {
        for(int i=0; i<9; i++) {
            inventory.setItem(i, itemStack);
        }
        for(int i=inventory.getSize()-9; i<inventory.getSize(); i++) {
            inventory.setItem(i, itemStack);
        }
        for(int i=0; i<inventory.getSize(); i = i+9) {
            inventory.setItem(i, itemStack);
        }
        for(int i=8; i<inventory.getSize()-9; i = i+9) {
            inventory.setItem(i, itemStack);
        }
    }

    public void fillGlass(ItemStack itemStack) {
        for(int i=0; i<inventory.getSize(); i++) {
            if(inventory.getItem(i) == null)  inventory.setItem(i, itemStack);
        }
    }

}