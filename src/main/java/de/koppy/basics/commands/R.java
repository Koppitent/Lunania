package de.koppy.basics.commands;

import de.koppy.basics.api.PlayerProfile;
import de.koppy.lunaniasystem.LunaniaSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class R implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player p = (Player) sender;
        PlayerProfile profile = PlayerProfile.getProfile(p.getUniqueId());

        if(args.length >= 1) {
            if(Msg.lastmessagereceivedfrom.containsKey(p)) {
                Player t = Msg.lastmessagereceivedfrom.get(p);
                PlayerProfile tprofile = PlayerProfile.getProfile(t.getUniqueId());
                if(!tprofile.isMsgAccept()) {
                    String msg = "";
                    for(int i=0; i<args.length; i++) {
                        msg = msg + " " + args[i];
                    }
                    Msg.lastmessagereceivedfrom.put(t, p);
                    t.sendMessage("§8[§e"+p.getName()+" §7-> §eDir§8]§e"+msg);
                    p.sendMessage("§8[§eDu §7-> §e"+t.getName()+"§8]§e"+msg);
                }else {
                    p.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + profile.getMessage("doesntacceptmessages"));
                }
            }else {
                p.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + profile.getMessage("nomsgsent"));
            }
        }

        return false;
    }
}