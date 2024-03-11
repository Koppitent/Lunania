package de.koppy.cases.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CaseItem {

    private ItemStack material;
    private String title;
    private String description;
    private Type type;
    private Object typecontent;
    private Rarity rarity;

    public CaseItem(ItemStack material, String title, String description, Type type, Object typecontent, Rarity rarity) {
        this.material = material.clone();
        this.title = title;
        this.description = description;
        this.type = type;
        this.typecontent = typecontent;
        this.rarity = rarity;
    }

    public ItemStack getCustomItem() {
        ItemStack istack = material.clone();
        ItemMeta istackM = istack.getItemMeta();
        istackM.setDisplayName(title);
        List<String> lore = new ArrayList<String>();
        lore.add("§7-----------");
        lore.add(description);
        lore.add(" ");
        lore.add(rarity.toFormatString());
        istackM.setLore(lore);
        istack.setItemMeta(istackM);
        return istack;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public ItemStack getMaterial() {
        return material.clone();
    }

    public Rarity getRarity() {
        return rarity;
    }

    public double getRewardCompensation() {
        switch (this.rarity) {
            case LEGENDARY:
                return 50000;
            case EPIC:
                return 10000;
            case RARE:
                return 5000;
            case UNCOMMON:
                return 1000;
            case COMMON:
                return 100;
            default:
                return 100;
        }
    }

    public Type getType() {
        return type;
    }

    public Object getRewardObject() {
        return typecontent;
    }

}