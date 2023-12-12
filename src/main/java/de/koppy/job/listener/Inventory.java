package de.koppy.job.listener;

import de.koppy.basics.api.ItemBuilder;
import de.koppy.job.api.JobMenu;
import de.koppy.job.api.JobType;
import de.koppy.job.commands.Job;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class Inventory implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(Job.inmenu.contains(e.getWhoClicked())) {
            e.setCancelled(true);
            if(e.getCurrentItem() == null) return;
            if(e.getCurrentItem().getItemMeta().getDisplayName().startsWith("§3")) {
                new JobMenu(e.getWhoClicked().getUniqueId()).getMenuJob(JobType.fromString(e.getCurrentItem().getItemMeta().getLocalizedName()), e.getInventory());
            }else if(JobMenu.jobback.isSimilar(e.getCurrentItem())) {
                new JobMenu(e.getWhoClicked().getUniqueId()).getMainMenu(e.getInventory());
            }else if(JobMenu.jobleave.isSimilar(e.getCurrentItem())) {
                e.getWhoClicked().closeInventory();
                ((Player) e.getWhoClicked()).performCommand("job leave");
            }else if(e.getCurrentItem().getType() == Material.LIME_DYE) {
                e.getWhoClicked().closeInventory();
                ((Player) e.getWhoClicked()).performCommand("job join " + e.getCurrentItem().getItemMeta().getLocalizedName());
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if(Job.inmenu.contains(e.getPlayer())) {
            Job.inmenu.remove(e.getPlayer());
        }
    }

}