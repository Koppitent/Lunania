package de.koppy.mission.listener;

import de.koppy.mission.api.MissionHandler;
import de.koppy.mission.commands.Mission;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class MissionEvents implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(Mission.inmenudaily.contains(e.getWhoClicked())) {
            e.setCancelled(true);
            if(e.getCurrentItem() != null) {
                if(e.getCurrentItem().getItemMeta().getDisplayName().startsWith("�3")) {
                    new MissionHandler().claimRewardDaily((Player) e.getWhoClicked(), e.getCurrentItem().getItemMeta().getLocalizedName());
                }
            }
        }
        if(Mission.inmenuweekly.contains(e.getWhoClicked())) {
            e.setCancelled(true);
            if(e.getCurrentItem() != null) {
                if(e.getCurrentItem().getItemMeta().getDisplayName().startsWith("�3")) {
                    new MissionHandler().claimRewardWeekly((Player) e.getWhoClicked(), e.getCurrentItem().getItemMeta().getLocalizedName());
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if(Mission.inmenudaily.contains(e.getPlayer())) {
            Mission.inmenudaily.remove(e.getPlayer());
        }
        if(Mission.inmenuweekly.contains(e.getPlayer())) {
            Mission.inmenuweekly.remove(e.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent e) {
        new MissionHandler().checkDailies(e.getPlayer());
        new MissionHandler().checkWeeklies(e.getPlayer());
        new MissionHandler().checkSeasonal(e.getPlayer());
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if(e.getEntity().getKiller() != null) {
            if(e.getEntity().getKiller() instanceof Player) {
                Player p = (Player) e.getEntity().getKiller();
                if(e.getEntityType() == EntityType.PIG) {
                    new MissionHandler().addStageDailyIfExist(p, "pigs");
                }else if(e.getEntityType() == EntityType.ZOMBIE) {
                    new MissionHandler().addStageDailyIfExist(p, "zombies");
                }else if(e.getEntityType() == EntityType.COW) {
                    new MissionHandler().addStageDailyIfExist(p, "cows");
                }else if(e.getEntityType() == EntityType.SKELETON) {
                    new MissionHandler().addStageDailyIfExist(p, "skelletons");
                }else if(e.getEntityType() == EntityType.LLAMA) {
                    new MissionHandler().addStageDailyIfExist(p, "lama");
                }
            }
        }
    }
}