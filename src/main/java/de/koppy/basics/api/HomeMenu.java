package de.koppy.basics.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class HomeMenu {

    private UUID uuid;
    public HomeMenu(UUID uuid) {
        this.uuid = uuid;
    }

    public Inventory getMenu() {
        Inventory inventory = Bukkit.createInventory(null, 9*2, "§6Homes:");
        return updateMenu(inventory, 1);
    }

    @SuppressWarnings("deprecation")
    public Inventory updateMenu(Inventory inventory, int page) {
        List<Home> homes = PlayerProfile.getProfile(uuid).getHomes();
        int size = homes.size();
        int pages = size / 9;
        if(size % 9 != 0) pages++;

        inventory.clear();

        ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta glassM = glass.getItemMeta();
        glassM.setDisplayName("§4");
        glass.setItemMeta(glassM);

        for(int i=9; i<inventory.getSize(); i++) inventory.setItem(i, glass);

        ItemStack arrowl = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta arrowlM = (SkullMeta) arrowl.getItemMeta();
        arrowlM.setOwner("MHF_ArrowLeft");
        arrowlM.setDisplayName("§6To page "+(page-1));
        arrowlM.setLocalizedName(""+(page-1));
        arrowl.setItemMeta(arrowlM);

        ItemStack arrowr = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta arrowrM = (SkullMeta) arrowr.getItemMeta();
        arrowrM.setOwner("MHF_ArrowRight");
        arrowrM.setDisplayName("§6To page "+(page+1));
        arrowrM.setLocalizedName(""+(page+1));
        arrowr.setItemMeta(arrowrM);

        if(page < pages) inventory.setItem(17, arrowr);
        if(page > 1) inventory.setItem(9, arrowl);

        int begin = (page-1)*9;
        int end = (page*9)-1;

        for(int i=begin; i<(end+1); i++) {
            if(homes.size() > i) {
                inventory.addItem(getHomeItem(homes.get(i)));
            }
        }
        return inventory;
    }

    private ItemStack getHomeItem(Home home) {

        ItemStack istack = new ItemStack(Material.PAPER);
        ItemMeta istackM = istack.getItemMeta();
        istackM.setDisplayName("§8§ §3"+home.getName());
        istackM.setLocalizedName(home.getName());
        Random rndm = new Random();
        istackM.setCustomModelData(1010+rndm.nextInt(2));
        List<String> lore = new ArrayList<String>();
        lore.add("");
        lore.add("§eWorld: " + home.getLocation().getWorld().getName());
        lore.add("§eX: " + (int) home.getLocation().getX());
        lore.add("§eY: " + (int) home.getLocation().getY());
        lore.add("§eZ: " + (int) home.getLocation().getZ());
        istackM.setLore(lore);
        istack.setItemMeta(istackM);

        return istack;
    }
}