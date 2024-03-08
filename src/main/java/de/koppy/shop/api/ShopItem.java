package de.koppy.shop.api;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import de.koppy.economy.EconomySystem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ShopItem {

    String title;
    Adminshop shop;
    String description;
    private ItemStack item;
    private double buypriceperpiece=0d;
    private double sellpriceperpiece=0d;
    private int amount1=1;
    private int amount2=32;
    private int amount3=64;
    private int position;

    public ShopItem(Adminshop shop, String title, String description, ItemStack item, int position) {
        this.title = title;
        this.description = description;
        if(item == null) {
            this.item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        }else {
            this.item = item.clone();
        }
        this.amount2 = item.getMaxStackSize()/2;
        this.amount3 = item.getMaxStackSize();
        this.position = position;
        this.shop = shop;
    }

    public ShopItem(Adminshop shop, String title, String description, ItemStack item, int amount1, int amount2, int amount3, int position) {
        this.title = title;
        this.description = description;
        if(item == null) {
            this.item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        }else {
            this.item = item.clone();
        }
        this.amount1 = amount1;
        this.amount2 = amount2;
        this.amount3 = amount3;
        this.position = position;
        this.shop = shop;
    }

    public Inventory getInventory(int slot) {
        Inventory inventory = Bukkit.createInventory(null, 3*9, slot + " " + getTitle());

        ItemStack istack = getShopItem().clone();
        istack.setAmount(1);

        ItemStack buy = new ItemStack(Material.GREEN_CONCRETE_POWDER);
        buy.setAmount(amount1);
        ItemStack buy2 = buy.clone();
        buy2.setAmount(amount2);
        ItemStack buy3 = buy.clone();
        buy3.setAmount(amount3);

        ItemStack sell = new ItemStack(Material.RED_CONCRETE_POWDER);
        sell.setAmount(amount1);
        ItemStack sell2 = sell.clone();
        sell2.setAmount(amount2);
        ItemStack sell3 = sell.clone();
        sell3.setAmount(amount3);

        inventory.setItem(10, buy3);
        inventory.setItem(11, buy2);
        inventory.setItem(12, buy);

        inventory.setItem(13, istack);

        inventory.setItem(14, sell);
        inventory.setItem(15, sell2);
        inventory.setItem(16, sell3);

        return inventory;
    }

    public double getBuyPriceperpiece() {
        return buypriceperpiece;
    }

    public int getPosition() {
        return position;
    }

    public double getSellPriceperpiece() {
        return sellpriceperpiece;
    }

    public int getAmount1() {
        return amount1;
    }

    public int getAmount2() {
        return amount2;
    }

    public int getAmount3() {
        return amount3;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setBuypriceperpiece(double buypriceperpiece) {
        this.buypriceperpiece = buypriceperpiece;
        saveShopItem();
    }

    public void setSellpriceperpiece(double sellpriceperpiece) {
        this.sellpriceperpiece = sellpriceperpiece;
        saveShopItem();
    }

    public void setTitle(String title) {
        this.title = title;
        saveShopItem();
    }

    public void setDescription(String description) {
        this.description = description;
        saveShopItem();
    }

    public ItemStack getShopItem() {
        ItemStack shopitem = item.clone();
        if(shopitem.getType() != Material.GRAY_STAINED_GLASS_PANE) {
            String currency = EconomySystem.getEcosymbol();
            ItemMeta shopitemM = shopitem.getItemMeta();
            shopitemM.setDisplayName(getTitle().replace("&", "§").replace("_", " "));
            shopitemM.setLocalizedName(shop.getName());
            ArrayList<String> lore = new ArrayList<String>();
            if(this.shop.getType() == ShopType.DIRECTBUY) {
                lore.add("§d");
                if(getBuyPriceperpiece() > 0) {
                    lore.add("§7Buy: §e" + new DecimalFormat("#.##").format(getBuyPriceperpiece()) + currency);
                }else {
                    lore.add("§7Buy: §c-/-");
                }
                if(getSellPriceperpiece() > 0) {
                    lore.add("§7Sell: §e" + new DecimalFormat("#.##").format(getSellPriceperpiece()) + currency);
                }else {
                    lore.add("§7Sell: §c-/-");
                }
                lore.add(" ");
                lore.add("§7Buying §7§o(Rightclick)");
                lore.add("§7Selling §7§o(Leftclick)");
                lore.add(" ");
                lore.add("§e"+getDescription());
                shopitemM.setLore(lore);
                shopitem.setItemMeta(shopitemM);
            }else {
                lore.add("§b");
                if(getBuyPriceperpiece() > 0) {
                    lore.add("§7Buyprice/stk: §e" + new DecimalFormat("#.##").format(getBuyPriceperpiece()) + currency);
                }else {
                    lore.add("§7Buyprice/stk: §c-/-");
                }
                if(getSellPriceperpiece() > 0) {
                    lore.add("§7Sellprice/stk: §e" + new DecimalFormat("#.##").format(getSellPriceperpiece()) + currency);
                }else {
                    lore.add("§7Sellprice/stk: §c-/-");
                }
                lore.add(" ");
                lore.add("§7Open menu §7§o(Rightclick)");
                lore.add(" ");
                lore.add("§e"+getDescription());
                shopitemM.setLore(lore);
                shopitem.setItemMeta(shopitemM);
                shopitem.setAmount(1);
            }
        }else {
            shopitem = null;
        }
        return shopitem;
    }

    public ItemStack getItem() {
        if(item.getType() == Material.GRAY_STAINED_GLASS_PANE) return null;
        return item.clone();
    }

    public void setItem(ItemStack item) {
        this.item = item.clone();
        saveShopItem();
    }

    public void saveShopItem() {
        File file = new File("plugins/Lunania/Adminshops", shop.getName()+".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set("shopitems" + "."+this.getPosition() + ".item", this.getItem());
        cfg.set("shopitems" + "."+this.getPosition() + ".buyprice", this.getBuyPriceperpiece());
        cfg.set("shopitems" + "."+this.getPosition() + ".sellprice", this.getSellPriceperpiece());
        cfg.set("shopitems" + "."+this.getPosition() + ".amount1", this.getAmount1());
        cfg.set("shopitems" + "."+this.getPosition() + ".amount2", this.getAmount2());
        cfg.set("shopitems" + "."+this.getPosition() + ".amount3", this.getAmount3());
        cfg.set("shopitems" + "."+this.getPosition() + ".title", this.getTitle());
        cfg.set("shopitems" + "."+this.getPosition() + ".description", this.getDescription());
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}