package de.lunania.quest.listener;

import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.npc.api.NpcHitEvent;
import de.koppy.quest.api.PlayerQuest;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class QuestEvents implements Listener {

    @EventHandler
    public void npcEvent(NpcHitEvent e) {
        if(e.getNpc().getName().equals("tutorial")) {
            final PlayerQuest pq = new PlayerQuest(e.getPlayer().getUniqueId());
            if(pq.getStage("tutorial") == 0) {
                pq.addStage("tutorial");
                final Player p = e.getPlayer();
                p.sendMessage("§4Joe §8§ §7Oh, hey! Welcome to lunania!");
                p.playSound(p, Sound.ENTITY_VILLAGER_AMBIENT, 10, 1);

                Bukkit.getScheduler().runTaskLater(LunaniaSystem.getPlugin(), new Runnable() {
                    public void run() {
                        p.sendMessage("§4Joe §8§ §7I will help you to get a good start in the world!");
                        p.playSound(p, Sound.ENTITY_VILLAGER_AMBIENT, 10, 1);
                    }
                }, 20*1);

                Bukkit.getScheduler().runTaskLater(LunaniaSystem.getPlugin(), new Runnable() {
                    public void run() {
                        p.sendMessage("§4Joe §8§ §7First, teleport yourself to the farmworld.");
                        p.playSound(p, Sound.ENTITY_VILLAGER_AMBIENT, 10, 1);
                    }
                }, 20*2);

                Bukkit.getScheduler().runTaskLater(LunaniaSystem.getPlugin(), new Runnable() {
                    public void run() {
                        p.sendMessage("§4Joe §8§ §7Im going to wait there for you.");
                        p.playSound(p, Sound.ENTITY_VILLAGER_AMBIENT, 10, 1);
                    }
                }, 20*3);

                Bukkit.getScheduler().runTaskLater(LunaniaSystem.getPlugin(), new Runnable() {
                    @SuppressWarnings("deprecation")
                    public void run() {
                        p.sendTitle("§3Next Step:", "§7go to the Farmworld!");
                    }
                }, 20*4);

            }
        }
    }

}