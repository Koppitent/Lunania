package de.koppy.economy.api;

import de.koppy.basics.api.ItemBuilder;
import de.koppy.job.listener.Inventory;
import de.koppy.lunaniasystem.api.UI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.UUID;

public class EcoUI extends UI {

    private UUID uuid;

    public EcoUI(UUID uuid) {
        super(Bukkit.getOfflinePlayer(uuid).getName()+"'s ", 9*4);
    }

    public void getMenu() {
        inventory.clear();
        fillGlass(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayname("§c").getItemStack());
        inventory.setItem(10, new ItemBuilder(Material.GREEN_CONCRETE).setDisplayname("Your account").getItemStack());
    }
}