package de.koppy.bansystem.listener;

import de.koppy.bansystem.api.BanManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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

}
