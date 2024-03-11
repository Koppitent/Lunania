package de.koppy.land.listener;

import de.koppy.basics.api.ItemBuilder;
import de.koppy.basics.api.PlayerProfile;
import de.koppy.land.LandSystem;
import de.koppy.land.api.*;
import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.lunaniasystem.api.UI;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.data.type.GlassPane;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.UUID;

public class InventoryEvents implements Listener {
    public static ArrayList<Player> typenameinchatmember = new ArrayList<>();
    public static ArrayList<Player> typenameinchatban = new ArrayList<>();

    public void uwu() {

    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (typenameinchatban.contains(e.getPlayer())) {
            e.setCancelled(true);
            typenameinchatban.remove(e.getPlayer());
            Land land = new Land((int) e.getPlayer().getLocation().getX() / 16, (int) e.getPlayer().getLocation().getZ() / 16);
            if (!land.isOwner(e.getPlayer().getUniqueId())) return;
            String name = e.getMessage().split(" ")[0];
            OfflinePlayer op = Bukkit.getOfflinePlayer(name);
            if (op.hasPlayedBefore()) {
                LandUI landUI = new LandUI(e.getPlayer().getUniqueId(), land.getChunk());
                landUI.getConfirmBanAdd(op.getUniqueId().toString());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        e.getPlayer().openInventory(landUI.getInventory());
                    }
                }.runTaskLater(LunaniaSystem.getPlugin(), 10);
            } else {
                PlayerProfile.getProfile(e.getPlayer().getUniqueId()).sendMessage(LandSystem.PREFIX, "playerneveronline", new String[]{"%player%", name});
            }
        } else if (typenameinchatmember.contains(e.getPlayer())) {
            e.setCancelled(true);
            typenameinchatmember.remove(e.getPlayer());
            Land land = new Land((int) e.getPlayer().getLocation().getX() / 16, (int) e.getPlayer().getLocation().getZ() / 16);
            if (!land.isOwner(e.getPlayer().getUniqueId())) return;
            String name = e.getMessage().split(" ")[0];
            OfflinePlayer op = Bukkit.getOfflinePlayer(name);
            if (op.hasPlayedBefore()) {
                LandUI landUI = new LandUI(e.getPlayer().getUniqueId(), land.getChunk());
                landUI.getConfirmMemberAdd(op.getUniqueId().toString());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        e.getPlayer().openInventory(landUI.getInventory());
                    }
                }.runTaskLater(LunaniaSystem.getPlugin(), 10);
            } else {
                PlayerProfile.getProfile(e.getPlayer().getUniqueId()).sendMessage(LandSystem.PREFIX, "playerneveronline", new String[]{"%player%", name});
            }
        }
    }

    @EventHandler
    public void onInventory(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;
        if (e.getCurrentItem() instanceof GlassPane) return;
        if (UI.inventories.containsKey(e.getInventory())) {
            UI ui = UI.inventories.get(e.getInventory());
            if (ui instanceof LandUI) {
                LandUI landui = (LandUI) ui;
                if (landui.getPage().equalsIgnoreCase("main")) {
                    if (e.getSlot() == 10) {
                        landui.getLandList(1);
                    } else if (e.getSlot() == 13) {
                        landui.getLandManage();
                    } else if (e.getSlot() == 16) {
                        landui.getListHelp();
                    }
                } else if (landui.getPage().equalsIgnoreCase("help")) {
                    if (e.getSlot() == 19) {
                        landui.getMainPage();
                    }
                } else if (landui.getPage().equalsIgnoreCase("landlist")) {
                    if (e.getSlot() == 19) {
                        landui.getMainPage();
                    } else if (e.getSlot() == 26) {
                        landui.nextPageLandList();
                    } else if (e.getSlot() == 18) {
                        landui.previousPageLandList();
                    }
                } else if (landui.getPage().equalsIgnoreCase("manageland")) {
                    if (e.getSlot() == 19) {
                        landui.getMainPage();
                        return;
                    }
                    Land land = new Land((int) e.getWhoClicked().getLocation().getX() / 16, (int) e.getWhoClicked().getLocation().getZ() / 16);
                    if (!land.isOwner(e.getWhoClicked().getUniqueId())) return;
                    if (e.getSlot() == 8 || e.getSlot() == 8 + 9 || e.getSlot() == 8 + 9 + 9) {
                        if (e.getCurrentItem().getType() == Material.LIME_DYE) {
                            land.setFlag(Flag.getFlag(e.getCurrentItem().getItemMeta().getLocalizedName()), false);
                            landui.getLandManage();
                        } else if (e.getCurrentItem().getType() == Material.RED_DYE) {
                            land.setFlag(Flag.getFlag(e.getCurrentItem().getItemMeta().getLocalizedName()), true);
                            landui.getLandManage();
                        }
                    } else if (e.getSlot() == 12) {
                        landui.getLandManageMember(1);
                    } else if (e.getSlot() == 14) {
                        landui.getLandManageBan(1);
                    }
                } else if (landui.getPage().equalsIgnoreCase("manageban")) {
                    if (e.getSlot() == 19) {
                        landui.getLandManage();
                        return;
                    }
                    if (e.getSlot() == 26) {
                        landui.getNextManageBan();
                    } else if (e.getSlot() == 18) {
                        landui.getPreviousManageBan();
                    } else if (e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                        landui.getConfirmBanRemove(e.getCurrentItem().getItemMeta().getLocalizedName());
                    } else if (e.getSlot() == 25) {
                        e.getWhoClicked().closeInventory();
                        e.getWhoClicked().sendMessage("§7Please type the name into the chat.");
                        typenameinchatban.add((Player) e.getWhoClicked());
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (typenameinchatban.contains((Player) e.getWhoClicked())) {
                                    typenameinchatban.remove((Player) e.getWhoClicked());
                                    e.getWhoClicked().sendMessage(LandSystem.PREFIX + "§cZeit abgelaufen.");
                                }
                            }
                        }.runTaskLater(LunaniaSystem.getPlugin(), 20*30);
                    }
                } else if (landui.getPage().equalsIgnoreCase("managemember")) {
                    if (e.getSlot() == 19) {
                        landui.getLandManage();
                        return;
                    }
                    if (e.getSlot() == 26) {
                        landui.getNextManageMember();
                    } else if (e.getSlot() == 18) {
                        landui.getPreviousManageMember();
                    } else if (e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                        landui.getConfirmMemberRemove(e.getCurrentItem().getItemMeta().getLocalizedName());
                    } else if (e.getSlot() == 25) {
                        e.getWhoClicked().closeInventory();
                        e.getWhoClicked().sendMessage("§7Please type the name into the chat.");
                        typenameinchatmember.add((Player) e.getWhoClicked());
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (typenameinchatmember.contains((Player) e.getWhoClicked())) {
                                    typenameinchatmember.remove((Player) e.getWhoClicked());
                                    e.getWhoClicked().sendMessage(LandSystem.PREFIX + "§cZeit abgelaufen.");
                                }
                            }
                        }.runTaskLater(LunaniaSystem.getPlugin(), 20*30);
                    }
                } else if (landui.getPage().equalsIgnoreCase("memberremove")) {
                    if (e.getSlot() == 11) {
                        e.getWhoClicked().closeInventory();
                        Land land = new Land((int) e.getWhoClicked().getLocation().getX() / 16, (int) e.getWhoClicked().getLocation().getZ() / 16);
                        if (!land.isOwner(e.getWhoClicked().getUniqueId())) {
                            e.getWhoClicked().sendMessage(LandSystem.PREFIX + "§cYour not the owner of that land.");
                        }else {
                            land.removeMember(UUID.fromString(e.getCurrentItem().getItemMeta().getLocalizedName()));
                            e.getWhoClicked().sendMessage(LandSystem.PREFIX + "§7Player removed.");
                        }
                    } else if (e.getSlot() == 15) {
                        e.getWhoClicked().closeInventory();
                        e.getWhoClicked().sendMessage(LandSystem.PREFIX + "§cMission Abbruch!");
                    }
                }else if (landui.getPage().equalsIgnoreCase("memberadd")) {
                    if (e.getSlot() == 11) {
                        e.getWhoClicked().closeInventory();
                        Land land = new Land((int) e.getWhoClicked().getLocation().getX() / 16, (int) e.getWhoClicked().getLocation().getZ() / 16);
                        if (!land.isOwner(e.getWhoClicked().getUniqueId())) {
                            e.getWhoClicked().sendMessage(LandSystem.PREFIX + "§cYour not the owner of that land.");
                        }else {
                            land.addMember(UUID.fromString(e.getCurrentItem().getItemMeta().getLocalizedName()));
                            e.getWhoClicked().sendMessage(LandSystem.PREFIX + "§7Player added.");
                        }
                    } else if (e.getSlot() == 15) {
                        e.getWhoClicked().closeInventory();
                        e.getWhoClicked().sendMessage(LandSystem.PREFIX + "§cMission Abbruch!");
                    }
                }else if (landui.getPage().equalsIgnoreCase("banadd")) {
                    if (e.getSlot() == 11) {
                        e.getWhoClicked().closeInventory();
                        Land land = new Land((int) e.getWhoClicked().getLocation().getX() / 16, (int) e.getWhoClicked().getLocation().getZ() / 16);
                        if (!land.isOwner(e.getWhoClicked().getUniqueId())) {
                            e.getWhoClicked().sendMessage(LandSystem.PREFIX + "§cYour not the owner of that land.");
                        }else {
                            land.addBanned(UUID.fromString(e.getCurrentItem().getItemMeta().getLocalizedName()));
                            e.getWhoClicked().sendMessage(LandSystem.PREFIX + "§7Ban added.");
                        }
                    } else if (e.getSlot() == 15) {
                        e.getWhoClicked().closeInventory();
                        e.getWhoClicked().sendMessage(LandSystem.PREFIX + "§cMission Abbruch!");
                    }
                }else if (landui.getPage().equalsIgnoreCase("banremove")) {
                    if (e.getSlot() == 11) {
                        e.getWhoClicked().closeInventory();
                        Land land = new Land((int) e.getWhoClicked().getLocation().getX() / 16, (int) e.getWhoClicked().getLocation().getZ() / 16);
                        if (!land.isOwner(e.getWhoClicked().getUniqueId())) {
                            e.getWhoClicked().sendMessage(LandSystem.PREFIX + "§cYour not the owner of that land.");
                        }else {
                            land.removeBanned(UUID.fromString(e.getCurrentItem().getItemMeta().getLocalizedName()));
                            e.getWhoClicked().sendMessage(LandSystem.PREFIX + "§7Ban removed.");
                        }
                    } else if (e.getSlot() == 15) {
                        e.getWhoClicked().closeInventory();
                        e.getWhoClicked().sendMessage(LandSystem.PREFIX + "§cMission Abbruch!");
                    }
                }
            }
        }
    }

}
