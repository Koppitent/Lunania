package de.koppy.basics.api.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public interface Menu extends InventoryHolder {
    void click(Player player, int slot);
    void setItem(int slot, ItemStack item);
    void setItem(int slot, ItemStack item, Consumer<Player> action);
    void onSetItems();
    default void update() {
        getInventory().clear();
        onSetItems();
    }
    default void open(Player player) {
        onSetItems();
        player.openInventory(getInventory());
    }
}
