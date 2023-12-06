package de.koppy.basics.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.koppy.basics.api.PlayerProfile;
import de.koppy.lunaniasystem.LunaniaSystem;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Tpa implements CommandExecutor {

    public static HashMap<Player, List<Player>> requests = new HashMap<Player, List<Player>>();

    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player p = (Player) sender;
        PlayerProfile profile = PlayerProfile.getProfile(p.getUniqueId());

        if(args.length == 1) {
            String name = args[0];
            if(Bukkit.getPlayer(name) != null) {
                Player sendrequestto = Bukkit.getPlayer(name);
                if(sendrequestto == p) return false;
                PlayerProfile tprofile = PlayerProfile.getProfile(sendrequestto.getUniqueId());
                if(!tprofile.isTpaAccept()) {
                    if(p != sendrequestto) {
                        addToRequest(sendrequestto, p);
                        p.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + profile.getMessage("tpasentto").replace("%player%", sendrequestto.getName()));
                        sendrequestto.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + tprofile.getMessage("tpagotfrom").replace("%player%", p.getName()));

                        TextComponent tc = new TextComponent("�a[ACCEPT]");
                        tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept " + p.getName()));
                        tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(tprofile.getMessage("clickaccept")).create()));

                        TextComponent tcspace = new TextComponent("�7 - ");

                        TextComponent tcd = new TextComponent("�c[DENY]");
                        tcd.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpdeny " + p.getName()));
                        tcd.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(tprofile.getMessage("clickdeny")).create()));

                        tc.addExtra(tcspace);
                        tc.addExtra(tcd);
                        sendrequestto.spigot().sendMessage(tc);

                    }else {
                        p.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + profile.getMessage("thatsurself"));
                    }
                }else {
                    p.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + profile.getMessage("doesntaccepttpa"));
                }
            }else {
                p.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + profile.getMessage("playernotonline"));
            }
        }

        return false;
    }

    public static void addToRequest(Player player, Player targettoadd) {
        if(requests.containsKey(player)) {
            List<Player> list = requests.get(player);
            if(!list.contains(targettoadd)) {
                list.add(targettoadd);
                requests.put(player, list);
            }
        }else {
            List<Player> list = new ArrayList<Player>();
            list.add(targettoadd);
            requests.put(player, list);
        }
    }

    public static void removeFromRequest(Player player, Player targettoadd) {
        if(requests.containsKey(player)) {
            List<Player> list = requests.get(player);
            if(list.contains(targettoadd)) {
                list.remove(targettoadd);
                requests.put(player, list);
            }
        }
    }

    public static boolean existInRequest(Player player, Player targettoadd) {
        if(requests.containsKey(player)) {
            List<Player> list = requests.get(player);
            return list.contains(targettoadd);
        }
        return false;
    }

}