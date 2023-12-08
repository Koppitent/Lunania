package de.koppy.basics.api;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HeadObject {
    private String name;
    private ItemStack itemStack;

    public HeadObject(String name, ItemStack itemStack) {
        this.name = name;
        this.itemStack = itemStack;
    }

    public String getName() {
        return name;
    }

    public ItemStack getItemStack() {
        ItemMeta istackM = itemStack.getItemMeta();
        istackM.setLocalizedName(name);
        itemStack.setItemMeta(istackM);
        return itemStack.clone();
    }
}
