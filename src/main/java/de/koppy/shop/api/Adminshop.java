package de.koppy.shop.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class Adminshop {

    public static List<Adminshop> adminshops = new ArrayList<Adminshop>();

    private String name;
    private String title;
    private int rows;
    private HashMap<Integer, ShopItem> shopitems = new HashMap<Integer, ShopItem>();
    private ShopType shoptype;

    public Adminshop(String name, int rows) {
        this.name = name;
        this.shoptype = ShopType.DIRECTBUY;
        this.title = "default";
        this.rows = rows;
        setTitle("default");
        setRows(rows);
        adminshops.add(this);
        for(int i=0; i<rows*9; i++) {
            addShopItem(new ShopItem(this, "§3", "", new ItemStack(Material.GRAY_STAINED_GLASS_PANE), i));
        }
    }

    public ShopType getType() {
        return shoptype;
    }

    public void setShoptype(ShopType shoptype) {
        this.shoptype = shoptype;
        File file = new File("plugins/Lunania/Adminshops", name+".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set("shoptype", shoptype.toString());
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getRows() {
        return rows;
    }

    public String getTitle() {
        return title;
    }

    public Adminshop(File file) {
        this.name = file.getName().replace(".yml", "");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        this.rows = cfg.getInt("rows");
        this.title = cfg.getString("title");
        this.shoptype = ShopType.fromString(cfg.getString("shoptype"));
        if(cfg.contains("shopitems")) {
            for(int i=0; i<(rows*9); i++) {
                ShopItem shopitem = loadShopItem(i);
                if(shopitem != null) shopitems.put(i, shopitem);
            }
        }
        adminshops.add(this);
    }

    public boolean existFile() {
        File file = new File("plugins/Lunania/Adminshops", name+".yml");
        return file.exists();
    }

    public void setRows(int rows) {
        this.rows = rows;
        File file = new File("plugins/Lunania/Adminshops", name+".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set("rows", rows);
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTitle(String title) {
        this.title = title;
        File file = new File("plugins/Lunania/Adminshops", name+".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set("title", title);
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addShopItem(ShopItem shopitem) {
        this.shopitems.put(shopitem.getPosition(), shopitem);
        saveShopItem(shopitem);
    }

    public void removeShopItem(ShopItem shopitem) {
        this.shopitems.remove(shopitem.getPosition());
        File file = new File("plugins/Lunania/Adminshops", name+".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set("shopitems" + "."+shopitem.getPosition(), null);
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeShopItemByPosition(int position) {
        this.shopitems.remove(position);
        File file = new File("plugins/Lunania/Adminshops", name+".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set("shopitems" + "."+position, null);
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean existShopItembyPosition(int position) {
        return shopitems.get(position) != null;
    }

    public ShopItem getShopItembyPosition(int position) {
        return shopitems.get(position);
    }

    public void delete() {
        adminshops.remove(this);
        File file = new File("plugins/Lunania/Adminshops", name+".yml");
        file.delete();
    }

    /*
     * loading ShopItem at given position from save-file
     */
    private ShopItem loadShopItem(int position) {
        File file = new File("plugins/Lunania/Adminshops", name+".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        if(cfg.contains("shopitems" + "."+position)) {
            ItemStack item = cfg.getItemStack("shopitems" + "."+position + ".item");
            double buyprice = cfg.getDouble("shopitems" + "."+position + ".buyprice");
            double sellprice = cfg.getDouble("shopitems" + "."+position + ".sellprice");
            int amount1 = cfg.getInt("shopitems" + "."+position + ".amount1");
            int amount2 = cfg.getInt("shopitems" + "."+position + ".amount2");
            int amount3 = cfg.getInt("shopitems" + "."+position + ".amount3");
            String title = cfg.getString("shopitems" + "."+position + ".title");
            String description = cfg.getString("shopitems" + "."+position + ".description");
            ShopItem shopitem = new ShopItem(this, title, description, item, amount1, amount2, amount3, position);
            shopitem.setBuypriceperpiece(buyprice);
            shopitem.setSellpriceperpiece(sellprice);
            return shopitem;
        }else {
            return null;
        }
    }

    private void saveShopItem(ShopItem shopitem) {
        File file = new File("plugins/Lunania/Adminshops", name+".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set("shopitems" + "."+shopitem.getPosition() + ".item", shopitem.getItem());
        cfg.set("shopitems" + "."+shopitem.getPosition() + ".buyprice", shopitem.getBuyPriceperpiece());
        cfg.set("shopitems" + "."+shopitem.getPosition() + ".sellprice", shopitem.getSellPriceperpiece());
        cfg.set("shopitems" + "."+shopitem.getPosition() + ".amount1", shopitem.getAmount1());
        cfg.set("shopitems" + "."+shopitem.getPosition() + ".amount2", shopitem.getAmount2());
        cfg.set("shopitems" + "."+shopitem.getPosition() + ".amount3", shopitem.getAmount3());
        cfg.set("shopitems" + "."+shopitem.getPosition() + ".title", shopitem.getTitle());
        cfg.set("shopitems" + "."+shopitem.getPosition() + ".description", shopitem.getDescription());
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setBuyprice(double buyprice, int position) {
        if(shopitems.containsKey(position)) {
            ShopItem si = shopitems.get(position);
            si.setBuypriceperpiece(buyprice);
            File file = new File("plugins/Lunania/Adminshops", name+".yml");
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            cfg.set("shopitems" + "."+position + ".buyprice", buyprice);
            try {
                cfg.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setSellprice(double sellprice, int position) {
        if(shopitems.containsKey(position)) {
            ShopItem si = shopitems.get(position);
            si.setSellpriceperpiece(sellprice);
            File file = new File("plugins/Lunania/Adminshops", name+".yml");
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            cfg.set("shopitems" + "."+position + ".sellprice", sellprice);
            try {
                cfg.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getName() {
        return name;
    }

    public ShopItem getShopItem(ItemStack item) {
        for(ShopItem si : shopitems.values()) {
            if(si.getItem().isSimilar(item)) {
                return si;
            }
        }
        return null;
    }

    public static boolean existAdminshop(String name) {
        for(Adminshop ashop : adminshops) {
            if(ashop.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public static Adminshop getAdminshop(String name) {
        for(Adminshop ashop : adminshops) {
            if(ashop.getName().equalsIgnoreCase(name)) {
                return ashop;
            }
        }
        return null;
    }

}