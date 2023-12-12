package de.koppy.economy.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import de.koppy.basics.api.InventoryHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.checkerframework.checker.units.qual.A;


public class BankMenu {

    private UUID uuid;
    public BankMenu(UUID uuid) {
        this.uuid = uuid;
    }

    public Inventory getStartMenu() {
        Inventory inventory = Bukkit.createInventory(null, 9*6, "§3"+Bukkit.getOfflinePlayer(uuid).getName()+" Account");
        getMenu(inventory, 1, 1, null);
        return inventory;
    }

    public void getMenu(Inventory inventory, int sitelog, int sitemember, String activebankaccount) {
        if(activebankaccount != null && activebankaccount.equalsIgnoreCase("ecolog-")) {
            getMenuEcoaccount(inventory, sitelog);
            return;
        }
        inventory.clear();
        InventoryHelper.setInventoryGlassPane(inventory);
        setAccounts(inventory, activebankaccount, new PlayerAccount(uuid).getBankaccounts());
        setLog(inventory, activebankaccount, sitelog);
        setMember(inventory, activebankaccount, sitemember);
        setEcoaccount(inventory, false);
    }

    private void getMenuEcoaccount(Inventory inventory, int sitelog) {
        inventory.clear();
        InventoryHelper.setInventoryGlassPane(inventory);
        setAccounts(inventory, null, new PlayerAccount(uuid).getBankaccounts());
        setEcoaccount(inventory, true);
        setLog(inventory, "ecolog-", sitelog);
    }

    private void setEcoaccount(Inventory inventory, boolean active) {
        ItemStack istack = new ItemStack(Material.SPRUCE_HANGING_SIGN);
        ItemMeta istackM = istack.getItemMeta();
        if(active) istackM.setDisplayName("§2§lOwn Account");
        else istackM.setDisplayName("§2Own Account");
        istack.setItemMeta(istackM);
        inventory.setItem(36, istack);
    }

