package de.koppy.basics.listener;

import de.koppy.basics.api.PlayerProfile;
import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.server.Server;
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
        if(e.getPlayer().hasPermission("server.admin")) {
            if(LunaniaSystem.getServerInstance().isVersionmessage()) {
                String version = LunaniaSystem.getPlugin().getDescription().getVersion();
                e.getPlayer().sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§aDieses Plugin läuft auf der Version §3PREALPHA-" +  version);
            }
        }
        Server server = LunaniaSystem.getServerInstance();
        e.getPlayer().setResourcePack(server.getUrl(), server.getSha1(), "§8§m---------------------------------\n\n§r "+server.getLunania()+"\n\n§3This resourcepack is used \nfor a better experience \n\n§8§m---------------------------------", true);
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
