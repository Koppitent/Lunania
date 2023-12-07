package de.koppy.basics.listener;

import de.koppy.basics.api.PlayerProfile;
import de.koppy.basics.api.scoreboard.Tablist;
import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.server.Server;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
        LunaniaSystem.getServerInstance().setPlayerListHeaderFooter(e.getPlayer());
        for(Player all : Bukkit.getOnlinePlayers()) {
            new Tablist().updateTablist(all);
        }
        if(e.getPlayer().hasPermission("server.admin")) {
            if(LunaniaSystem.getServerInstance().isVersionmessage()) {
                String version = LunaniaSystem.getPlugin().getDescription().getVersion();
                e.getPlayer().sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§aDieses Plugin läuft auf der Version §3PREALPHA-" +  version);
            }
        }
        Server server = LunaniaSystem.getServerInstance();
        server.applyTexturepack(e.getPlayer());
      }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        e.setQuitMessage(null);
        PlayerProfile profile = PlayerProfile.getProfile(e.getPlayer().getUniqueId());
        profile.saveHearts(e.getPlayer());
        profile.saveFood(e.getPlayer());
        profile.remove();
    }

}
