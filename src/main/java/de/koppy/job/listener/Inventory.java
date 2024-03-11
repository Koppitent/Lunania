package de.koppy.job.listener;

import de.koppy.basics.api.ItemBuilder;
import de.koppy.economy.EconomySystem;
import de.koppy.economy.commands.Economy;
import de.koppy.job.api.JobMenu;
import de.koppy.job.api.JobType;
import de.koppy.job.api.TaskAchievedEvent;
import de.koppy.job.commands.Job;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.text.DecimalFormat;

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
    public void onAchieve(TaskAchievedEvent e) {
        e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§e§l"+e.getJobType().toString().toUpperCase() + " §r§7You gained §e" + new DecimalFormat("#,###.##").format(e.getAmountmoney())
        + EconomySystem.getEcosymbol() + " §7and " + e.getAmountxp()+"§3XP"));
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if(Job.inmenu.contains(e.getPlayer())) {
            Job.inmenu.remove(e.getPlayer());
        }
    }

}