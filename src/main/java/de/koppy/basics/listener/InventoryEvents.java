package de.koppy.basics.listener;

import de.koppy.bansystem.BanSystem;
import de.koppy.bansystem.api.BanUI;
import de.koppy.basics.api.*;
import de.koppy.basics.commands.Changelog;
import de.koppy.basics.commands.Collect;
import de.koppy.basics.commands.Head;
import de.koppy.basics.commands.Home;
import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.lunaniasystem.api.UI;
import de.koppy.nick.NickSystem;
import de.koppy.nick.api.NickManager;
import de.koppy.nick.api.NickUI;
import de.koppy.nick.commands.Nick;
import de.koppy.quest.api.QuestUI;
import de.koppy.warp.api.WarpManager;
import de.koppy.warp.api.WarpUI;
import org.bukkit.Bukkit;
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
    public static ArrayList<Player> headinv = new ArrayList<Player>();

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;

        if(UI.inventories.containsKey(e.getInventory())) {
            UI ui = UI.inventories.get(e.getInventory());
            if(ui instanceof LanguageUI) {
                LanguageUI langui = (LanguageUI) ui;
                if(e.getCurrentItem().getType() == Material.PAPER) {
                    langui.select(Language.fromString(e.getCurrentItem().getItemMeta().getLocalizedName()));
                    e.getWhoClicked().closeInventory();
                }
            }else if(ui instanceof NickUI) {
                NickUI nickui = (NickUI) ui;
                if(e.getCurrentItem().getType() == Material.RED_CONCRETE) {
                    //* Nick randomly here
                    e.getWhoClicked().closeInventory();
                    PlayerProfile profile = PlayerProfile.getProfile(nickui.getUuid());
                    if(e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                        //* without Skin
                        NickManager nm = new NickManager(profile.getPlayer());
                        String name = nm.getRandomNickname();
                        nm.changeName(name);
                        profile.getPlayer().sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§7You got nicked!");
                    }else {
                        //* with Skin
                        NickManager nm = new NickManager(profile.getPlayer());
                        String name = nm.getRandomNickname();
                        nm.changeName(name);
                        String skin[] = nm.getRandomSkinData();
                        nm.changeSkin(skin[0], skin[1]);
                        profile.getPlayer().sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§7You got nicked with skin!");
                    }
                }else if(e.getCurrentItem().getType() == Material.GREEN_CONCRETE) {
                    e.getWhoClicked().closeInventory();
                    PlayerProfile profile = PlayerProfile.getProfile(nickui.getUuid());
                    if(!profile.isNicked()) return;
                    NickManager nm = new NickManager(Bukkit.getPlayer(nickui.getUuid()));
                    nm.unnick();
                    e.getWhoClicked().sendMessage(BanSystem.getPrefix() + "§cYou got unnicked.");
                }else if(e.getCurrentItem().getType() == Material.PAPER) {
                    e.getWhoClicked().closeInventory();
                    e.getWhoClicked().sendMessage("brah brah, oh junge!");
                }
            }else if(ui instanceof QuestUI) {
                if(e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                    QuestUI questui = (QuestUI) ui;
                    questui.getMenu(Integer.parseInt(e.getCurrentItem().getItemMeta().getLocalizedName()));
                }
            }else if(ui instanceof WarpUI) {
                WarpUI warpui = (WarpUI) ui;
                if(e.getSlot() == 46 && e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                    warpui.getMainMenu();
                }else if(e.getSlot() == 47 && e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                    int page = Integer.parseInt(e.getCurrentItem().getItemMeta().getLocalizedName());
                    warpui.getUserWarps(page);
                }else if(e.getSlot() == 51 && e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                    int page = Integer.parseInt(e.getCurrentItem().getItemMeta().getLocalizedName());
                    warpui.getUserWarps(page);
                }else if(e.getSlot() == 53 && e.getCurrentItem().getType() == Material.SPRUCE_HANGING_SIGN) {
                    warpui.getUserWarps(1);
                }else if(e.getCurrentItem().getType() != Material.BLACK_STAINED_GLASS_PANE) {
                    String warpname = e.getCurrentItem().getItemMeta().getLocalizedName();
                    e.getWhoClicked().closeInventory();
                    ((Player) e.getWhoClicked()).performCommand("warp " + warpname);
                }
            }
        }

        if (headinv.contains(e.getWhoClicked())) {
            e.setCancelled(true);
            if(InventoryHelper.isArrow(e.getCurrentItem())) {
                Head.setHeadMenu(InventoryHelper.getArrowPage(e.getCurrentItem()), e.getInventory());
            }else {
                InventoryHelper.isHead(e.getCurrentItem(), (Player) e.getWhoClicked());
            }
        }else if (ininv.contains(e.getWhoClicked())) {
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
        headinv.remove(e.getPlayer());
        if (Collect.inCollect.contains(e.getPlayer())) {
            Collect.inCollect.remove(e.getPlayer());
            new SavedItems(e.getPlayer().getUniqueId()).saveInventory(e.getInventory());
        }
    }

}
