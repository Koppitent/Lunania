package de.koppy.basics.listener;

import de.koppy.basics.api.PlayerProfile;
import de.koppy.basics.api.Rank;
import de.koppy.lunaniasystem.LunaniaSystem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.event.world.WorldInitEvent;

public class ServerManageListener implements Listener {

    @EventHandler
    public void onPortalEvent(PortalCreateEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onShitSpreading(BlockSpreadEvent e) {
        if(e.getSource().getType() == Material.VINE || e.getSource().getType() == Material.TWISTING_VINES || e.getSource().getType() == Material.WEEPING_VINES) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerCommandSend(PlayerCommandSendEvent e) {
        if(!e.getPlayer().hasPermission("server.admin.tabs")) {
            Player p = e.getPlayer();
            e.getCommands().clear();
            e.getCommands().add("home");
            e.getCommands().add("land");
            e.getCommands().add("job");
            e.getCommands().add("eco");
            e.getCommands().add("money");
            e.getCommands().add("bank");
            e.getCommands().add("msg");
            e.getCommands().add("message");
            e.getCommands().add("ping");
            e.getCommands().add("playtime");
            e.getCommands().add("tpa");
            e.getCommands().add("tpaccept");
            e.getCommands().add("tpdeny");
            e.getCommands().add("tptoggle");
            e.getCommands().add("msgtoggle");
            e.getCommands().add("lang");
            e.getCommands().add("language");
            e.getCommands().add("r");
            e.getCommands().add("version");
            e.getCommands().add("spawn");
            e.getCommands().add("warp");
            e.getCommands().add("discord");
            e.getCommands().add("shop");
            e.getCommands().add("help");
            e.getCommands().add("mission");
            e.getCommands().add("quest");
            e.getCommands().add("store");
            if(p.hasPermission("server.gamemode")) e.getCommands().add("gamemode");
        }
    }

    @EventHandler
    public void onJoin(AsyncPlayerChatEvent e) {
        PlayerProfile profile = PlayerProfile.getProfile(e.getPlayer().getUniqueId());
        String message = e.getMessage();
        Rank rank = Rank.getRank(e.getPlayer());
        if(profile.isNicked()) {
            rank = Rank.RANDOM;
        }else {
            if(e.getPlayer().hasPermission("server.premium")) {
                message = message.replace("&", "§");
            }
        }
        if(message.contains("%")) message = message.replace("%", "");
        e.setFormat(Rank.getRankPrefix(rank) + "§7" + e.getPlayer().getName() + " §8» §7" + message);
    }

    @EventHandler
    public void onGamemodeChange(PlayerGameModeChangeEvent e) {
        PlayerProfile profile = PlayerProfile.getProfile(e.getPlayer().getUniqueId());
        e.getPlayer().sendMessage(LunaniaSystem.getServerInstance().getPrefix() + profile.getMessage("gamemodechanged").replace("%gamemode%", e.getNewGameMode().toString()));
    }

}