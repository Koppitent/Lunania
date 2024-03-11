package de.koppy.mission.api;

import de.koppy.basics.api.ItemBuilder;
import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.lunaniasystem.api.UI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MissionUI extends UI {

    private Player player;
    private String type;
    private String curmenu="";
    public MissionUI(Player player, String type) {
        super("§e"+type+" Missions", 9*3);
        this.player = player;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void getMenuDaily() {
        curmenu = "daily";
        getMenuDailyCode();
        new BukkitRunnable() {
            public void run() {
                getMenuDailyCode();
                if(!UI.inventories.containsKey(inventory) || !curmenu.equalsIgnoreCase("daily")) {
                    cancel();
                }
            }
        }.runTaskTimer(LunaniaSystem.getPlugin(), 20, 20);
    }

    private void getMenuDailyCode() {
        inventory.clear();
        setGlassRand(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayname("§c").getItemStack());
        for(String s : new PlayerMission(player.getUniqueId()).getDaily()) {
            inventory.addItem(new MissionHandler().getMission(s.split(":")[0]).getShowItem(player));
        }
        fillGlass(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayname("§c").getItemStack());
    }

    public void getMenuWeekly() {
        curmenu = "weekly";
        getMenuWeeklyCode();
        new BukkitRunnable() {
            public void run() {
                getMenuWeeklyCode();
                if(!UI.inventories.containsKey(inventory) || !curmenu.equalsIgnoreCase("weekly")) {
                    cancel();
                }
            }
        }.runTaskTimer(LunaniaSystem.getPlugin(), 20, 20);
    }

    private void getMenuWeeklyCode() {
        inventory.clear();
        setGlassRand(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayname("§c").getItemStack());
        for(String s : new PlayerMission(player.getUniqueId()).getWeekly()) {
            inventory.addItem(new MissionHandler().getMission(s.split(":")[0]).getShowItem(player));
        }
        fillGlass(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayname("§c").getItemStack());
    }

}
