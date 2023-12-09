package de.koppy.basics.api;

import de.koppy.basics.commands.Head;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class InventoryHelper {

    public static ItemStack getArrow(boolean right, int site) {
        ItemStack arrow = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta arrowM = (SkullMeta) arrow.getItemMeta();
        if(right) arrowM.setOwner("MHF_arrowright");
        else arrowM.setOwner("MHF_arrowleft");
        arrowM.setDisplayName("§7Zu Seite "+site);
        arrowM.setLocalizedName(""+site);
        arrow.setItemMeta(arrowM);
        return arrow;
    }

    public static boolean isArrow(ItemStack istack) {
        if(istack.getType() == Material.PLAYER_HEAD) {
            if(istack.getItemMeta().getDisplayName().startsWith("§7Zu Seite")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEcoaccount(ItemStack stack) {
        ItemStack istack = new ItemStack(Material.SPRUCE_HANGING_SIGN);
        ItemMeta istackM = istack.getItemMeta();
        istackM.setDisplayName("§2Own Account");
        istack.setItemMeta(istackM);
        return stack.isSimilar(istack);
    }

    public static ItemStack getArrowLogs(boolean right, int site) {
        ItemStack arrow = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta arrowM = (SkullMeta) arrow.getItemMeta();
        if(right) arrowM.setOwner("MHF_arrowright");
        else arrowM.setOwner("MHF_arrowleft");
        arrowM.setDisplayName("§7Zu Log-Seite "+site);
        arrowM.setLocalizedName(""+site);
        arrow.setItemMeta(arrowM);
        return arrow;
    }

    public static boolean isArrowLogs(ItemStack istack) {
        if(istack.getType() == Material.PLAYER_HEAD) {
            if(istack.getItemMeta().getDisplayName().startsWith("§7Zu Log-Seite")) {
                return true;
            }
        }
        return false;
    }

    public static ItemStack getArrowMember(boolean right, int site) {
        ItemStack arrow = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta arrowM = (SkullMeta) arrow.getItemMeta();
        if(right) arrowM.setOwner("MHF_arrowright");
        else arrowM.setOwner("MHF_arrowleft");
        arrowM.setDisplayName("§7Zu Member-Seite "+site);
        arrowM.setLocalizedName(""+site);
        arrow.setItemMeta(arrowM);
        return arrow;
    }

    public static boolean isArrowMember(ItemStack istack) {
        if(istack.getType() == Material.PLAYER_HEAD) {
            if(istack.getItemMeta().getDisplayName().startsWith("§7Zu Member-Seite")) {
                return true;
            }
        }
        return false;
    }

    public static int getArrowPage(ItemStack istack) {
        if(istack.getType() == Material.PLAYER_HEAD) {
            return Integer.parseInt(istack.getItemMeta().getLocalizedName());
        }
        return 1;
    }

    public static void isHead(ItemStack istack, Player player) {
        if(istack.getType() == Material.PLAYER_HEAD) {
            player.getInventory().addItem(Head.getHead(istack.getItemMeta().getLocalizedName()));
        }
    }

    public static ItemStack getEmptyGlass(Material material) {
        ItemStack glass = new ItemStack(material);
        ItemMeta glassM = glass.getItemMeta();
        glassM.setDisplayName("§d");
        glass.setItemMeta(glassM);
        return glass;
    }

    public static void setRandGlassPane(Inventory inventory) {
        ItemStack glass = InventoryHelper.getEmptyGlass(Material.GRAY_STAINED_GLASS_PANE);
        for(int i=0; i<9; i++) {
            inventory.setItem(i, glass);
            inventory.setItem(inventory.getSize()-i-1, glass);
        }
        for(int i=0; i<inventory.getSize(); i=i+9) {
            inventory.setItem(i, glass);
            inventory.setItem(i+8, glass);
        }
    }

    public static void setInventoryGlassPane(Inventory inventory) {
        ItemStack glass = InventoryHelper.getEmptyGlass(Material.GRAY_STAINED_GLASS_PANE);
        for(int i=0; i<inventory.getSize(); i++) {
            inventory.setItem(i, glass);
            inventory.setItem(inventory.getSize()-i-1, glass);
        }
    }

}
