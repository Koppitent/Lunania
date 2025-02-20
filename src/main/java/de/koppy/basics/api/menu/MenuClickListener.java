package de.koppy.basics.api.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class MenuClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        final Inventory inventory = e.getInventory();

        if(inventory == null)
            return;

        if(!(inventory.getHolder() instanceof final Menu menu)) {
            return;
        }

        e.setCancelled(true);
        menu.click((Player) e.getWhoClicked(), e.getSlot());
    }

}
