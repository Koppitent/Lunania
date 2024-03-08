package de.koppy.cases.api;

import org.bukkit.inventory.ItemStack;

public class MoneyCaseItem extends CaseItem{


    public MoneyCaseItem(ItemStack material, String title, String description, Type type, Object typecontent, Rarity rarity) {
        super(material, title, description, type, typecontent, rarity);
    }
}
