package de.koppy.land.api;

import de.koppy.basics.api.InventoryHelper;
import de.koppy.basics.api.ItemBuilder;
import de.koppy.basics.api.PlayerProfile;
import de.koppy.economy.EconomySystem;
import de.koppy.land.commands.Lands;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

//TODO landmenu

public class LandMenu extends InventoryHelper {
    private final Inventory inventory;
    public LandMenu(Inventory inventory) {
        this.inventory = inventory;
    }
    public LandMenu(String displayname) {
        this.inventory = new Menu(displayname, 3*9).getInventory();
    }

    public Inventory getMainPage() {
        inventory.clear();
        setInventoryGlassPane(inventory);
        inventory.setItem(10, new ItemBuilder(Material.MAP).setDisplayname("§2List of Lands").getItemStack());
        inventory.setItem(13, new ItemBuilder(Material.WOODEN_AXE).setDisplayname("§3Manage Lands").getItemStack());
        inventory.setItem(16, new ItemBuilder(Material.PAPER).setDisplayname("§eHelp §7for lands").getItemStack());
        return inventory;
    }

    public Inventory getListHelp() {
        inventory.clear();
        InventoryHelper.setInventoryGlassPane(inventory);
        inventory.setItem(12, new ItemBuilder(Material.PAPER).setDisplayname("§eHilfe §7- Grundstücke kaufen").addLore("§7§m---------")
                .addLore("§7Du kannst dir eine gewisse Anzahl an Grundstücken kaufen.").addLore("§7Wenn du noch mehr haben möchtest, musst du dir diese erspielen.")
                .addLore("§7Mehr §aLandSlots §7erhälst du durch das §eLeveln §7in verschiedenen Systemen,")
                .addLore("das erledigen von §2Quests §7oder das öffnen von §dTruhen§7.")
                .addLore("").addLore("§7Jedes Grundstück hat einen festen Preis von §e"+ EconomySystem.getFormat().format(Lands.PRICE_PER_LAND) + EconomySystem.getEcosymbol() +" §7Coins.").getItemStack());
        inventory.setItem(13, new ItemBuilder(Material.PAPER).setDisplayname("§eHilfe §7- Managen von Grundstücken").addLore("§7§m---------")
                .addLore("§7Es gibt mehrere Befehle mit denen du dein Grundstück verwalten kannst.")
                .addLore("§7Verwende dafür /land gefolgt von:").addLore("")
                .addLore("§emember add §7<playername>").addLore("§emember remove §7<playername>").addLore("§eban add §7<playername>").addLore("§eban remove §7<playername>")
                .addLore("§esetflag §7<flag> <true/false>").getItemStack());
        inventory.setItem(14, new ItemBuilder(Material.PAPER).setDisplayname("§eHilfe §7- weiteres...").addLore("§7§m---------")
                .addLore("§7Es gibt noch ein paar weitere commands und es werden auch noch mehr hinzugefügt.")
                .addLore("§7Tab dich einfach mal durch die Liste!").getItemStack());
        inventory.setItem(19, new ItemBuilder(Material.PAPER).setDisplayname("§cGo Back").getItemStack());
        return inventory;
    }

    public Inventory getConfirmBanAdd(String uuid) {
        inventory.clear();
        getPlayerInventory(uuid);
        inventory.setItem(11, new ItemBuilder(Material.GREEN_CONCRETE_POWDER).setDisplayname("§7Ban §abestätigen").setLocalizedName("banadd").getItemStack());
        inventory.setItem(15, new ItemBuilder(Material.RED_CONCRETE_POWDER).setDisplayname("§cAbbruch").getItemStack());
        return inventory;
    }

    public Inventory getConfirmMemberAdd(String uuid) {
        inventory.clear();
        getPlayerInventory(uuid);
        inventory.setItem(11, new ItemBuilder(Material.GREEN_CONCRETE_POWDER).setDisplayname("§7Member §abestätigen").setLocalizedName("memberadd").getItemStack());
        inventory.setItem(15, new ItemBuilder(Material.RED_CONCRETE_POWDER).setDisplayname("§cAbbruch").getItemStack());
        return inventory;
    }

    public Inventory getConfirmBanRemove(String uuid) {
        inventory.clear();
        getPlayerInventory(uuid);
        inventory.setItem(11, new ItemBuilder(Material.GREEN_CONCRETE_POWDER).setDisplayname("§7Unban §abestätigen").setLocalizedName("banremove").getItemStack());
        inventory.setItem(15, new ItemBuilder(Material.RED_CONCRETE_POWDER).setDisplayname("§cAbbruch").getItemStack());
        return inventory;
    }

