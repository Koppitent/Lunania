package de.koppy.quest.listener;

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
                p.sendMessage("§4Joe §8|§r §7Oh, hey! Welcome to Lunania!");
                p.playSound(p, Sound.ENTITY_VILLAGER_AMBIENT, 10, 1);

                Bukkit.getScheduler().runTaskLater(LunaniaSystem.getPlugin(), new Runnable() {
                    public void run() {
                        p.sendMessage("§4Joe §8|§r §7I will help you to get a good start in the world!");
                        p.playSound(p, Sound.ENTITY_VILLAGER_AMBIENT, 10, 1);
                    }
                }, 20*1);

                Bukkit.getScheduler().runTaskLater(LunaniaSystem.getPlugin(), new Runnable() {
                    public void run() {
                        p.sendMessage("§4Joe §8|§r §7First, teleport yourself to the farmworld.");
                        p.playSound(p, Sound.ENTITY_VILLAGER_AMBIENT, 10, 1);
                    }
                }, 20*2);

                Bukkit.getScheduler().runTaskLater(LunaniaSystem.getPlugin(), new Runnable() {
                    public void run() {
                        p.sendMessage("§4Joe §8|§r §7Im waiting there for you.");
                        p.playSound(p, Sound.ENTITY_VILLAGER_AMBIENT, 10, 1);
                    }
                }, 20*3);

                Bukkit.getScheduler().runTaskLater(LunaniaSystem.getPlugin(), new Runnable() {
                    @SuppressWarnings("deprecation")
                    public void run() {
                        p.sendTitle("§3Next Step:", "§7Go to the Farmworld!");
                    }
                }, 20*4);

            }
        }
    }

}