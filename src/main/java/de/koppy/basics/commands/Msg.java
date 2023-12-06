package de.koppy.basics.commands;

import java.util.HashMap;

import de.koppy.basics.api.PlayerProfile;
import de.koppy.lunaniasystem.LunaniaSystem;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Msg implements CommandExecutor {

    public static HashMap<Player, Player> lastmessagereceivedfrom = new HashMap<Player, Player>();
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player p = (Player) sender;
        PlayerProfile profile = PlayerProfile.getProfile(p.getUniqueId());
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("toggle")) {
                if(profile.isMsgAccept()) {
                    p.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + profile.getMessage("msgtoggleon"));
                    profile.setMsgAccept(false);
                }else {
                    p.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + profile.getMessage("msgtoggleoff"));
                    profile.setMsgAccept(true);
                }
            }
        }else if(args.length >= 2) {
            String name = args[0];
            if(Bukkit.getPlayer(name) != null) {
                Player t = Bukkit.getPlayer(name);
                if(t == p) return false;
                PlayerProfile tprofile = PlayerProfile.getProfile(t.getUniqueId());
                if(tprofile.isMsgAccept() == false) {
                    String msg = "";
                    for(int i=1; i<args.length; i++) {
                        msg = msg + " " + args[i];
                    }
                    lastmessagereceivedfrom.put(t, p);
                    t.sendMessage("§8[§e"+p.getName()+" §7-> §eDir§8]§e"+msg);
                    p.sendMessage("§8[§eDu §7-> §e"+t.getName()+"§8]§e"+msg);
                }else {
                    p.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + profile.getMessage("doesntacceptmessages"));
                }
            }else {
                p.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + profile.getMessage("playernotonline"));
            }
        }

        return false;
    }
}