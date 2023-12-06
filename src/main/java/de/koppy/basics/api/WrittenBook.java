package de.koppy.basics.api;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class WrittenBook {

    private ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
    private BookMeta bookM = (BookMeta) book.getItemMeta();

    public WrittenBook() {
        setAuthor("null");
        setTitle("null");
    }

    public ItemStack getBook() {
        this.book.setItemMeta(this.bookM);
        return this.book;
    }

    public void setAuthor(String author){
        this.bookM.setAuthor(author);
    }

    public void setTitle(String title) {
        this.bookM.setTitle(title);
    }

    public void addPage(Page page) {
        this.bookM.addPage(page.getPage());
    }

    public void setPage(int index, Page page) {
        this.bookM.setPage(index, page.getPage());;
    }

}