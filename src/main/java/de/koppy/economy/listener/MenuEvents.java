package de.koppy.economy.listener;

import de.koppy.basics.api.InventoryHelper;
import de.koppy.economy.api.BankAccount;
import de.koppy.economy.api.BankMenu;
import de.koppy.economy.api.PlayerAccount;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.ArrayList;

public class MenuEvents implements Listener {

    public static ArrayList<Player> ininv = new ArrayList<>();

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(ininv.contains(e.getWhoClicked())) {
            e.setCancelled(true);
            if(e.getCurrentItem() == null) return;
            if(e.getCurrentItem().getType() == Material.OAK_HANGING_SIGN && !e.getCurrentItem().getItemMeta().getDisplayName().startsWith("§8§l§ §3§l")) {
                //* Clicked a bankaccount
                new BankMenu(e.getWhoClicked().getUniqueId()).getMenu(e.getInventory(), 1, 1, e.getCurrentItem().getItemMeta().getLocalizedName());
            }else if(InventoryHelper.isArrowLogs(e.getCurrentItem())) {
                int site = InventoryHelper.getArrowPage(e.getCurrentItem());
                BankMenu bankMenu = new BankMenu(e.getWhoClicked().getUniqueId());
                String activebankaccount = bankMenu.findAccount(e.getInventory());
                bankMenu.setLog(e.getInventory(), activebankaccount, site);
            }else if(InventoryHelper.isArrowMember(e.getCurrentItem())) {
                int site = InventoryHelper.getArrowPage(e.getCurrentItem());
                BankMenu bankMenu = new BankMenu(e.getWhoClicked().getUniqueId());
                String activebankaccount = bankMenu.findAccount(e.getInventory());
                bankMenu.setMember(e.getInventory(), activebankaccount, site);
            }else if(InventoryHelper.isEcoaccount(e.getCurrentItem())) {
                BankMenu bankMenu = new BankMenu(e.getWhoClicked().getUniqueId());
                bankMenu.getMenu(e.getInventory(), 1, 1, "ecolog-");
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        ininv.remove(e.getPlayer());
    }

}
