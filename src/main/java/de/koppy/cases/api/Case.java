package de.koppy.cases.api;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.koppy.basics.api.PlayerProfile;
import de.koppy.cases.CaseSystem;
import de.koppy.cases.listener.CaseEvents;
import de.koppy.economy.EconomySystem;
import de.koppy.economy.api.PlayerAccount;
import de.koppy.lunaniasystem.LunaniaSystem;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

public class Case {

    public static ArrayList<Case> cases = new ArrayList<Case>();

    private String name;
    private Color color;
    private boolean needsKey;
    private List<CaseItem> caseitems = new ArrayList<CaseItem>();

    public Case(String name, CaseItem caseitem, boolean needskey) {
        this.name = name;
        this.needsKey = needskey;
        caseitems.add(caseitem);
        cases.add(this);
    }

    public boolean needsKey() {
        return needsKey;
    }

    public String getName() {
        return name;
    }

    private int getPrewviewRowsSize() {
        int items = caseitems.size();
        int rows = items/7;
        if(items % 7 != 0) rows++;
        rows = rows + 2;
        return rows;
    }

    private Inventory fillRand(Inventory inventory) {
        ItemStack istack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta istackM = istack.getItemMeta();
        istackM.setDisplayName("§d");
        istack.setItemMeta(istackM);

        for(int i=0; i<inventory.getSize(); i=i+9) {
            inventory.setItem(i, istack);
        }

        for(int i=8; i<inventory.getSize(); i=i+9) {
            inventory.setItem(i, istack);
        }

        for(int i=0; i<9; i++) {
            inventory.setItem(i, istack);
        }

        for(int i=inventory.getSize()-9; i<inventory.getSize(); i++) {
            inventory.setItem(i, istack);
        }

        return inventory;
    }

    public Inventory getPreview() {
        Inventory inventory = Bukkit.createInventory(null, getPrewviewRowsSize()*9, "§3"+name);
        fillRand(inventory);
        for(Rarity rarity : Rarity.values()) {
            for(CaseItem ci : caseitems) {
                if(ci.getRarity() == rarity) {
                    inventory.addItem(ci.getCustomItem());
                }
            }
        }

        ItemStack istack = new ItemStack(Material.END_CRYSTAL);
        ItemMeta istackM = istack.getItemMeta();
        istackM.setDisplayName("§7Open Case");
        istackM.setLocalizedName(name);
        istack.setItemMeta(istackM);
        inventory.setItem(inventory.getSize()-2, istack);

        return inventory;
    }

    public List<CaseItem> trim(Rarity rarity) {
        List<CaseItem> items = new ArrayList<CaseItem>();
        for(CaseItem item : caseitems) {
            if(item.getRarity() == rarity) {
                items.add(item);
            }
        }
        return items;
    }

    public CaseItem getRandomCaseItem(Rarity rarity) {
        Random rndm = new Random();
        List<CaseItem> items = trim(rarity);
        int index = rndm.nextInt(items.size());
        return items.get(index);
    }

    public CaseItem getRandomCaseItem() {
        Random rndm = new Random();
        double chance = rndm.nextDouble();
        double chancecounter = 0d;
        for(Rarity rarity : Rarity.values()) {
            if(chance > chancecounter && chance < chancecounter+rarity.getChance()) return getRandomCaseItem(rarity);
            chancecounter = chancecounter+rarity.getChance();
        }
        return null;
    }

    public ItemStack getKey() {
        ItemStack istack = new ItemStack(Material.TRIPWIRE_HOOK);

        ItemMeta istackM = istack.getItemMeta();
        istackM.addEnchant(Enchantment.ARROW_DAMAGE, 2, true);
        istackM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        istackM.setDisplayName("§8§ §r§3"+name+"§7-Key");
        Random rndm = new Random();
        istackM.setLocalizedName(""+rndm.nextInt(1000000));
        istack.setItemMeta(istackM);

        return istack;
    }

    public ItemStack getCase() {
        ItemStack istack = new ItemStack(Material.WHITE_SHULKER_BOX);
        if(color == Color.BLACK) {
            istack.setType(Material.BLACK_SHULKER_BOX);
        }else if(color == Color.BLUE) {
            istack.setType(Material.BLUE_SHULKER_BOX);
        }

        ItemMeta istackM = istack.getItemMeta();
        istackM.setDisplayName("§8§ §r§3"+name);
        Random rndm = new Random();
        istackM.setLocalizedName(""+rndm.nextInt(1000000));
        istack.setItemMeta(istackM);

        return istack;
    }

    public void addItem(CaseItem casitem) {
        caseitems.add(casitem);
    }

