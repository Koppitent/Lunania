package de.koppy.basics.commands;

import java.util.ArrayList;
import java.util.List;

import de.koppy.basics.api.ChangelogItem;
import de.koppy.basics.listener.InventoryEvents;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class Changelog implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player))
            return false;

        Inventory inventory = Bukkit.createInventory(null, 9*3, "§eChangelog");
        Player player = (Player) sender;
        player.openInventory(inventory);
        getChangelogInv(inventory, getChangelogItems(), 1);
        player.updateInventory();
        InventoryEvents.ininv.add(player);

        return false;
    }

    public static List<ChangelogItem> getChangelogItems(){
        List<ChangelogItem> changelogitems = new ArrayList<ChangelogItem>();

        String title1 = "§3Changelog-Update (#1)";
        String date1 = "§7§o17.07.2023 09:43";
        ArrayList<String> lore1 = new ArrayList<String>();
        lore1.add(" ");
        lore1.add("§a+ §7setup Project");
        lore1.add("§a+ §7implemented MySQL classes");
        lore1.add("§a+ §7implemented Subsystem features");
        lore1.add("§a+ §7implemented PlayerProfile classes");
        lore1.add("§a+ §7added Changelog");
        lore1.add("§a+ §7added Home-System");
        lore1.add("§a+ §7added Ban-System");
        lore1.add("§a+ §7added Language-System");
        lore1.add("§a+ §7added small cmd's (/discord, /store, ...)");
        changelogitems.add(new ChangelogItem(title1, date1, lore1));

        String title2 = "§3Changelog-Update (#2)";
        String date2 = "§7§o19.07.2023 04:47";
        ArrayList<String> lore2 = new ArrayList<String>();
        lore2.add(" ");
        lore2.add("§a+ §7added Land-System");
        lore2.add("§a+ §7added Tablist");
        lore2.add("§a+ §7added Scoreboard");
        lore2.add("§a+ §7added gamemode command");
        lore2.add("§a+ §7added playtime");
        lore2.add("§a+ §7fixed Ban and added a log for it");
        lore2.add("§a+ §7added Chunk-Editor (necessary for landsys)");
        lore2.add("§a+ §7added Options (server config.yml file)");
        changelogitems.add(new ChangelogItem(title2, date2, lore2));

        String title3 = "§3Changelog-Update (#3)";
        String date3 = "§7§o19.07.2023 05:56";
        ArrayList<String> lore3 = new ArrayList<String>();
        lore3.add(" ");
        lore3.add("§a+ §7added Land-Systems Events");
        lore3.add("§8§ §7§oneed to change Lands InteractEvent");
        lore3.add("§a+ §7added Join/Quit Messages");
        lore3.add("§a+ §7added Chat-Format");
        lore3.add("§8§ §7§oneed to add LanguageSys into other systems");
        lore3.add("§8§ §7§oneed to add GUI's to Systems");
        lore3.add("§a+ §7added Ecologs");
        changelogitems.add(new ChangelogItem(title3, date3, lore3));

        String title4 = "§3Changelog-Update (#4)";
        String date4 = "§7§o18.08.2023 20:33";
        ArrayList<String> lore4 = new ArrayList<String>();
        lore4.add(" ");
        lore4.add("§a+ §7added AdminShop-System");
        lore4.add("§a+ §7added Job-System");
        lore4.add("§a+ §7added /baltop, /eco <playernam> and /eco toggle");
        lore4.add("§a+ §7added Seats");
        lore4.add("§a+ §7added Playtime");
        lore4.add("§a+ §7added UserWarp-System");
        changelogitems.add(new ChangelogItem(title4, date4, lore4));

        String title5 = "§3Changelog-Update (#5)";
        String date5 = "§7§o19.08.2023 18:34";
        ArrayList<String> lore5 = new ArrayList<String>();
        lore5.add(" ");
        lore5.add("§a+ §7added NPC-System");
        lore5.add("§8§ §7§oneed to make Displayname with color for NPC");
        lore5.add("§8§ §7§oneed to rework commands for NPC-system");
        lore5.add("§a+ §7added /ping");
        lore5.add("§a+ §7added /spawn");
        lore5.add("§a+ §7added MOTD and MaxPlayers");
        lore5.add("§a+ §7added Broadcastmessages");
        lore5.add("§7- §7minor Bugfixes in Shop and Basic Systems");
        changelogitems.add(new ChangelogItem(title5, date5, lore5));

        String title6 = "§3Changelog-Update (#6)";
        String date6 = "§7§o20.08.2023 04:43";
        ArrayList<String> lore6 = new ArrayList<String>();
        lore6.add(" ");
        lore6.add("§a+ §7added Nick-System");
        lore6.add("§a+ §7added Skin-System");
        lore6.add("§c- §7temporary disabled Seats due to overuse");
        changelogitems.add(new ChangelogItem(title6, date6, lore6));

        String title7 = "§3Changelog-Update (#7)";
        String date7 = "§7§o22.08.2023 00:53";
        ArrayList<String> lore7 = new ArrayList<String>();
        lore7.add(" ");
        lore7.add("§a+ §7added Server Resourcepack");
        lore7.add("§a+ §7added Case-system");
        lore7.add("§a+ §7added /tpa, /tptoggle, /tpaccept and /tpdeny");
        lore7.add("§a+ §7added /msg, /r, /msgtoggle");
        lore7.add("§a- §7fixed NPC types/typecontent");
        lore7.add("§a- §7changed Tablist&Scoreboard");
        changelogitems.add(new ChangelogItem(title7, date7, lore7));

        String title8 = "§3Changelog-Update (#8)";
        String date8 = "§8§o23.08.2023 02:11";
        ArrayList<String> lore8 = new ArrayList<String>();
        lore8.add(" ");
        lore8.add("§a+ §7added Mission-system");
        lore8.add("§a+ §7added /heal");
        lore8.add("§a+ §7various Bugfixes");
        changelogitems.add(new ChangelogItem(title8, date8, lore8));

        String title9 = "§3Changelog-Update (#9)";
        String date9 = "§8§o29.08.2023 01:20";
        ArrayList<String> lore9 = new ArrayList<String>();
        lore9.add(" ");
        lore9.add("§a+ §7/warp list");
        lore9.add("§a+ §7Job UI");
        lore9.add("§a+ §7Basic case");
        lore9.add("§7- §7fixed various NPC-bugs");
        lore9.add("§7- §7fixed playtime double message");
        changelogitems.add(new ChangelogItem(title9, date9, lore9));

        String title10 = "§3Changelog-Update (#10)";
        String date10 = "§8§o29.08.2023 03:20";
        ArrayList<String> lore10 = new ArrayList<String>();
        lore10.add(" ");
        lore10.add("§a+ §7/land list");
        lore10.add("§a+ §7added Jobs");
        lore10.add("§a+ §7added Level for Jobs");
        lore10.add("§a+ §7added Tasks for Jobs");
        changelogitems.add(new ChangelogItem(title10, date10, lore10));

        return changelogitems;
    }

    @SuppressWarnings("deprecation")
    public static void getChangelogInv(Inventory inventory, List<ChangelogItem> changelogitems, int page) {

        ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta glassM = glass.getItemMeta();
        glassM.setDisplayName(" ");
        glass.setItemMeta(glassM);

        inventory.setItem(0, glass);
        inventory.setItem(1, glass);
        inventory.setItem(2, glass);
        inventory.setItem(3, glass);
        inventory.setItem(4, glass);
        inventory.setItem(5, glass);
        inventory.setItem(6, glass);
        inventory.setItem(7, glass);
        inventory.setItem(8, glass);

        inventory.setItem(18, glass);
        inventory.setItem(19, glass);
        inventory.setItem(20, glass);
        inventory.setItem(21, glass);
        inventory.setItem(22, glass);
        inventory.setItem(23, glass);
        inventory.setItem(24, glass);
        inventory.setItem(25, glass);
        inventory.setItem(26, glass);

        int ii=9;
        if(changelogitems.size() > ((page-1) * 9)) {
            for(int i=(((page-1)*9)); i<(((page)*9));i++){
                if(changelogitems.size() > i) {
                    ChangelogItem changelogitem = changelogitems.get(i);
                    inventory.setItem(ii, changelogitem.getBook());
                }else {
                    inventory.setItem(ii, null);
                }
                ii++;
            }
        }

        //* Check if next site exist
        if(changelogitems.size() > ((page) * 9)) {
            ItemStack arrowleft = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta arrowleftM = (SkullMeta) arrowleft.getItemMeta();
            arrowleftM.setDisplayName("§3Next Page");
            arrowleftM.setLocalizedName(""+(page+1));
            arrowleftM.setOwner("MHF_ArrowRight");
            arrowleft.setItemMeta(arrowleftM);
            inventory.setItem(26, arrowleft);
        }

        //* Check if site before exist
        if(page > 1) {
            ItemStack arrowleft = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta arrowleftM = (SkullMeta) arrowleft.getItemMeta();
            arrowleftM.setDisplayName("§3Previous Page");
            arrowleftM.setLocalizedName(""+(page-1));
            arrowleftM.setOwner("MHF_ArrowLeft");
            arrowleft.setItemMeta(arrowleftM);
            inventory.setItem(18, arrowleft);
        }

    }

}