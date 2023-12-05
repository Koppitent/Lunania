package de.koppy.basics.listener;

import de.koppy.basics.api.PlayerProfile;
import de.koppy.lunaniasystem.LunaniaSystem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        e.getPlayer().sendMessage(LunaniaSystem.getServerInstance().getJoinMessage());
        PlayerProfile profile = PlayerProfile.getProfile(e.getPlayer().getUniqueId());
        e.getPlayer().setHealth(profile.getHeartsDatabase());
        e.getPlayer().setFoodLevel(profile.getFoodDatabase());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        e.setQuitMessage(null);
        PlayerProfile profile = PlayerProfile.getProfile(e.getPlayer().getUniqueId());
        profile.saveHearts(e.getPlayer());
        profile.saveFood(e.getPlayer());
    }

}
