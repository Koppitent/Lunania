package de.koppy.land.api;

import de.koppy.basics.api.InventoryHelper;
import de.koppy.basics.api.ItemBuilder;
import de.koppy.basics.api.PlayerProfile;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.UUID;

//TODO landmenu

public class LandMenu extends InventoryHelper {
    private final Inventory inventory;
    public LandMenu(Inventory inventory) {
        this.inventory = inventory;
    }
    public LandMenu(String displayname) {
        this.inventory = new Menu(displayname, 3*9).getInventory();
    }

    public Inventory getMainPage() {
        inventory.clear();
        setInventoryGlassPane(inventory);
        inventory.setItem(10, new ItemBuilder(Material.MAP).setDisplayname("§2List of Lands").getItemStack());
        inventory.setItem(13, new ItemBuilder(Material.WOODEN_AXE).setDisplayname("§3Manage Lands").getItemStack());
        inventory.setItem(16, new ItemBuilder(Material.PAPER).setDisplayname("§eHelp §7for lands").getItemStack());
        return inventory;
    }

    public Inventory getListLands(int page, UUID uuid) {
        List<String> lands = PlayerProfile.getLands(uuid.toString());
        int landsize = lands.size();
        int maxpage = landsize / 18;
        if(landsize % 18 != 0) maxpage++;


        return inventory;
    }
}