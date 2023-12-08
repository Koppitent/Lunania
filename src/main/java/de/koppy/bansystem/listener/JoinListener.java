package de.koppy.bansystem.listener;

import de.koppy.bansystem.api.BanManager;
import de.koppy.bansystem.api.MuteManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.Date;
import java.util.UUID;

public class JoinListener implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        BanManager banManager = new BanManager(uuid);
        if(banManager.isBanned()) {
            if(banManager.getExpireDate().before(new Date())) {
                banManager.unban(true);
                return;
            }
            e.setKickMessage(banManager.getBanMessage());
            e.setResult(PlayerLoginEvent.Result.KICK_BANNED);
        }
    }

    @EventHandler
    public void onLogin(AsyncPlayerChatEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        MuteManager muteManager = new MuteManager(uuid);
        if(muteManager.isMuted()) {
            if(muteManager.getExpireDate().before(new Date())) {
                muteManager.unmute(true);
                return;
            }
            e.setCancelled(true);
            e.getPlayer().sendMessage(muteManager.getMuteMessage());
        }
    }

}
