package de.koppy.npc.listener;

import de.koppy.basics.api.ItemBuilder;
import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.npc.api.*;
import de.koppy.npc.commands.NPC;
import de.koppy.shop.api.Adminshop;
import de.koppy.shop.commands.Shop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;

public class NpcEvents implements Listener {

    @EventHandler
    public void onJoin(PlayerMoveEvent e) {
        if(e.getFrom().getX() != e.getTo().getX() ||
                e.getFrom().getZ() != e.getTo().getZ() ||
                e.getFrom().getY() != e.getTo().getY()) {
            if(Npc.npcs.isEmpty()) return;
            for(Npc npc : Npc.npcs) {
                if(npc.getLocation().getWorld().getName().equals(e.getPlayer().getLocation().getWorld().getName())) {
                    if(npc.isLooking()) {
                        if(npc.getLocation().distance(e.getPlayer().getLocation()) < 8) {
                            npc.look(e.getPlayer());
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onNpc(NpcHitEvent e) {
        if(NPC.select.contains(e.getPlayer())) {
            NPC.select.remove(e.getPlayer());
            Npc.selectednpc.put(e.getPlayer(), e.getNpc());
            e.getPlayer().sendMessage("§7You selected the NPC " + e.getNpc().getName());
            return;
        }

        if(e.getNpc().getType() == NpcType.COMMAND) {
            String command = e.getNpc().getTypeContent();
            e.getPlayer().performCommand(command.replace("/", ""));
        }else if(e.getNpc().getType() == NpcType.ADMINSHOP) {
            String shopname = e.getNpc().getTypeContent();
            if(Adminshop.existAdminshop(shopname)) {
                Adminshop shop = Adminshop.getAdminshop(shopname);
                Inventory inventory = Bukkit.createInventory(null, shop.getRows()*9, shop.getTitle().replace("&", "§"));
                for(int i=0; i<inventory.getSize(); i++) {
                    if(shop.getShopItembyPosition(i) != null) {
                        inventory.setItem(i, shop.getShopItembyPosition(i).getShopItem());
                    }
                }
                for(int i=0; i<inventory.getSize(); i++) {
                    if(inventory.getItem(i) == null) {
                        inventory.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayname("§d").getItemStack());
                    }
                }
                Shop.inshop.add(e.getPlayer());
                e.getPlayer().openInventory(inventory);
            }
        }
    }

    @EventHandler
    public void onJoin(final PlayerTeleportEvent e) {
        if(!e.getTo().getWorld().getName().equals(e.getFrom().getWorld().getName())) {
            Bukkit.getScheduler().runTaskLater(LunaniaSystem.getPlugin(), new Runnable() {
                public void run() {
                    for(Npc npc : Npc.npcs) {
                        if(npc.getLocation().getWorld().getName().equals(e.getPlayer().getLocation().getWorld().getName())) {
                            npc.show(e.getPlayer());
                        }
                    }
                    if(e.getPlayer().hasPermission("server.admin.gamemode")) new Packet(e.getPlayer()).sendFakeOP();
                }
            }, 20);
        }
    }

    @EventHandler
    public void onJoinEvent(final PlayerRespawnEvent e) {
        if(e.getPlayer().hasPermission("server.admin.gamemode")) {
            Bukkit.getScheduler().runTaskLater(LunaniaSystem.getPlugin(), new Runnable() {
                public void run() {
                    new Packet(e.getPlayer()).sendFakeOP();
                }
            }, 20);
        }
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        new PacketReader(e.getPlayer()).inject();
        if(e.getPlayer().hasPermission("server.admin.gamemode")) {
            new Packet(e.getPlayer()).sendFakeOP();
        }
        Bukkit.getScheduler().runTaskLater(LunaniaSystem.getPlugin(), new Runnable() {
            public void run() {
                if(!Npc.npcs.isEmpty()) {
                    for(Npc npc : Npc.npcs) {
                        if(npc.getLocation().getWorld().getName().equals(e.getPlayer().getLocation().getWorld().getName())) {
                            npc.show(e.getPlayer());
                        }
                    }
                }
            }
        }, 20);
    }
}