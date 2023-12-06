package de.koppy.basics.api;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SavedItems {

    private File file;

    public SavedItems(UUID uuid) {
        this.file = new File("plugins/Lunania/SavedInventories", uuid.toString()+".yml");
        if(!file.exists()) {
            saveInventory(Bukkit.createInventory(null, 9*6));
        }
    }

    public Inventory getInventory() {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        int amount = cfg.getKeys(false).size();
        int rows = amount / 9;
        if(amount % 9 != 0) rows++;
        if(amount == 0) rows = 1;
        Inventory inventory = Bukkit.createInventory(null, 9*rows, "Saved-Items");
        for(String index : cfg.getKeys(false)) {
            inventory.addItem(cfg.getItemStack(index));
        }
        return inventory;
    }

    public boolean addItem(ItemStack item) {
        Inventory inventory = getInventory();
        if(inventory.firstEmpty() == -1) return false;
        inventory.addItem(item);
        saveInventory(inventory);
        return true;
    }

    public void saveInventory(Inventory inventory) {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        for(int i=0; i<inventory.getSize(); i++) {
            cfg.set(i+"", inventory.getItem(i));
        }
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}