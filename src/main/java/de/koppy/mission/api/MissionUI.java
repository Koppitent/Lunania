package de.koppy.mission.api;

import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.lunaniasystem.api.UI;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MissionUI extends UI {

    private Player player;
    private String type;
    public MissionUI(Player player, String type) {
        super("§e"+type+" Missions", 9*1);
        this.player = player;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void getMenuDaily() {
        inventory.clear();
        for(String s : new PlayerMission(player.getUniqueId()).getDaily()) {
            inventory.addItem(new MissionHandler().getMission(s.split(":")[0]).getShowItem(player));
        }
        new BukkitRunnable() {
            public void run() {
                inventory.clear();
                for(String s : new PlayerMission(player.getUniqueId()).getDaily()) {
                    inventory.addItem(new MissionHandler().getMission(s.split(":")[0]).getShowItem(player));
                }
                if(!UI.inventories.containsKey(inventory)) {
                    cancel();
                }
            }
        }.runTaskTimer(LunaniaSystem.getPlugin(), 20, 20);
    }

    public void getMenuWeekly() {
        inventory.clear();
        for(String s : new PlayerMission(player.getUniqueId()).getWeekly()) {
            inventory.addItem(new MissionHandler().getMission(s.split(":")[0]).getShowItem(player));
        }
        new BukkitRunnable() {
            public void run() {
                inventory.clear();
                for(String s : new PlayerMission(player.getUniqueId()).getWeekly()) {
                    inventory.addItem(new MissionHandler().getMission(s.split(":")[0]).getShowItem(player));
                }
                if(!UI.inventories.containsKey(inventory)) {
                    cancel();
                }
            }
        }.runTaskTimer(LunaniaSystem.getPlugin(), 20, 20);
    }

}
