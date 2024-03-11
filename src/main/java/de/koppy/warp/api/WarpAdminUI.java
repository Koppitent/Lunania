package de.koppy.warp.api;

import de.koppy.basics.api.ItemBuilder;
import de.koppy.lunaniasystem.api.UI;
import de.koppy.warp.commands.Warp;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class WarpAdminUI extends UI {

    private boolean inspectWarp = false;
    private int curpage = 1;
    public WarpAdminUI() {
        super("§cWarp Admin UI", 9*5);
    }

    public void getPage(int page) {
        inspectWarp = false;
        curpage = page;
        inventory.clear();
        setGlassRand(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayname("§d").getItemStack());
        //* 21 pro Seite
        List<String> warplist = Warp.getWarpList(false);
        int maxpage = warplist.size() / 21;
        if(warplist.size() % 21 != 0) maxpage++;
        if(page > maxpage) page = maxpage;
        page--;
        for(int i=0+(page*21); i<21+(page*20); i++) {
            if(warplist.size() == i) break;
            WarpManager warp = new WarpManager(warplist.get(i));
            inventory.addItem(new ItemBuilder(warp.getMaterial()).setDisplayname("§8> §3" + warplist.get(i)).addLore("§7Owner: §8"+ Bukkit.getOfflinePlayer(warp.getOwner()).getName())
                    .addLore("§7Server: §8" + warp.getServer()).addLore("§7Beschreibung: §8"+warp.getMessage()).setLocalizedName(warplist.get(i)).getItemStack());
        }
        if(page+1 != maxpage) inventory.setItem(inventory.getSize()-3, new ItemBuilder(Material.PLAYER_HEAD).setSkull("MHF_ArrowRight").setDisplayname("§3Next Site").setLocalizedName(""+(page+2)).getItemStack());
        if(page != 0) inventory.setItem(inventory.getSize()-7, new ItemBuilder(Material.PLAYER_HEAD).setSkull("MHF_ArrowLeft").setDisplayname("§3Last Site").setLocalizedName(""+(page)).getItemStack());
    }

    public boolean isInspectWarp() {
        return inspectWarp;
    }

    public int getCurpage() {
        return curpage;
    }

    public void openWarp(String warpname) {
        WarpManager warp = new WarpManager(warpname);
        if(!warp.existWarp()) return;
        inspectWarp = true;
        inventory.clear();
        fillGlass(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayname("§d").getItemStack());

        inventory.setItem(22, new ItemBuilder(warp.getMaterial()).setDisplayname("§3§l" + warpname).addLore("§7Owner: §8"+ Bukkit.getOfflinePlayer(warp.getOwner()).getName())
                .addLore("§7Server: §8" + warp.getServer()).addLore("§7Beschreibung: §8"+warp.getMessage()).setLocalizedName(warpname).getItemStack());
        inventory.setItem(20, new ItemBuilder(Material.ENDER_PEARL).setDisplayname("§7Teleport to Warp").getItemStack());
        inventory.setItem(24, new ItemBuilder(Material.GREEN_CONCRETE_POWDER).setDisplayname("§aAccept Warp").getItemStack());
        inventory.setItem(25, new ItemBuilder(Material.RED_CONCRETE_POWDER).setDisplayname("§cDecline Warp").getItemStack());

        inventory.setItem(inventory.getSize()-8, new ItemBuilder(Material.PLAYER_HEAD).setSkull("MHF_ArrowLeft").setDisplayname("§cBack").getItemStack());
    }

}