    public Inventory getConfirmMemberRemove(String uuid) {
        inventory.clear();
        getPlayerInventory(uuid);
        inventory.setItem(11, new ItemBuilder(Material.GREEN_CONCRETE_POWDER).setDisplayname("§7Member-Remove §abestätigen").setLocalizedName("memberremove").getItemStack());
        inventory.setItem(15, new ItemBuilder(Material.RED_CONCRETE_POWDER).setDisplayname("§cAbbruch").getItemStack());
        return inventory;
    }

    private Inventory getPlayerInventory(String uuid) {
        inventory.clear();
        InventoryHelper.setInventoryGlassPane(inventory);
        String name = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
        inventory.setItem(13, new ItemBuilder(Material.PLAYER_HEAD).setSkull(name).setDisplayname("§8Player: §7" + name).setLocalizedName(uuid).getItemStack());
        return inventory;
    }

    public Inventory getLandManageBan(int page, Chunk chunk) {
        inventory.clear();

        List<String> lands = new Land(chunk).getBannedUUIDs();
        int landsize = lands.size();
        int maxpage = landsize / 18;
        if(landsize % 18 != 0) maxpage++;

        for(int i=18; i<inventory.getSize(); i++) {
            inventory.setItem(i, InventoryHelper.getEmptyGlass(Material.GRAY_STAINED_GLASS_PANE));
        }
        if(maxpage > page) inventory.setItem(26, new ItemBuilder(Material.PLAYER_HEAD).setSkull("MHF_ArrowRight").setDisplayname("§7Zu Seite " + (page+1)).setLocalizedName("ban:"+(page+1)).getItemStack());
        if(page > 1) inventory.setItem(18, new ItemBuilder(Material.PLAYER_HEAD).setSkull("MHF_ArrowLeft").setDisplayname("§7Zu Seite " + (page-1)).setLocalizedName("ban:"+(page-1)).getItemStack());

        int beginindex = 18 * (page-1);
        int endindex = (18 * page)-1;
        for(int i=beginindex; i<=endindex; i++) {
            if(lands.size() <= i) break;
            String uuid = lands.get(i);
            String name = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
            inventory.addItem(new ItemBuilder(Material.PLAYER_HEAD).setSkull(name).setDisplayname("§c> §7"+name).setLocalizedName(uuid).getItemStack());
        }

        inventory.setItem(25, new ItemBuilder(Material.GREEN_CONCRETE).setDisplayname("§aadd §7Ban").getItemStack());
        inventory.setItem(19, new ItemBuilder(Material.PAPER).setDisplayname("§cGo Back to ManageMenu").getItemStack());
        return inventory;
    }

    public Inventory getLandManageMember(int page, Chunk chunk) {
        inventory.clear();
        List<String> lands = new Land(chunk).getMemberUUIDs();
        int landsize = lands.size();
        int maxpage = landsize / 18;
        if(landsize % 18 != 0) maxpage++;

        for(int i=18; i<inventory.getSize(); i++) {
            inventory.setItem(i, InventoryHelper.getEmptyGlass(Material.GRAY_STAINED_GLASS_PANE));
        }
        if(maxpage > page) inventory.setItem(26, new ItemBuilder(Material.PLAYER_HEAD).setSkull("MHF_ArrowRight").setDisplayname("§7Zu Seite " + (page+1)).setLocalizedName("member:"+(page+1)).getItemStack());
        if(page > 1) inventory.setItem(18, new ItemBuilder(Material.PLAYER_HEAD).setSkull("MHF_ArrowLeft").setDisplayname("§7Zu Seite " + (page-1)).setLocalizedName("member:"+(page-1)).getItemStack());

        int beginindex = 18 * (page-1);
        int endindex = (18 * page)-1;
        for(int i=beginindex; i<=endindex; i++) {
            if(lands.size() <= i) break;
            String uuid = lands.get(i);
            String name = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
            inventory.addItem(new ItemBuilder(Material.PLAYER_HEAD).setSkull(name).setDisplayname("§8> §7"+name).setLocalizedName(uuid).getItemStack());
        }
        inventory.setItem(25, new ItemBuilder(Material.GREEN_CONCRETE).setDisplayname("§aadd §7Member").getItemStack());
        inventory.setItem(19, new ItemBuilder(Material.PAPER).setDisplayname("§cGo Back to ManageMenu").getItemStack());
        return inventory;
    }

