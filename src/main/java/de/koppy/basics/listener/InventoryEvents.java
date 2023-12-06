package de.koppy.basics.listener;

import de.koppy.basics.api.HomeMenu;
import de.koppy.basics.api.SavedItems;
import de.koppy.basics.commands.Changelog;
import de.koppy.basics.commands.Collect;
import de.koppy.basics.commands.Home;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.ArrayList;
import java.util.Objects;

public class InventoryEvents implements Listener {

    public static ArrayList<Player> ininv = new ArrayList<Player>();

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;
        if (ininv.contains(e.getWhoClicked())) {
            e.setCancelled(true);
            if (e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                Integer page = Integer.parseInt(e.getCurrentItem().getItemMeta().getLocalizedName());
                Changelog.getChangelogInv(e.getInventory(), Changelog.getChangelogItems(), page);
                ((Player) e.getWhoClicked()).updateInventory();
            }
        } else if (Collect.inCollect.contains(e.getWhoClicked())) {
            if (e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                if (Objects.equals(e.getClickedInventory(), e.getWhoClicked().getInventory())) {
                    e.setCancelled(true);
                }
            }
            e.getWhoClicked().sendMessage(e.getAction().toString());
            if (e.getAction() == InventoryAction.PICKUP_ALL || e.getAction() == InventoryAction.PICKUP_SOME || e.getAction() == InventoryAction.PICKUP_ONE || e.getAction() == InventoryAction.PICKUP_HALF) {
                if (Objects.equals(e.getClickedInventory(), e.getWhoClicked().getInventory())) {
                    e.setCancelled(true);
                }
            }
            if (e.getAction() == InventoryAction.HOTBAR_SWAP) {
                e.setCancelled(true);
            }
        } else if (Home.inmenu.contains(e.getWhoClicked())) {
            e.setCancelled(true);
            if (e.getCurrentItem().getItemMeta().getDisplayName().startsWith("§8»")) {
                e.getWhoClicked().closeInventory();
                ((Player) e.getWhoClicked()).performCommand("home " + e.getCurrentItem().getItemMeta().getLocalizedName());
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().startsWith("§6To page ")) {
                new HomeMenu(e.getWhoClicked().getUniqueId()).updateMenu(e.getInventory(), Integer.parseInt(e.getCurrentItem().getItemMeta().getLocalizedName()));
            }
        }

    }

    @EventHandler
    public void onClick(InventoryCloseEvent e) {
        ininv.remove(e.getPlayer());
        Home.inmenu.remove(e.getPlayer());
        if (Collect.inCollect.contains(e.getPlayer())) {
            Collect.inCollect.remove(e.getPlayer());
            new SavedItems(e.getPlayer().getUniqueId()).saveInventory(e.getInventory());
        }
    }

}
