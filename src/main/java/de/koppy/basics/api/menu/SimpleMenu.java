package de.koppy.basics.api.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class SimpleMenu implements Menu {

    private final Map<Integer, Consumer<Player>> actions = new HashMap<>();
    private final Inventory inventory;

    public SimpleMenu(String title, Rows rows) {
        this.inventory = Bukkit.createInventory(this, rows.getSize(), title);
    }

    @Override
    public void click(Player player, int slot) {
        final Consumer<Player> action = actions.get(slot);

        if(action != null)
            action.accept(player);
    }

    @Override
    public void setItem(int slot, ItemStack item) {
        setItem(slot, item, player -> {});
    }

    @Override
    public void setItem(int slot, ItemStack item, Consumer<Player> action) {
        this.actions.put(slot, action);
        getInventory().setItem(slot, item);
    }

    public abstract void onSetItems();

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }
}
