package de.koppy.basics.api;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ChangelogItem {

    private String title;
    private String date;
    private ItemStack book;

    public ChangelogItem(String title, String date) {
        this.title = title;
        this.date = date;
    }

    public ChangelogItem(String title, String date, ArrayList<String> lores) {
        this.title = title;
        this.date = date;
        setBook(lores);
    }

    public void setBook(ItemStack book) {
        this.book = book;
    }

    public void setBook(ArrayList<String> lores) {
        ItemStack update1 = new ItemStack(Material.BOOK);
        ItemMeta update1M = update1.getItemMeta();
        update1M.setDisplayName(this.title + "§7, " + this.date);
        update1M.setLore(lores);
        update1.setItemMeta(update1M);
        this.book = update1;
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
        return book;
    }

    public String getTitle() {
        return title;
    }

}