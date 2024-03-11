package de.koppy.basics.api;

import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.lunaniasystem.api.UI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class LanguageUI extends UI {

    private UUID uuid;

    public LanguageUI(UUID uuid) {
        super("§eLanguage Settings", 9*2);
        this.uuid = uuid;
    }

    public void getLanguageSelection() {
        inventory.clear();
        for(int i=9; i<inventory.getSize(); i++) {
            inventory.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayname("§c").getItemStack());
        }
        inventory.setItem(0, new ItemBuilder(Material.PAPER).setDisplayname("§eGerman").setLocalizedName("german").setCustomModelData(1020).getItemStack());
        inventory.setItem(1, new ItemBuilder(Material.PAPER).setDisplayname("§eEnglish").setLocalizedName("english").setCustomModelData(1021).getItemStack());
        inventory.setItem(2, new ItemBuilder(Material.PAPER).setDisplayname("§eBaguette").setLocalizedName("french").setCustomModelData(1022).getItemStack());
    }

    public void select(Language language) {
        PlayerProfile profile = PlayerProfile.getProfile(uuid);
        profile.setLanguage(language);
        profile.getPlayer().sendMessage(profile.getMessage("languagechanged").replace("%language%", language.toString()));
    }

}