    public Inventory getLandManage(Chunk chunk) {
        inventory.clear();
        InventoryHelper.setInventoryGlassPane(inventory);
        inventory.setItem(19, new ItemBuilder(Material.PAPER).setDisplayname("§cGo Back").getItemStack());
        if(chunk.getWorld().getName().equalsIgnoreCase("world")) {
            Land land = new Land(chunk);

            if(land.getFlag(Flag.PVP)) {
                inventory.setItem(7, new ItemBuilder(Material.IRON_SWORD).setDisplayname("§3Flag: §7PvP §8(§aan§8)").getItemStack());
                inventory.setItem(8, new ItemBuilder(Material.LIME_DYE).setDisplayname("§a§lon").setLocalizedName("pvp").getItemStack());
            }else {
                inventory.setItem(7, new ItemBuilder(Material.IRON_SWORD).setDisplayname("§3Flag: §7PvP §8(§caus§8)").getItemStack());
                inventory.setItem(8, new ItemBuilder(Material.RED_DYE).setDisplayname("§c§loff").setLocalizedName("pvp").getItemStack());
            }

            if(land.getFlag(Flag.PVE)) {
                inventory.setItem(7+9, new ItemBuilder(Material.SHEEP_SPAWN_EGG).setDisplayname("§3Flag: §7PvE §8(§aan§8)").getItemStack());
                inventory.setItem(8+9, new ItemBuilder(Material.LIME_DYE).setDisplayname("§a§lon").setLocalizedName("pve").getItemStack());
            }else {
                inventory.setItem(7+9, new ItemBuilder(Material.SHEEP_SPAWN_EGG).setDisplayname("§3Flag: §7PvE §8(§caus§8)").getItemStack());
                inventory.setItem(8+9, new ItemBuilder(Material.RED_DYE).setDisplayname("§c§loff").setLocalizedName("pve").getItemStack());
            }

            if(land.getFlag(Flag.TNT)) {
                inventory.setItem(7+9+9, new ItemBuilder(Material.TNT_MINECART).setDisplayname("§3Flag: §7PTNT §8(§aan§8)").getItemStack());
                inventory.setItem(8+9+9, new ItemBuilder(Material.LIME_DYE).setDisplayname("§a§lon").setLocalizedName("tnt").getItemStack());
            }else {
                inventory.setItem(7+9+9, new ItemBuilder(Material.TNT_MINECART).setDisplayname("§3Flag: §7TNT §8(§caus§8)").getItemStack());
                inventory.setItem(8+9+9, new ItemBuilder(Material.RED_DYE).setDisplayname("§c§loff").setLocalizedName("tnt").getItemStack());
            }

            inventory.setItem(12, new ItemBuilder(Material.PLAYER_HEAD).setSkull(land.getOwnerName()).setDisplayname("§7Manage member").getItemStack());
            inventory.setItem(14, new ItemBuilder(Material.STRUCTURE_VOID).setDisplayname("§7Manage bans").getItemStack());

            return inventory;
        }
        inventory.setItem(13, new ItemBuilder(Material.BARRIER).setDisplayname("§cNo access to the Land you are standing on!").getItemStack());
        return inventory;
    }

    public Inventory getListLands(int page, UUID uuid) {
        inventory.clear();
        List<String> lands = PlayerProfile.getLands(uuid.toString());
        int landsize = lands.size();
        int maxpage = landsize / 18;
        if(landsize % 18 != 0) maxpage++;

        for(int i=18; i<inventory.getSize(); i++) {
            inventory.setItem(i, InventoryHelper.getEmptyGlass(Material.GRAY_STAINED_GLASS_PANE));
        }
        if(maxpage > page) inventory.setItem(26, new ItemBuilder(Material.PLAYER_HEAD).setSkull("MHF_ArrowRight").setDisplayname("§7Zu Seite " + (page+1)).setLocalizedName("lands:"+(page+1)).getItemStack());
        if(page > 1) inventory.setItem(18, new ItemBuilder(Material.PLAYER_HEAD).setSkull("MHF_ArrowLeft").setDisplayname("§7Zu Seite " + (page-1)).setLocalizedName("lands:"+(page-1)).getItemStack());
        inventory.setItem(19, new ItemBuilder(Material.PAPER).setDisplayname("§cGo Back").getItemStack());

        int beginindex = 18 * (page-1);
        int endindex = (18 * page)-1;
        for(int i=beginindex; i<=endindex; i++) {
            if(lands.size() <= i) break;
            Land land = Land.fromString(lands.get(i));
            inventory.addItem(new ItemBuilder(Material.PAPER).setDisplayname("§3Land: §7x"+land.getChunk().getX() + " z"+land.getChunk().getZ()).addLore(" ").addLore("§7PVP: §8"+land.getFlag(Flag.PVP)).getItemStack());
        }

        return inventory;
    }
}