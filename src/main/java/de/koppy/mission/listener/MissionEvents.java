package de.koppy.mission.listener;

import de.koppy.basics.api.LanguageUI;
import de.koppy.lunaniasystem.api.UI;
import de.koppy.mission.api.MissionHandler;
import de.koppy.mission.api.MissionUI;
import de.koppy.mission.commands.Mission;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class MissionEvents implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;
        if(UI.inventories.containsKey(e.getInventory())) {
            UI ui = UI.inventories.get(e.getInventory());
            if (ui instanceof MissionUI) {
                MissionUI mui = (MissionUI) ui;
                if(mui.getType().equalsIgnoreCase("Daily")) {
                    if(e.getCurrentItem().getItemMeta().getDisplayName().startsWith("§3")) {
                        new MissionHandler().claimRewardDaily((Player) e.getWhoClicked(), e.getCurrentItem().getItemMeta().getLocalizedName());
                    }
                }else if(mui.getType().equalsIgnoreCase("Weekly")) {
                    if(e.getCurrentItem().getItemMeta().getDisplayName().startsWith("§3")) {
                        new MissionHandler().claimRewardWeekly((Player) e.getWhoClicked(), e.getCurrentItem().getItemMeta().getLocalizedName());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onChopLog(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if(e.getBlock().getType() == Material.OAK_LOG || e.getBlock().getType() == Material.OAK_WOOD) {
            new MissionHandler().addStageDailyIfExist(p, "oak");
            new MissionHandler().addStageWeeklyIfExist(p, "oakw");
        }else if(e.getBlock().getType() == Material.SPRUCE_LOG || e.getBlock().getType() == Material.SPRUCE_WOOD) {
            new MissionHandler().addStageDailyIfExist(p, "spruce");
            new MissionHandler().addStageWeeklyIfExist(p, "sprucew");
        }else if(e.getBlock().getType() == Material.ACACIA_LOG || e.getBlock().getType() == Material.ACACIA_WOOD) {
            new MissionHandler().addStageDailyIfExist(p, "acacia");
            new MissionHandler().addStageWeeklyIfExist(p, "acaciaw");
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if(e.getEntity().getKiller() != null) {
            if(e.getEntity().getKiller() instanceof Player) {
                Player p = (Player) e.getEntity().getKiller();
                if(e.getEntityType() == EntityType.PIG) {
                    new MissionHandler().addStageDailyIfExist(p, "pigs");
                    new MissionHandler().addStageWeeklyIfExist(p, "pigsw");
                }else if(e.getEntityType() == EntityType.ZOMBIE) {
                    new MissionHandler().addStageDailyIfExist(p, "zombies");
                    new MissionHandler().addStageWeeklyIfExist(p, "zombiesw");
                }else if(e.getEntityType() == EntityType.COW) {
                    new MissionHandler().addStageDailyIfExist(p, "cows");
                    new MissionHandler().addStageWeeklyIfExist(p, "cowsw");
                }else if(e.getEntityType() == EntityType.SKELETON) {
                    new MissionHandler().addStageDailyIfExist(p, "skelletons");
                    new MissionHandler().addStageWeeklyIfExist(p, "skelletonsw");
                }else if(e.getEntityType() == EntityType.LLAMA) {
                    new MissionHandler().addStageDailyIfExist(p, "lama");
                    new MissionHandler().addStageWeeklyIfExist(p, "lamaw");
                }
            }
        }
    }
}