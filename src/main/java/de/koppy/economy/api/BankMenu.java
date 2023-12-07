package de.koppy.economy.api;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class BankMenu {

    private UUID uuid;
    public BankMenu(UUID uuid) {
        this.uuid = uuid;
    }

    public Inventory getStartMenu() {
        Inventory inventory = Bukkit.createInventory(null, 9*6, "§3"+Bukkit.getOfflinePlayer(uuid).getName()+" Account");

        getMenu(inventory, 1, 1);

        return inventory;
    }

    private void getMenu(Inventory inventory, int sitelog, int sitemember) {

        inventory.clear();
        ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta glassM = glass.getItemMeta();
        glassM.setDisplayName("§3");
        glass.setItemMeta(glassM);
        List<String> bankaccounts = new PlayerAccount(uuid).getBankaccounts();
        for(int i=0; i<inventory.getSize(); i++) inventory.setItem(i, glass);

        String acc = findAccount(inventory);
        setAccounts(inventory, acc, bankaccounts);

        if(acc != null) {
            //* add content for member and log of account

        }

    }

    private void setAccounts(Inventory inventory, String activeaccunt, List<String> bankaccounts) {
        if(activeaccunt != null) {
            ItemStack empty = new ItemStack(Material.BARRIER);
            ItemMeta emptyM = empty.getItemMeta();
            emptyM.setDisplayName("§cEmpty Slot");
            empty.setItemMeta(emptyM);

            if(bankaccounts.get(0) != null) {
                ItemStack bankacc = new ItemStack(Material.OAK_HANGING_SIGN);
                ItemMeta bankaccM = bankacc.getItemMeta();
                if(activeaccunt.equals(bankaccounts.get(0))) bankaccM.setDisplayName("§8§l§ §3§l");
                else bankaccM.setDisplayName("§8§ §3"+bankaccounts.get(0));
                bankacc.setItemMeta(bankaccM);
                inventory.setItem(9, bankacc);
            }else {
                inventory.setItem(9, empty);
            }

            if(bankaccounts.get(1) != null) {
                ItemStack bankacc = new ItemStack(Material.OAK_HANGING_SIGN);
                ItemMeta bankaccM = bankacc.getItemMeta();
                if(activeaccunt.equals(bankaccounts.get(1))) bankaccM.setDisplayName("§8§l§ §3§l");
                else bankaccM.setDisplayName("§8§ §3"+bankaccounts.get(1));
                bankacc.setItemMeta(bankaccM);
                inventory.setItem(9+9, bankacc);
            }else {
                inventory.setItem(9+9, empty);
            }

            if(bankaccounts.get(2) != null) {
                ItemStack bankacc = new ItemStack(Material.OAK_HANGING_SIGN);
                ItemMeta bankaccM = bankacc.getItemMeta();
                if(activeaccunt.equals(bankaccounts.get(2))) bankaccM.setDisplayName("§8§l§ §3§l");
                else bankaccM.setDisplayName("§8§ §3"+bankaccounts.get(2));
                bankacc.setItemMeta(bankaccM);
                inventory.setItem(9+9+9, bankacc);
            }else {
                inventory.setItem(9+9+9, empty);
            }
        }else {
            ItemStack empty = new ItemStack(Material.BARRIER);
            ItemMeta emptyM = empty.getItemMeta();
            emptyM.setDisplayName("§cEmpty Slot");
            empty.setItemMeta(emptyM);

            if(bankaccounts.get(0) != null) {
                ItemStack bankacc = new ItemStack(Material.OAK_HANGING_SIGN);
                ItemMeta bankaccM = bankacc.getItemMeta();
                bankaccM.setDisplayName("§8§ §3"+bankaccounts.get(0));
                bankacc.setItemMeta(bankaccM);
                inventory.setItem(9, bankacc);
            }else {
                inventory.setItem(9, empty);
            }

            if(bankaccounts.get(1) != null) {
                ItemStack bankacc = new ItemStack(Material.OAK_HANGING_SIGN);
                ItemMeta bankaccM = bankacc.getItemMeta();
                bankaccM.setDisplayName("§8§ §3"+bankaccounts.get(1));
                bankacc.setItemMeta(bankaccM);
                inventory.setItem(9+9, bankacc);
            }else {
                inventory.setItem(9+9, empty);
            }

            if(bankaccounts.get(2) != null) {
                ItemStack bankacc = new ItemStack(Material.OAK_HANGING_SIGN);
                ItemMeta bankaccM = bankacc.getItemMeta();
                bankaccM.setDisplayName("§8§ §3"+bankaccounts.get(2));
                bankacc.setItemMeta(bankaccM);
                inventory.setItem(9+9+9, bankacc);
            }else {
                inventory.setItem(9+9+9, empty);
            }
        }
    }

    private String findAccount(Inventory inventory) {
        if(inventory.getItem(9) != null) {
            if(inventory.getItem(9).getItemMeta().getDisplayName().startsWith("§8§l§ §3§l")) {
                return inventory.getItem(9).getItemMeta().getDisplayName().replace("§8§l§ §3§l", "");
            }
        }
        if(inventory.getItem(18) != null) {
            if(inventory.getItem(18).getItemMeta().getDisplayName().startsWith("§8§l§ §3§l")) {
                return inventory.getItem(18).getItemMeta().getDisplayName().replace("§8§l§ §3§l", "");
            }
        }
        if(inventory.getItem(27) != null) {
            if(inventory.getItem(27).getItemMeta().getDisplayName().startsWith("§8§l§ §3§l")) {
                return inventory.getItem(27).getItemMeta().getDisplayName().replace("§8§l§ §3§l", "");
            }
        }
        return null;
    }

    public void getDefault(Inventory inventory) {
        inventory.clear();

        ItemStack istack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta istackM = istack.getItemMeta();
        istackM.setDisplayName(" ");
        istack.setItemMeta(istackM);

        for(int i = 0; i<inventory.getSize(); i++) {
            inventory.setItem(i, istack);
        }

        PlayerAccount pa = new PlayerAccount(uuid);
        List<String> accounts =  pa.getBankaccounts();

        if(accounts.get(0) != null) {
            String acc1 = accounts.get(0);
            ItemStack istack1 = new ItemStack(Material.OAK_HANGING_SIGN);
            ItemMeta istack1M = istack1.getItemMeta();
            istack1M.setDisplayName("§8§ §3"+acc1);
            istack1.setItemMeta(istack1M);
            inventory.setItem(9, istack1);
        }
        if(accounts.get(1) != null) {
            String acc2 = accounts.get(1);
            ItemStack istack2 = new ItemStack(Material.OAK_HANGING_SIGN);
            ItemMeta istack2M = istack2.getItemMeta();
            istack2M.setDisplayName("§8§ §3"+acc2);
            istack2.setItemMeta(istack2M);
            inventory.setItem(9+9, istack2);
        }
        if(accounts.get(2) != null) {
            String acc3 = accounts.get(2);
            ItemStack istack3 = new ItemStack(Material.OAK_HANGING_SIGN);
            ItemMeta istack3M = istack3.getItemMeta();
            istack3M.setDisplayName("§8§ §3"+acc3);
            istack3.setItemMeta(istack3M);
            inventory.setItem(9+9+9, istack3);
        }



    }

}