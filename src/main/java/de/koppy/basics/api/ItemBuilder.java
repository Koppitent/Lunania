package de.koppy.basics.api;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {

    private ItemStack istack;
    public ItemBuilder(Material material) {
        this.istack = new ItemStack(material);
    }

    public ItemBuilder setAmount(int amount) {
        this.istack.setAmount(amount);
        return this;
    }

    public ItemBuilder setLocalizedName(String localizedname) {
        ItemMeta meta = this.istack.getItemMeta();
        meta.setLocalizedName(localizedname);
        this.istack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setCustomModelData(int id) {
        ItemMeta meta = this.istack.getItemMeta();
        meta.setCustomModelData(id);
        this.istack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setMaterial(Material material) {
        this.istack.setType(material);
        return this;
    }

    public ItemBuilder setDisplayname(String displayname) {
        ItemMeta meta = this.istack.getItemMeta();
        meta.setDisplayName(displayname);
        this.istack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        ItemMeta meta = this.istack.getItemMeta();
        meta.setLore(lore);
        this.istack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addLore(String text) {
        ItemMeta meta = this.istack.getItemMeta();
        List<String> lore = meta.getLore();
        if(lore == null) lore = new ArrayList<>();
        lore.add(text);
        meta.setLore(lore);
        this.istack.setItemMeta(meta);
        return this;
    }

    public ItemStack getItemStack() {
        return istack.clone();
    }

    public ItemBuilder setSkull(String playername) {
        SkullMeta meta = (SkullMeta) this.istack.getItemMeta();
        meta.setOwner(playername);
        this.istack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder getSkullURL(String url) {
        this.istack = SkullMethods.getSkull(url);
        return this;
    }

    public ItemBuilder getSkullValue(String value) {
        this.istack = SkullMethods.getSkullValue(value);
        return this;
    }

}