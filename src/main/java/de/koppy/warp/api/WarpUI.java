package de.koppy.warp.api;

import de.koppy.basics.api.ItemBuilder;
import de.koppy.lunaniasystem.api.UI;
import de.koppy.warp.commands.Warp;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.List;

public class WarpUI extends UI {


    public WarpUI() {
        super("§2§lWarps", 9*6);
    }

    public void getMainMenu() {
        inventory.clear();
        fillGlass(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayname("§d").getItemStack());
        inventory.setItem(10, new ItemBuilder(Material.GRASS_BLOCK).setDisplayname("§2§lFarmwelt").setLocalizedName("farmworld").addLore("§eteleports you to the farmworld.").getItemStack());
        inventory.setItem(15, new ItemBuilder(Material.NETHERRACK).setDisplayname("§c§lNether").setLocalizedName("nether").addLore("§eteleports you to the nether.").getItemStack());
        inventory.setItem(31, new ItemBuilder(Material.RECOVERY_COMPASS).setDisplayname("§3§lSpawn").setLocalizedName("spawn").addLore("§eteleports you to the spawn.").getItemStack());
        inventory.setItem(inventory.getSize()-1, new ItemBuilder(Material.SPRUCE_HANGING_SIGN).setDisplayname("§2UserWarps").addLore("§eopens a list of all UserWarps.").getItemStack());
    }

    public void getUserWarps(int page) {
        inventory.clear();
        setGlassRand(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayname("§d").getItemStack());
        //* 21 pro Seite
        List<String> warplist = Warp.getWarpList(true);
        int maxpage = warplist.size() / 21;
        if(warplist.size() % 21 != 0) maxpage++;
        if(page > maxpage) page = maxpage;
        page--;
        for(int i=0+(page*28); i<28+(page*27); i++) {
            if(warplist.size() == i) break;
            WarpManager warp = new WarpManager(warplist.get(i));
            inventory.addItem(new ItemBuilder(warp.getMaterial()).setDisplayname("§8> §3" + warplist.get(i)).addLore("§7Owner: §8"+ Bukkit.getOfflinePlayer(warp.getOwner()).getName())
                    .addLore("§7Server: §8" + warp.getServer()).setLocalizedName(warplist.get(i)).getItemStack());
        }
        if(page+1 != maxpage) inventory.setItem(inventory.getSize()-3, new ItemBuilder(Material.PLAYER_HEAD).setSkull("MHF_ArrowRight").setDisplayname("§3Next Site").setLocalizedName(""+(page+2)).getItemStack());
        if(page != 0) inventory.setItem(inventory.getSize()-7, new ItemBuilder(Material.PLAYER_HEAD).setSkull("MHF_ArrowLeft").setDisplayname("§3Last Site").setLocalizedName(""+(page)).getItemStack());
        inventory.setItem(inventory.getSize()-8, new ItemBuilder(Material.PLAYER_HEAD).setSkull("MHF_ArrowLeft").setDisplayname("§cBack").getItemStack());
    }

}