    private void setAccounts(Inventory inventory, String activeaccunt, List<String> bankaccounts) {
        if(activeaccunt != null) {
            ItemStack empty = new ItemStack(Material.BARRIER);
            ItemMeta emptyM = empty.getItemMeta();
            emptyM.setDisplayName("§cEmpty Slot");
            empty.setItemMeta(emptyM);

            if(!bankaccounts.isEmpty()) {
                ItemStack bankacc = new ItemStack(Material.OAK_HANGING_SIGN);
                ItemMeta bankaccM = bankacc.getItemMeta();
                bankaccM.setLocalizedName(bankaccounts.get(0));
                if(activeaccunt.equals(bankaccounts.get(0))) bankaccM.setDisplayName("§8§l§» §3§l"+bankaccounts.get(0));
                else bankaccM.setDisplayName("§8§ §3"+bankaccounts.get(0));
                bankacc.setItemMeta(bankaccM);
                inventory.setItem(9, bankacc);
            }else {
                inventory.setItem(9, empty);
            }

            if(bankaccounts.size() >= 2) {
                ItemStack bankacc = new ItemStack(Material.OAK_HANGING_SIGN);
                ItemMeta bankaccM = bankacc.getItemMeta();
                bankaccM.setLocalizedName(bankaccounts.get(1));
                if(activeaccunt.equals(bankaccounts.get(1))) bankaccM.setDisplayName("§8§l§» §3§l"+bankaccounts.get(1));
                else bankaccM.setDisplayName("§8§ §3"+bankaccounts.get(1));
                bankacc.setItemMeta(bankaccM);
                inventory.setItem(9+9, bankacc);
            }else {
                inventory.setItem(9+9, empty);
            }

            if(bankaccounts.size() >= 3) {
                ItemStack bankacc = new ItemStack(Material.OAK_HANGING_SIGN);
                ItemMeta bankaccM = bankacc.getItemMeta();
                bankaccM.setLocalizedName(bankaccounts.get(2));
                if(activeaccunt.equals(bankaccounts.get(2))) bankaccM.setDisplayName("§8§l§» §3§l"+bankaccounts.get(2));
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

            if(bankaccounts.size() >= 1) {
                ItemStack bankacc = new ItemStack(Material.OAK_HANGING_SIGN);
                ItemMeta bankaccM = bankacc.getItemMeta();
                bankaccM.setLocalizedName(bankaccounts.get(0));
                bankaccM.setDisplayName("§8§ §3"+bankaccounts.get(0));
                bankacc.setItemMeta(bankaccM);
                inventory.setItem(9, bankacc);
            }else {
                inventory.setItem(9, empty);
            }

            if(bankaccounts.size() >= 2) {
                ItemStack bankacc = new ItemStack(Material.OAK_HANGING_SIGN);
                ItemMeta bankaccM = bankacc.getItemMeta();
                bankaccM.setLocalizedName(bankaccounts.get(1));
                bankaccM.setDisplayName("§8§ §3"+bankaccounts.get(1));
                bankacc.setItemMeta(bankaccM);
                inventory.setItem(9+9, bankacc);
            }else {
                inventory.setItem(9+9, empty);
            }

            if(bankaccounts.size() >= 3) {
                ItemStack bankacc = new ItemStack(Material.OAK_HANGING_SIGN);
                ItemMeta bankaccM = bankacc.getItemMeta();
                bankaccM.setLocalizedName(bankaccounts.get(2));
                bankaccM.setDisplayName("§8§ §3"+bankaccounts.get(2));
                bankacc.setItemMeta(bankaccM);
                inventory.setItem(9+9+9, bankacc);
            }else {
                inventory.setItem(9+9+9, empty);
            }
        }
    }

    public void setLog(Inventory inventory, String bankaccount, int site) {
        if(bankaccount != null && bankaccount.equalsIgnoreCase("ecolog-")) {
            for(int i=11; i<11+6; i++) inventory.setItem(i, null);
            for(int i=20; i<20+6; i++) inventory.setItem(i, null);
            for(int i=29; i<29+6; i++) inventory.setItem(i, null);
            List<Log> logs = new PlayerAccount(uuid).getLogs();
            int maxlogpage = logs.size() / 18;
            if(logs.size() % 18 != 0) maxlogpage = maxlogpage+1;
            if(maxlogpage == 0) maxlogpage = 1;
            if(site > maxlogpage) site = maxlogpage;

            int next = site+1;
            int before = site-1;
            ItemStack siteright = InventoryHelper.getArrowLogs(true, next);
            ItemStack siteleft = InventoryHelper.getArrowLogs(false, before);
            if(site > 1) inventory.setItem(17, siteleft);
            else inventory.setItem(17, InventoryHelper.getEmptyGlass(Material.GRAY_STAINED_GLASS_PANE));
            if(site < maxlogpage) inventory.setItem(26+9, siteright);
            else inventory.setItem(26+9, InventoryHelper.getEmptyGlass(Material.GRAY_STAINED_GLASS_PANE));
            int beginindex = ((site-1)*18);
            int endindex = beginindex+(18);
            for(int i=11; i<11+6; i++) inventory.setItem(i, null);
            for(int i=20; i<20+6; i++) inventory.setItem(i, null);
            for(int i=beginindex; i<endindex; i++) {
                if(i >= logs.size() || i < 0) continue;
                ItemStack istack = new ItemStack(Material.PAPER);
                ItemMeta istackM = istack.getItemMeta();
                istackM.setDisplayName("§3§n"+logs.get(i).getDate().toLocaleString());
                ArrayList<String> lore = new ArrayList<>();
                lore.add(" ");
                lore.add("§7Amount: " + logs.get(i).getAmount());
                lore.add("§7Reason: " + logs.get(i).getReason());
                String name = logs.get(i).getSentfrom();
                if(name.length() == UUID.randomUUID().toString().length()) name = Bukkit.getOfflinePlayer(UUID.fromString(name)).getName();
                lore.add("§7By: §8" + name);
                istackM.setLore(lore);
                istackM.setLocalizedName(""+Math.random());
                istack.setItemMeta(istackM);
                inventory.addItem(istack);
            }

            return;
        }
        BankAccount account = new BankAccount(bankaccount);
        if(account.existName()) {
            List<BankLog> logs = account.getLogs();
            int maxlogpage = logs.size() / 12;
            if(logs.size() % 12 != 0) maxlogpage = maxlogpage+1;
            if(maxlogpage == 0) maxlogpage = 1;
            if(site > maxlogpage) site = maxlogpage;

            int next = site+1;
            int before = site-1;
            ItemStack siteright = InventoryHelper.getArrowLogs(true, next);
            ItemStack siteleft = InventoryHelper.getArrowLogs(false, before);
            if(site > 1) inventory.setItem(17, siteleft);
            else inventory.setItem(17, InventoryHelper.getEmptyGlass(Material.GRAY_STAINED_GLASS_PANE));
            if(site < maxlogpage) inventory.setItem(26, siteright);
            else inventory.setItem(26, InventoryHelper.getEmptyGlass(Material.GRAY_STAINED_GLASS_PANE));
            int beginindex = ((site-1)*12);
            int endindex = beginindex+(12);
            for(int i=11; i<11+6; i++) inventory.setItem(i, null);
            for(int i=20; i<20+6; i++) inventory.setItem(i, null);
            for(int i=beginindex; i<endindex; i++) {
                if(i >= logs.size() || i < 0) continue;
                ItemStack istack = new ItemStack(Material.PAPER);
                ItemMeta istackM = istack.getItemMeta();
                istackM.setDisplayName("§3§n"+logs.get(i).getDate().toLocaleString());
                ArrayList<String> lore = new ArrayList<>();
                lore.add(" ");
                lore.add("§7Content: " + logs.get(i).getContent());
                String name = logs.get(i).getDoneby();
                if(name.length() == UUID.randomUUID().toString().length()) name = Bukkit.getOfflinePlayer(UUID.fromString(name)).getName();
                lore.add("§7By: §8" + name);
                istackM.setLore(lore);
                istackM.setLocalizedName(""+Math.random());
                istack.setItemMeta(istackM);
                inventory.addItem(istack);
            }
        }
    }

    public void setMember(Inventory inventory, String bankaccount, int site) {
        BankAccount account = new BankAccount(bankaccount);
        if(account.existName()) {
            List<String> members = account.getMember();
            int maxlogpage = members.size() / 5;
            if(members.size() % 5 != 0) maxlogpage++;
            if(site > maxlogpage) site = maxlogpage;

            int next = site+1;
            int before = site-1;
            ItemStack siteright = InventoryHelper.getArrowMember(true, next);
            ItemStack siteleft = InventoryHelper.getArrowMember(false, before);
            if(site > 1) inventory.setItem(38, siteleft);
            else inventory.setItem(38, InventoryHelper.getEmptyGlass(Material.GRAY_STAINED_GLASS_PANE));
            if(site < maxlogpage) inventory.setItem(38+6, siteright);
            else inventory.setItem(38+6, InventoryHelper.getEmptyGlass(Material.GRAY_STAINED_GLASS_PANE));

            int beginindex = ((site-1)*5);
            int endindex = beginindex+(5);
            for(int i=39; i<39+5; i++) inventory.setItem(i, null);
            int istart = 39;
            for(int i=beginindex; i<endindex; i++) {
                if(i >= members.size()) continue;
                ItemStack istack = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta istackM = (SkullMeta) istack.getItemMeta();
                String name = Bukkit.getOfflinePlayer(UUID.fromString(members.get(i))).getName();
                istackM.setDisplayName("§7"+name);
                istackM.setOwner(name);
                istack.setItemMeta(istackM);
                inventory.setItem(istart, istack);
                istart++;
            }

        }
    }

    public String findAccount(Inventory inventory) {
        if(inventory.getItem(9) != null) {
            if(inventory.getItem(9).getItemMeta().getDisplayName().startsWith("§8§l§» §3§l")) {
                return inventory.getItem(9).getItemMeta().getDisplayName().replace("§8§l§» §3§l", "");
            }
        }
        if(inventory.getItem(18) != null) {
            if(inventory.getItem(18).getItemMeta().getDisplayName().startsWith("§8§l§» §3§l")) {
                return inventory.getItem(18).getItemMeta().getDisplayName().replace("§8§l§» §3§l", "");
            }
        }
        if(inventory.getItem(27) != null) {
            if(inventory.getItem(27).getItemMeta().getDisplayName().startsWith("§8§l§» §3§l")) {
                return inventory.getItem(27).getItemMeta().getDisplayName().replace("§8§l§» §3§l", "");
            }
        }
        if(inventory.getItem(36).getType() == Material.SPRUCE_HANGING_SIGN) {
            return "ecolog-";
        }
        return null;
    }

}