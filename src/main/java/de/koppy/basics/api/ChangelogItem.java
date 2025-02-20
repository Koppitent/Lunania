package de.koppy.basics.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ChangelogItem {

    private String title;
    private String date;
    private List<String> items = new ArrayList<>();

    public ChangelogItem(String title, String date) {
        this.title = title;
        this.date = date;
    }

    public ChangelogItem(String title, String date, ArrayList<String> lores) {
        this.title = title;
        this.date = date;
        items.addAll(lores);
    }

    public void addItem(String item) {
        this.items.add(item);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public ItemStack getBook() {
        return new ItemBuilder(Material.BOOK).setLore(items).setDisplayname(this.title + "§7, " + this.date).getItemStack();
    }

    public String getTitle() {
        return title;
    }

}