package de.koppy.job.listener;

import de.koppy.job.api.JobMenu;
import de.koppy.job.api.JobType;
import de.koppy.job.commands.Job;
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