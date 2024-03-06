package de.koppy.bansystem.listener;

import de.koppy.bansystem.BanSystem;
import de.koppy.bansystem.api.BanManager;
import de.koppy.bansystem.api.BanUI;
import de.koppy.bansystem.api.MuteManager;
import de.koppy.bansystem.api.MuteUI;
import de.koppy.lunaniasystem.api.UI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInv(InventoryClickEvent e) {
        if(e.getCurrentItem() == null) return;
        if(UI.inventories.containsKey(e.getInventory())) {
            e.setCancelled(true);
            UI ui = UI.inventories.get(e.getInventory());
            if(ui instanceof BanUI) {
                BanUI banui = (BanUI) ui;
                if(e.getCurrentItem().getType() == Material.ANVIL) {
                    ((BanUI) ui).setIdList();
                }else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cBack")) {
                    ((BanUI) ui).setMainMenu();
                }else if(e.getCurrentItem().getType() == Material.CLOCK) {
                    ((BanUI) ui).setTimer(1, 0);
                }else if(e.getCurrentItem().getItemMeta().getLocalizedName().startsWith("#")) {
                    BanManager bm = new BanManager(banui.getUuid());
                    bm.banID(Integer.parseInt(e.getCurrentItem().getItemMeta().getLocalizedName().replace("#", "")), banui.getBannedbyuuid());
                    e.getWhoClicked().closeInventory();
                    e.getWhoClicked().sendMessage(BanSystem.getPrefix() + "§7Der Spieler §e" + Bukkit.getOfflinePlayer(banui.getUuid()).getName() + " §7wurde für §3" + bm.getReason() + " §7gebannt.");
                }else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7Less Number") || e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7More Number")) {
                    int i = Integer.parseInt(e.getCurrentItem().getItemMeta().getLocalizedName());
                    if(i >= 1 && i <= 9) {
                        banui.setTimer(i, Integer.parseInt(e.getInventory().getItem(13+9).getItemMeta().getLocalizedName()));
                    }
                }else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7Voran") || e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7Danach")) {
                    int i = Integer.parseInt(e.getCurrentItem().getItemMeta().getLocalizedName());
                    if(i >= 0 && i <= 4) {
                        banui.setTimer(Integer.parseInt(e.getInventory().getItem(13).getItemMeta().getLocalizedName()), i);
                    }
                }else if(e.getCurrentItem().getType() == Material.GREEN_CONCRETE) {
                    int time = Integer.parseInt(e.getInventory().getItem(13).getItemMeta().getLocalizedName());
                    int type = Integer.parseInt(e.getInventory().getItem(13+9).getItemMeta().getLocalizedName());
                    e.getWhoClicked().closeInventory();
                    String t = "h";
                    switch (type) {
                        case 0:
                            t = "h";
                            break;
                        case 1:
                            t = "d";
                            break;
                        case 2:
                            t = "w";
                            break;
                        case 3:
                            t = "m";
                            break;
                        case 4:
                            t = "y";
                            break;
                    }
                    ((Player) e.getWhoClicked()).performCommand("ban " + Bukkit.getOfflinePlayer(banui.getUuid()).getName() + " "+time+t + " §cYou got banned.");
                }
            }else if(ui instanceof MuteUI) {
                MuteUI muteui = (MuteUI) ui;
                if(e.getCurrentItem().getType() == Material.ANVIL) {
                    muteui.setIdList();
                }else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cBack")) {
                    muteui.setMainMenu();
                }else if(e.getCurrentItem().getType() == Material.CLOCK) {
                    muteui.setTimer(1, 0);
                }else if(e.getCurrentItem().getItemMeta().getLocalizedName().startsWith("#")) {
                    MuteManager bm = new MuteManager(muteui.getUuid());
                    bm.muteID(Integer.parseInt(e.getCurrentItem().getItemMeta().getLocalizedName().replace("#", "")), muteui.getMutedbyuuid());
                    e.getWhoClicked().closeInventory();
                    e.getWhoClicked().sendMessage(BanSystem.getPrefix() + "§7Der Spieler §e" + Bukkit.getOfflinePlayer(muteui.getUuid()).getName() + " §7wurde für §3" + bm.getReason() + " §7gemuted.");
                }else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7Less Number") || e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7More Number")) {
                    int i = Integer.parseInt(e.getCurrentItem().getItemMeta().getLocalizedName());
                    if(i >= 1 && i <= 9) {
                        muteui.setTimer(i, Integer.parseInt(e.getInventory().getItem(13+9).getItemMeta().getLocalizedName()));
                    }
                }else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7Voran") || e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7Danach")) {
                    int i = Integer.parseInt(e.getCurrentItem().getItemMeta().getLocalizedName());
                    if(i >= 0 && i <= 4) {
                        muteui.setTimer(Integer.parseInt(e.getInventory().getItem(13).getItemMeta().getLocalizedName()), i);
                    }
                }else if(e.getCurrentItem().getType() == Material.GREEN_CONCRETE) {
                    int time = Integer.parseInt(e.getInventory().getItem(13).getItemMeta().getLocalizedName());
                    int type = Integer.parseInt(e.getInventory().getItem(13+9).getItemMeta().getLocalizedName());
                    e.getWhoClicked().closeInventory();
                    String t = "h";
                    switch (type) {
                        case 0:
                            t = "h";
                            break;
                        case 1:
                            t = "d";
                            break;
                        case 2:
                            t = "w";
                            break;
                        case 3:
                            t = "m";
                            break;
                        case 4:
                            t = "y";
                            break;
                    }
                    ((Player) e.getWhoClicked()).performCommand("mute " + Bukkit.getOfflinePlayer(muteui.getUuid()).getName() + " "+time+t + " §cYou got muted.");
                }
            }
        }
    }

    @EventHandler
    public void onInvCLose(InventoryCloseEvent e) {
        if(UI.inventories.containsKey(e.getInventory())) {
            UI ui = UI.inventories.get(e.getInventory());
            ui.closeInventory();
        }
    }

}
