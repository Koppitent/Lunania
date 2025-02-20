package de.koppy.cases.listener;

import java.util.ArrayList;
import java.util.List;

import de.koppy.cases.api.Case;
import de.koppy.cases.api.PlayerCase;
import de.koppy.lunaniasystem.LunaniaSystem;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class CaseEvents implements Listener {

    public static List<Player> inpreview = new ArrayList<Player>();
    public static List<Player> inrolling = new ArrayList<Player>();
    
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
            if(e.getItem() != null) {
                if(e.getItem().getItemMeta().getDisplayName().startsWith("§8§")) {
                    e.setCancelled(true);
                    if(!e.getItem().getItemMeta().getDisplayName().endsWith("§7-Key")) {
                        String name = e.getItem().getItemMeta().getDisplayName().replace("§8§", "").replace(" ", "").replace("§3", "");
                        Case c = Case.getCasebyName(name);
                        inpreview.add(e.getPlayer());
                        e.getPlayer().openInventory(c.getPreview());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if(inrolling.contains(e.getWhoClicked())) {
            e.setCancelled(true);
        }
        if(inpreview.contains(e.getWhoClicked())) {
            e.setCancelled(true);
            if(e.getCurrentItem() != null) {
                if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7Open Case")) {
                    NamespacedKey key = new NamespacedKey(LunaniaSystem.getPlugin(), "item_custom_data");
                    String name = e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
                    Case c = Case.getCasebyName(name);
                    if(c.needsKey()) {
                        PlayerCase pc = new PlayerCase(e.getWhoClicked().getUniqueId());
                        if(pc.hasKey(name)) {
                            pc.removeAmount(name, 1);
                            if(existCaseInInv(c, e.getWhoClicked().getInventory())) {
                                removeCaseInInv(c, e.getWhoClicked().getInventory());
                                e.getWhoClicked().closeInventory();
                                c.open((Player) e.getWhoClicked());
                            }else {
                                e.getWhoClicked().sendMessage("§cNo case in inv.");
                            }
                        }else {
                            e.getWhoClicked().sendMessage("§cNo key.");
                        }
                    }else {
                        if(existCaseInInv(c, e.getWhoClicked().getInventory())) {
                            removeCaseInInv(c, e.getWhoClicked().getInventory());
                            e.getWhoClicked().closeInventory();
                            c.open((Player) e.getWhoClicked());
                        }else {
                            e.getWhoClicked().sendMessage("§cNo case in inv.");
                        }
                    }
                }
            }
        }
    }

    public void removeCaseInInv(Case c, Inventory inventory) {
        for(int i=0; i<inventory.getSize(); i++) {
            if(inventory.getItem(i) != null) {
                ItemStack item = c.getCase().clone();
                ItemMeta itemM = item.getItemMeta();
                NamespacedKey key = new NamespacedKey(LunaniaSystem.getPlugin(), "item_custom_data");
                itemM.getPersistentDataContainer().set(key, PersistentDataType.STRING, inventory.getItem(i).getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING));
                item.setItemMeta(itemM);
                if(inventory.getItem(i).isSimilar(item)) {
                    inventory.setItem(i, null);
                    return;
                }
            }
        }
    }

    public boolean existCaseInInv(Case c, Inventory inventory) {
        for(int i=0; i<inventory.getSize(); i++) {
            if(inventory.getItem(i) != null) {
                ItemStack item = c.getCase().clone();
                ItemMeta itemM = item.getItemMeta();
                NamespacedKey key = new NamespacedKey(LunaniaSystem.getPlugin(), "item_custom_data");
                itemM.getPersistentDataContainer().set(key, PersistentDataType.STRING, inventory.getItem(i).getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING));
                item.setItemMeta(itemM);
                if(inventory.getItem(i).isSimilar(item)) {
                    return true;
                }
            }
        }
        return false;
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent e) {
        if(inpreview.contains(e.getPlayer())) {
            inpreview.remove(e.getPlayer());
        }
        if(inrolling.contains(e.getPlayer())) {
            inrolling.remove(e.getPlayer());
        }
    }

}