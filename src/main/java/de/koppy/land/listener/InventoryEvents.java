package de.koppy.land.listener;

import de.koppy.basics.api.ItemBuilder;
import de.koppy.land.api.Flag;
import de.koppy.land.api.Land;
import de.koppy.land.api.LandMenu;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.ArrayList;

public class InventoryEvents implements Listener {

    public static ArrayList<Player> ininv = new ArrayList<>();
    @EventHandler
    public void onInventory(InventoryClickEvent e) {
        if(ininv.contains(e.getWhoClicked())) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) return;
            if (e.getCurrentItem().getType() == Material.PLAYER_HEAD && e.getCurrentItem().getItemMeta().getDisplayName().startsWith("§7Zu Seite")) {
                int page = Integer.parseInt(e.getCurrentItem().getItemMeta().getLocalizedName().split(":")[1]);
                switch (e.getCurrentItem().getItemMeta().getLocalizedName().split(":")[0]) {
                    case "member":
                        new LandMenu(e.getInventory()).getLandManageMember(page, e.getWhoClicked().getLocation().getChunk());
                        break;
                    case "ban":
                        new LandMenu(e.getInventory()).getLandManageBan(page, e.getWhoClicked().getLocation().getChunk());
                        break;
                    case "lands":
                        new LandMenu(e.getInventory()).getListLands(page, e.getWhoClicked().getUniqueId());
                        break;
                    default:
                        e.getWhoClicked().closeInventory();
                        break;
                }
                new LandMenu(e.getInventory()).getListLands(page, e.getWhoClicked().getUniqueId());
            } else if (e.getCurrentItem().isSimilar(new ItemBuilder(Material.MAP).setDisplayname("§2List of Lands").getItemStack())) {
                new LandMenu(e.getInventory()).getListLands(1, e.getWhoClicked().getUniqueId());
            } else if (e.getCurrentItem().isSimilar(new ItemBuilder(Material.PAPER).setDisplayname("§cGo Back").getItemStack())) {
                new LandMenu(e.getInventory()).getMainPage();
            } else if (e.getCurrentItem().isSimilar(new ItemBuilder(Material.PAPER).setDisplayname("§eHelp §7for lands").getItemStack())) {
                new LandMenu(e.getInventory()).getListHelp();
            } else if (e.getCurrentItem().isSimilar(new ItemBuilder(Material.WOODEN_AXE).setDisplayname("§3Manage Lands").getItemStack())) {
                new LandMenu(e.getInventory()).getLandManage(e.getWhoClicked().getLocation().getChunk());
            } else if (e.getCurrentItem().getType() == Material.LIME_DYE) {
                Land land = new Land(e.getWhoClicked().getLocation().getChunk());
                if (land.isOwner(e.getWhoClicked().getUniqueId())) {
                    land.setFlag(Flag.getFlag(e.getCurrentItem().getItemMeta().getLocalizedName()), false);
                }
                new LandMenu(e.getInventory()).getLandManage(e.getWhoClicked().getLocation().getChunk());
            } else if (e.getCurrentItem().getType() == Material.RED_DYE) {
                Land land = new Land(e.getWhoClicked().getLocation().getChunk());
                if (land.isOwner(e.getWhoClicked().getUniqueId())) {
                    land.setFlag(Flag.getFlag(e.getCurrentItem().getItemMeta().getLocalizedName()), true);
                }
                new LandMenu(e.getInventory()).getLandManage(e.getWhoClicked().getLocation().getChunk());
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7Manage member")) {
                new LandMenu(e.getInventory()).getLandManageMember(1, e.getWhoClicked().getLocation().getChunk());
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7Manage bans")) {
                new LandMenu(e.getInventory()).getLandManageBan(1, e.getWhoClicked().getLocation().getChunk());
            } else if (e.getCurrentItem().isSimilar(new ItemBuilder(Material.PAPER).setDisplayname("§cGo Back to ManageMenu").getItemStack())) {
                new LandMenu(e.getInventory()).getLandManage(e.getWhoClicked().getLocation().getChunk());
            } else if (e.getCurrentItem().isSimilar(new ItemBuilder(Material.RED_CONCRETE_POWDER).setDisplayname("§cAbbruch").getItemStack())) {
                new LandMenu(e.getInventory()).getLandManage(e.getWhoClicked().getLocation().getChunk());
            } else if (e.getCurrentItem().isSimilar(new ItemBuilder(Material.GREEN_CONCRETE).setDisplayname("§aadd §7Ban").getItemStack())) {
                //* Banadd
                //give sign or anvil to enter name (for now first do chat)
                Chunk chunk = e.getWhoClicked().getLocation().getChunk();
                e.getWhoClicked().closeInventory();

            } else if (e.getCurrentItem().isSimilar(new ItemBuilder(Material.GREEN_CONCRETE).setDisplayname("§aadd §7Member").getItemStack())) {
                //* Memberadd
                //TODO give sign or anvil to enter name (for now first do chat)
                Chunk chunk = e.getWhoClicked().getLocation().getChunk();
                e.getWhoClicked().closeInventory();

            } else if(e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                if(e.getCurrentItem().getItemMeta().getDisplayName().startsWith("§8> ")) {
                    //* Memberrem
                    new LandMenu(e.getInventory()).getConfirmMemberRemove(e.getCurrentItem().getItemMeta().getLocalizedName());
                }else if(e.getCurrentItem().getItemMeta().getDisplayName().startsWith("§c> ")) {
                    //* Banrem
                    new LandMenu(e.getInventory()).getConfirmBanRemove(e.getCurrentItem().getItemMeta().getLocalizedName());
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        ininv.remove(e.getPlayer());
    }

}
