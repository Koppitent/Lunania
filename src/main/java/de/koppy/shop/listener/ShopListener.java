package de.koppy.shop.listener;

import java.util.ArrayList;
import java.util.HashMap;

import de.koppy.basics.api.PlayerProfile;
import de.koppy.economy.api.PlayerAccount;
import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.shop.api.Adminshop;
import de.koppy.shop.api.ShopItem;
import de.koppy.shop.commands.Shop;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Barrel;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket;

public class ShopListener implements Listener {

    public static String prefix = "§8[§3Shop§8] §r§7";

    ArrayList<Player> inbulkinv = new ArrayList<Player>();
    HashMap<Location, Location> signlocs = new HashMap<Location, Location>();

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        if(e.getSide() == Side.FRONT) {
            if(e.getLine(0).equalsIgnoreCase("#"+e.getPlayer().getName())) {
                if(e.getLine(1).matches("[0-9]+")) {
//					int amount = Integer.valueOf(e.getLine(1));
                    double[] buysellprice = checkPrice(e.getLine(2), e.getPlayer());
                    double buyprice = buysellprice[0];
                    double sellprice = buysellprice[1];
                    if(buyprice > 0 || sellprice > 0) {



                    }else {
                        e.getPlayer().sendMessage(prefix + "§cYou need to provide buy or sell (line 3)");
                        e.getPlayer().playSound(e.getPlayer(), Sound.ENTITY_ITEM_BREAK, 10, 0);
                        e.getBlock().breakNaturally();
                    }
                }else {
                    e.getPlayer().sendMessage(prefix + "§cInvalid amount. (line 2)");
                    e.getPlayer().playSound(e.getPlayer(), Sound.ENTITY_ITEM_BREAK, 10, 0);
                    e.getBlock().breakNaturally();
                }
            }
        }
    }

//    @EventHandler
//    public void onPlaceSign(final BlockPlaceEvent e) {
//        if(e.getBlock().getState() instanceof Sign) {
//            if(e.getBlockAgainst().getState() instanceof Chest || e.getBlockAgainst().getState() instanceof Barrel) {
//                if(signlocs.containsValue(e.getBlockAgainst().getLocation()) == false) {
//                    e.setCancelled(true);
//                    e.getBlockReplacedState().setBlockData(e.getBlock().getBlockData());
//                    String pid = PlayerProfile.getProfile(e.getPlayer().getUniqueId()).getUID();
//                    final Sign sign  = (Sign) e.getBlock().getState();
//                    SignSide side = sign.getSide(Side.FRONT);
//                    side.setLine(0, "#"+pid);
//                    side.setLine(1, "1");
//                    side.setLine(2, "B: 1,S: 1");
//                    side.setLine(3, "?");
//                    sign.update();
//
//                    Bukkit.getScheduler().runTaskLater(LunaniaSystem.getPlugin(), new Runnable() {
//                        public void run() {
//                            Location loc = e.getBlock().getLocation();
//                            BlockPos blockposition = new BlockPos((int) loc.getX(), (int) loc.getY(), (int) loc.getZ());
//                            ClientboundOpenSignEditorPacket editor = new ClientboundOpenSignEditorPacket(blockposition, true);
//                            ((CraftPlayer) e.getPlayer()).getHandle().connection.send(editor);
//                        }
//                    }, 2);
//
//                }else {
//                    e.setCancelled(true);
//                    e.getPlayer().sendMessage(prefix + "§cDiese Kiste ist bereits ein UserShop!");
//                }
//            }
//        }
//    }


    public double[] checkPrice(String line, Player player) {
        double buyprice = -1d;
        double sellprice = -1d;
        if(line.contains(",")) {
            String left = line.split(",")[0];
            String right = line.split(",")[1];

            if(left.split(" ")[0].equals("B:") || left.split(" ")[0].equals("Buy:") || left.split(" ")[0].equals("buy:")) {
                String num = left.split(" ")[1];
                num = num.replace(",", ".");
                if(num.matches("[0.0-9.9]+")) {
                    buyprice = Double.valueOf(num);
                }
            }else if(right.split(" ")[0].equals("S:") || right.split(" ")[0].equals("Sell:") || right.split(" ")[0].equals("sell:")) {
                String num = right.split(" ")[1];
                num = num.replace(",", ".");
                if(num.matches("[0.0-9.9]+")) {
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
            e.getPlayer().sendMessage("§aShop sucessfully edited.");
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