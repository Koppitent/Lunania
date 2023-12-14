package de.koppy.shop.api;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ShopSign {

    private String puid;
    private double buyprice = 0;
    private double sellprice = 0;
    private int amount;
    private ItemStack itemStack;
    private String lastline;

    public ShopSign(String[] lines) {
        readSign(lines);
    }

    private void readSign(String[] lines) {

        this.puid = lines[0].replace("§e#§d", "");
        this.amount = Integer.parseInt(lines[1].replace("§7Anzahl: ", ""));
        String[] s = lines[2].split(" ");
        if(s.length == 5) {
            //* buy AND sell
            this.buyprice = Double.parseDouble(s[1]);
            this.sellprice = Double.parseDouble(s[3]);
        }else if(s.length == 2) {
           //* buy OR sell
            if(s[0].startsWith("§a▲§e")) {
                //* buy
                this.buyprice = Double.parseDouble(s[1]);
            }else {
                //* sell
                this.sellprice = Double.parseDouble(s[1]);
            }
        }
        this.itemStack = new ItemStack(Material.valueOf(lines[3].replace("§7", "")));
        this.lastline = lines[3];
    }

    public String getPuid() {
        return puid;
    }

    public double getBuyprice() {
        return buyprice;
    }

    public double getSellprice() {
        return sellprice;
    }

    public String getLastline() {
        return lastline;
    }

    public int getAmount() {
        return amount;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}