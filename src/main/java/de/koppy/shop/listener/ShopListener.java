package de.koppy.shop.listener;

import java.util.ArrayList;
import java.util.HashMap;

import de.koppy.basics.api.PlayerProfile;
import de.koppy.economy.api.PlayerAccount;
import de.koppy.land.api.Land;
import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.shop.api.Adminshop;
import de.koppy.shop.api.ClientEditSignEvent;
import de.koppy.shop.api.ShopItem;
import de.koppy.shop.api.ShopSign;
import de.koppy.shop.commands.Shop;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.*;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ShopListener implements Listener {

    public static final String PREFIX = "§8[§3Shop§8] §r§7";
    public static final double SHOP_SIGN_PRICE = 500d;

    ArrayList<Player> inbulkinv = new ArrayList<Player>();
    public static HashMap<Location, Location> signchestlocs = new HashMap<Location, Location>();
    HashMap<Player, Location> inedit = new HashMap<Player, Location>();

    @EventHandler
    public void inSignEdit(PlayerQuitEvent e) {
        if(inedit.containsKey(e.getPlayer())) {
            Location location = inedit.get(e.getPlayer());
            signchestlocs.remove(location);
            inedit.remove(e.getPlayer());
        }
    }

    @EventHandler
    public void onSignEdit(ClientEditSignEvent e) {
        if(e.getLine(0).startsWith("#")) {
            //* PlayerAccount
            String puid = e.getLine(0).replace("#", "");
            PlayerProfile profile = PlayerProfile.getProfile(e.getPlayer().getUniqueId());
            if(!puid.equals(profile.getUID())) { printErr(e.getBlock(), e.getPlayer(), "§cThats not your name."); return; }
            if(!e.getLine(1).matches("[0-9]+")) { printErr(e.getBlock(), e.getPlayer(), "§cAmount must be an full number."); return; }
            double[] prices = checkPrice(e.getLine(2));
            double buyprice = prices[0];
            double sellprice = prices[1];
            if(buyprice <= 0 && sellprice <= 0) { printErr(e.getBlock(), e.getPlayer(), "§cAt least one must be greater zero."); return; }
            PlayerAccount pa = new PlayerAccount(e.getPlayer().getUniqueId());
            if(pa.getMoney() < SHOP_SIGN_PRICE) { printErr(e.getBlock(), e.getPlayer(), "§cYou dont have enough money for a ShopSign"); return; }
            ItemStack istack;
            if(e.getLine(3).equals("?")) {
                istack = getFirstItem(((Container) signchestlocs.get(e.getSign().getLocation()).getBlock().getState()).getInventory());
            }else {
                istack = new ItemStack(Material.valueOf(e.getLine(3)));
            }
            if(istack == null) { printErr(e.getBlock(), e.getPlayer(), "§cInvalid ItemStack."); return; }

            e.getSign().setEditable(true);
            e.getSign().setLine(0, "§e#§d"+puid);
            e.getSign().setLine(1, "§7Anzahl: "+Integer.parseInt(e.getLine(1)));
            String b = "";
            if(buyprice > 0) {
                b = b + "§a▲§e "+buyprice;
                if(sellprice > 0) b = b + " §7|§e "+sellprice+" §c▼";
            }else {
                if(sellprice > 0) b = b + "§c▼§e "+sellprice;
            }
            e.getSign().setLine(2, b);
            e.getSign().setLine(3, "§7"+istack.getType().toString());
            e.getSign().update();
            inedit.remove(e.getPlayer());
            pa.removeMoney(SHOP_SIGN_PRICE, "Server", "Shopsign created/edited");
        }
    }

    private void printErr(Block block, Player player, String err) {
        player.sendMessage(PREFIX + err);
        player.playSound(player, Sound.ENTITY_ITEM_BREAK, 10, 0);
        block.breakNaturally();
        signchestlocs.remove(block.getLocation());
    }

    @EventHandler
    public void onClickSign(PlayerInteractEvent e) {
        if(e.getClickedBlock() == null) return;
        if(!(e.getClickedBlock().getState() instanceof Sign)) return;
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK || e.getAction() != Action.LEFT_CLICK_BLOCK) return;
        Sign sign = (Sign) e.getClickedBlock().getState();
        if(sign.getSide(Side.FRONT).getLine(0).startsWith("§e#")) {
            String line1 = sign.getSide(Side.FRONT).getLine(0);
            String line2 = sign.getSide(Side.FRONT).getLine(1);
            String line3 = sign.getSide(Side.FRONT).getLine(2);
            String line4 = sign.getSide(Side.FRONT).getLine(3);
            //* PlayerInteract with ShopSign
            String puid = line1.replace("§e#§d","");
            if(puid.equals(PlayerProfile.getProfile(e.getPlayer().getUniqueId()).getUID())) {
                //* Owner interacts
                sign.setEditable(true);
                ShopSign shopSign = new ShopSign(sign.getSide(Side.FRONT).getLines());
                sign.getSide(Side.FRONT).setLine(0, "#"+shopSign.getPuid());
                sign.getSide(Side.FRONT).setLine(1, ""+shopSign.getAmount());
                sign.getSide(Side.FRONT).setLine(2, "buy: "+(int) shopSign.getBuyprice()+",sell: "+(int) shopSign.getSellprice());
                sign.getSide(Side.FRONT).setLine(3, shopSign.getLastline());
                sign.update();

                inedit.put(e.getPlayer(), sign.getLocation());
                Bukkit.getScheduler().runTaskLater(LunaniaSystem.getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        e.getPlayer().openSign(sign);
                    }
                }, 2);
            }else {
                //* Buyer kaufen hier

            }
        }
    }

    @EventHandler
    public void onPlaceEvent(BlockPlaceEvent e) {
        if(e.getBlockAgainst() == null) return;
        if(e.getBlock() == null) return;
        if(!e.getPlayer().isSneaking()) return;
        if(e.getBlock().getState() instanceof Sign) {
            if (e.getBlockAgainst().getState() instanceof Chest || e.getBlockAgainst().getState() instanceof Barrel || e.getBlockAgainst().getState() instanceof DoubleChest) {
                if(signchestlocs.containsValue(e.getBlockAgainst().getLocation())) return;
                if(!new Land(e.getBlockAgainst().getChunk()).isOwner(e.getPlayer().getUniqueId())) return;
                if(!e.getBlockAgainst().getChunk().equals(e.getBlock().getChunk())) return;
                e.setCancelled(true);
                signchestlocs.put(e.getBlock().getLocation(), e.getBlockAgainst().getLocation());
                e.getBlockReplacedState().setBlockData(e.getBlock().getBlockData());
                Sign sign = (Sign) e.getBlock().getState();
                sign.setEditable(true);
                sign.setLine(0, "#"+new PlayerProfile(e.getPlayer().getUniqueId()).getUID());
                sign.setLine(1, "1");
                sign.setLine(2, "buy: 1,sell: 1");
                sign.setLine(3, "?");
                sign.setEditable(true);
                sign.update();

                inedit.put(e.getPlayer(), sign.getLocation());
                Bukkit.getScheduler().runTaskLater(LunaniaSystem.getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        e.getPlayer().openSign(sign);
                    }
                }, 2);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreakEvent(BlockBreakEvent e) {
        if(signchestlocs.containsValue(e.getBlock().getLocation())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(PREFIX + "§cCant break Shopchest!");
            return;
        }

        if(signchestlocs.containsKey(e.getBlock().getLocation())) {
            //* Breaking sign
            if(!e.getPlayer().isSneaking()) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(PREFIX + "§cYou must sneak to break ShopSigns!");
                return;
            }
            if(((Sign) e.getBlock().getState()).getSide(Side.FRONT).getLine(0).substring(5).equals(PlayerProfile.getProfile(e.getPlayer().getUniqueId()).getUID())) {
                signchestlocs.remove(e.getBlock().getLocation());
            }else {
                e.setCancelled(true);
                e.getPlayer().sendMessage(PREFIX + "§cCant break others ShopSign!");
                return;
            }
        }
    }

    private ItemStack getFirstItem(Inventory inventory) {
        for(int i=0; i<inventory.getSize(); i++) {
            if(inventory.getItem(i) != null) return inventory.getItem(i).clone();
        }
        return null;
    }

    public double[] checkPrice(String line) {
        double buyprice = -1d;
        double sellprice = -1d;
        line = line.replace(" ", "");
        if(line.contains(",")) {
            String left = line.split(",")[0];
            String right = line.split(",")[1];

            if(left.split(":")[0].equals("B") || left.split(":")[0].equals("Buy") || left.split(":")[0].equals("buy")) {
                String num = left.split(":")[1];
                if(num.matches("[0-9]+")) {
                    buyprice = Double.valueOf(num);
                }
            }
            if(right.split(":")[0].equals("S") || right.split(":")[0].equals("Sell") || right.split(":")[0].equals("sell")) {
                String num = right.split(":")[1];
                if(num.matches("[0-9]+")) {
                    sellprice = Double.valueOf(num);
                }
            }

        }
        double[] out = {buyprice, sellprice};
        return out;
    }

    @EventHandler
    public void onInvClick(InventoryCloseEvent e) {
        if(Shop.ininv.contains(e.getPlayer())) {
            Inventory inventory = e.getInventory();
            Adminshop shop = Adminshop.getAdminshop(e.getView().getTitle().replace(" edit", ""));
            for(int i=0; i<inventory.getSize(); i++) {
                if(inventory.getItem(i) != null) {
                    ShopItem si = shop.getShopItembyPosition(i);
                    si.setItem(inventory.getItem(i));
                }else {
                    ShopItem si = shop.getShopItembyPosition(i);
                    si.setItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
                }
            }
            Shop.ininv.remove(e.getPlayer());
            e.getPlayer().sendMessage("§aShop successfully edited.");
        }else if(Shop.inshop.contains(e.getPlayer())) {
            Shop.inshop.remove(e.getPlayer());
        }else if(inbulkinv.contains(e.getPlayer())) {
            inbulkinv.remove(e.getPlayer());
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if(inbulkinv.contains(e.getWhoClicked())) {
            e.setCancelled(true);
            if(e.getCurrentItem() != null) {
                if(e.getCurrentItem().getType() == Material.GREEN_CONCRETE_POWDER) {
                    String shopname = e.getInventory().getItem(13).getItemMeta().getLocalizedName();
                    Adminshop shop = Adminshop.getAdminshop(shopname);
                    ShopItem si = shop.getShopItembyPosition(Integer.valueOf(e.getView().getTitle().split(" ")[0]));
                    int amount = e.getCurrentItem().getAmount();
                    double buyprice = si.getBuyPriceperpiece();
                    if(buyprice > 0) {
                        PlayerAccount pa = new PlayerAccount(e.getWhoClicked().getUniqueId());
                        if(pa.getMoney() >= buyprice) {
                            pa.removeMoney(buyprice, "Server", "bought an item");
                            ItemStack item = si.getItem().clone();
                            item.setAmount(amount);
                            e.getWhoClicked().getInventory().addItem(item);
                        }else {
                            e.getWhoClicked().sendMessage("§cYou dont have enough money!");
                        }
                    }else {
                        e.getWhoClicked().sendMessage("§cThis Item is not for sale.");
                    }
                }else if(e.getCurrentItem().getType() == Material.RED_CONCRETE_POWDER) {
                    String shopname = e.getInventory().getItem(13).getItemMeta().getLocalizedName();
                    Adminshop shop = Adminshop.getAdminshop(shopname);
                    ShopItem si = shop.getShopItembyPosition(Integer.valueOf(e.getView().getTitle().split(" ")[0]));
                    int amount = e.getCurrentItem().getAmount();
                    double sellprice = si.getSellPriceperpiece();
                    if(sellprice > 0) {
                        if(hasEnoughItems(si.getItem(), amount, e.getWhoClicked().getInventory())) {

                            removeItems(si.getItem(), amount, e.getWhoClicked().getInventory());
                            PlayerAccount pa = new PlayerAccount(e.getWhoClicked().getUniqueId());
                            pa.addMoney(sellprice, "Server", "sold item/s");
                            e.getWhoClicked().sendMessage("§cYouve sold those items.");

                        }else {
                            e.getWhoClicked().sendMessage("§cYou dont have enough of this item.");
                        }
                    }else {
                        e.getWhoClicked().sendMessage("§cThis Item is not sellable here.");
                    }
                }
            }
        }else if(Shop.inshop.contains(e.getWhoClicked())) {
            e.setCancelled(true);
            if(e.getCurrentItem() != null) {
                Player p = (Player) e.getWhoClicked();
                if(e.getCurrentItem().getItemMeta().getLore().get(0).equals("§d")) {
                    //* Directbuy
                    String shopname = e.getCurrentItem().getItemMeta().getLocalizedName();
                    Adminshop shop = Adminshop.getAdminshop(shopname);
                    ShopItem si = shop.getShopItembyPosition(e.getSlot());
                    double buyprice = si.getBuyPriceperpiece();
                    double sellprice = si.getSellPriceperpiece();
                    if(e.getClick() == ClickType.RIGHT || e.getClick() == ClickType.SHIFT_RIGHT) {
                        if(buyprice > 0) {
                            PlayerAccount pa = new PlayerAccount(p.getUniqueId());
                            if(pa.getMoney() >= buyprice) {
                                pa.removeMoney(buyprice, "Server", "bought an item");
                                p.getInventory().addItem(si.getItem().clone());
                            }else {
                                p.sendMessage("§cYou dont have enough money!");
                            }
                        }else {
                            p.sendMessage("§cThis Item is not for sale.");
                        }
                    }else if(e.getClick() == ClickType.LEFT || e.getClick() == ClickType.SHIFT_LEFT) {
                        if(sellprice > 0) {
                            if(hasEnoughItems(si.getItem(), si.getItem().getAmount(), p.getInventory())) {

                                removeItems(si.getItem(), si.getItem().getAmount(), p.getInventory());
                                PlayerAccount pa = new PlayerAccount(p.getUniqueId());
                                pa.addMoney(sellprice, "Server", "sold item/s");
                                p.sendMessage("§cYouve sold those items.");

                            }else {
                                p.sendMessage("§cYou dont have enough of this item.");
                            }
                        }else {
                            p.sendMessage("§cThis Item is not sellable here.");
                        }
                    }
                }else if(e.getCurrentItem().getItemMeta().getLore().get(0).equals("§b")) {
                    //* Bulkbuy
                    String shopname = e.getCurrentItem().getItemMeta().getLocalizedName();
                    Adminshop shop = Adminshop.getAdminshop(shopname);
                    ShopItem si = shop.getShopItembyPosition(e.getSlot());
                    p.closeInventory();
                    inbulkinv.add(p);
                    p.openInventory(si.getInventory(e.getSlot()));
                }
            }
        }
    }

    public boolean hasEnoughItems(ItemStack istack, int amount, Inventory inventory) {
        istack = istack.clone();
        istack.setAmount(1);
        for(int i=0; i<inventory.getSize(); i++) {
            if(inventory.getItem(i) != null) {
                istack.setAmount(inventory.getItem(i).getAmount());
                if(istack.isSimilar(inventory.getItem(i))) {
                    amount = amount - istack.getAmount();
                    if(amount <= 0) return true;
                }
            }
        }
        return false;
    }

    public void removeItems(ItemStack istack, int amount, Inventory inventory) {
        istack = istack.clone();
        istack.setAmount(1);
        for(int i=0; i<inventory.getSize(); i++) {
            if(inventory.getItem(i) != null) {
                istack.setAmount(inventory.getItem(i).getAmount());
                if(istack.isSimilar(inventory.getItem(i))) {
                    if(amount > inventory.getItem(i).getAmount()) {
                        inventory.setItem(i, null);
                        amount = amount - istack.getAmount();
                    }else if(amount == inventory.getItem(i).getAmount()) {
                        inventory.setItem(i, null);
                        return;
                    }else if(amount < inventory.getItem(i).getAmount()) {
                        int newamount = inventory.getItem(i).getAmount() - amount;
                        inventory.getItem(i).setAmount(newamount);
                        return;
                    }
                }
            }
        }
    }

}