    public static Case getCasebyName(String name) {
        for(Case c : cases) {
            if(c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

    public static boolean existCasebyName(String name) {
        for(Case c : cases) {
            if(c.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public void open(final Player player) {
        final CaseItem winitem = getRandomCaseItem();
        final PlayerProfile profile = PlayerProfile.getProfile(player.getUniqueId());
        profile.addCaseOpened();

        Bukkit.getScheduler().runTaskLater(LunaniaSystem.getPlugin(), new Runnable() {
            public void run() {
                if(winitem.getType() == Type.HOME) {
                    int amount = (Integer) winitem.getRewardObject();
                    int maxhomes = profile.getMaxhomes();
                    profile.setMaxhomes(maxhomes+amount);
                    player.sendMessage(winitem.getRarity().toFormatString() + " §7You've §awon §e"+amount+" §7more homes.");
                }else if(winitem.getType() == Type.ITEM) {
                    ItemStack item = (ItemStack) winitem.getRewardObject();
                    player.getInventory().addItem(item);
                    player.sendMessage(winitem.getRarity().toFormatString() + " §7You've §awon§7 an Item!");
                }else if(winitem.getType() == Type.LAND) {
                    int amount = (Integer) winitem.getRewardObject();
                    int maxlands = profile.getMaxhomes();
                    profile.setMaxLands(maxlands+amount);
                    player.sendMessage(winitem.getRarity().toFormatString() + " §7You've won §e"+amount+" §7more Lands.");
                }else if(winitem.getType() == Type.MONEY) {
                    double money = (Double) winitem.getRewardObject();
                    new PlayerAccount(player.getUniqueId()).addMoney(money, "Server", "Won in Case.");
                    player.sendMessage(winitem.getRarity().toFormatString() + " §7You've won §e"+new DecimalFormat("#,###.##").format(money)+ EconomySystem.getEcosymbol() +"§7.");
                }else if(winitem.getType() == Type.PERMISSION) {
                    String perm = (String) winitem.getRewardObject();
                    if(player.hasPermission(perm)) {
                        player.sendMessage(winitem.getRarity().toFormatString() + " §7You've won the permission §e" + perm + "§7.");
                        player.sendMessage("§7Because you already have the permission, you receive money depending on the rarity.");
                        new PlayerAccount(player.getUniqueId()).addMoney(winitem.getRewardCompensation(), "Server", "Won in Case (Compensation for perms).");
                    }else {
                        LuckPerms api = LuckPermsProvider.get();
                        User user = api.getUserManager().getUser(player.getUniqueId());
                        user.data().add(Node.builder(perm).build());
                        api.getUserManager().saveUser(user);
                        player.sendMessage(winitem.getRarity().toFormatString() + " §7You've won the permission §e" + perm + "§7.");
                    }
                }else if(winitem.getType() == Type.USERWARP) {
                    int amount = (Integer) winitem.getRewardObject();
                    int warptokens = profile.getWarptokens();
                    profile.setWarptokens(warptokens+amount);
                    player.sendMessage(winitem.getRarity().toFormatString() + " §7You've won §e"+amount+" §7more WarpTokens.");
                }
            }
        }, 96);

        //* Animation
        final Inventory inventory = Bukkit.createInventory(null, 9*6, "§6§lCase");
        setupInv(inventory);
        CaseEvents.inrolling.add(player);
        player.openInventory(inventory);

        int time = 1; // start time
        for(int rounds=20; rounds>0; rounds--) {
            if(rounds > 10) {
                time = time + 2;
            }else if(rounds > 4) {
                time = time + 5;
            }else {
                time = time + 10;
            }
            final int check = rounds;
            Bukkit.getScheduler().runTaskLater(LunaniaSystem.getPlugin(), new Runnable() {
                public void run() {
                    boolean bool = check == 6;
                    next(inventory, bool, winitem.getCustomItem());
                    player.playSound(player, Sound.BLOCK_LEVER_CLICK, 10, 0);
                }
            }, time);
        }
    }

    public Inventory setupInv(Inventory inventory) {
        for(int i=0; i<inventory.getSize(); i++) {
            ItemStack istack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            ItemMeta istackM = istack.getItemMeta();
            istackM.setDisplayName(" ");
            istack.setItemMeta(istackM);
            inventory.setItem(i, istack);
        }

        inventory.setItem(45, null);
        inventory.setItem(36, null);
        inventory.setItem(28, null);
        inventory.setItem(20, null);
        inventory.setItem(21, null);
        inventory.setItem(22, null);
        inventory.setItem(23, null);
        inventory.setItem(24, null);
        inventory.setItem(34, null);
        inventory.setItem(44, null);
        inventory.setItem(53, null);

        ItemStack campfire = new ItemStack(Material.CAMPFIRE);
        ItemMeta campfireM = campfire.getItemMeta();
        campfireM.setDisplayName("§6Your Win:");
        campfire.setItemMeta(campfireM);

        ItemStack hopper = new ItemStack(Material.HOPPER);
        ItemMeta hopperM = hopper.getItemMeta();
        hopperM.setDisplayName("§6Your Win:");
        hopper.setItemMeta(hopperM);

        inventory.setItem(13, hopper);
        inventory.setItem(31, campfire);

        return inventory;
    }

    public Inventory next(Inventory inventory, boolean addwinningitem, ItemStack winningitem) {

        ItemStack newitem = getRandomCaseItem().getCustomItem();
        if(addwinningitem) newitem = winningitem.clone();

        if(inventory.getItem(44) != null) inventory.setItem(53, inventory.getItem(44));
        if(inventory.getItem(34) != null) inventory.setItem(44, inventory.getItem(34));
        if(inventory.getItem(24) != null) inventory.setItem(34, inventory.getItem(24));
        if(inventory.getItem(23) != null) inventory.setItem(24, inventory.getItem(23));
        if(inventory.getItem(22) != null) inventory.setItem(23, inventory.getItem(22));
        if(inventory.getItem(21) != null) inventory.setItem(22, inventory.getItem(21));
        if(inventory.getItem(20) != null) inventory.setItem(21, inventory.getItem(20));
        if(inventory.getItem(28) != null) inventory.setItem(20, inventory.getItem(28));
        if(inventory.getItem(36) != null) inventory.setItem(28, inventory.getItem(36));
        if(inventory.getItem(45) != null) inventory.setItem(36, inventory.getItem(45));

        inventory.setItem(45, newitem);

        return inventory;
    }

}