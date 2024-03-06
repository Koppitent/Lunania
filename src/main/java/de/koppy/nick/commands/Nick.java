package de.koppy.nick.commands;

import de.koppy.basics.api.PlayerProfile;
import de.koppy.nick.api.NickManager;
import de.koppy.nick.api.NickUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Nick implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player p = (Player) sender;

        if(p.hasPermission("server.nick")) {
            if(args.length == 0) {
                NickUI ui = new NickUI(p.getUniqueId());
                ui.getMenu();
                p.openInventory(ui.getInventory());
                /*
                PlayerProfile profile = PlayerProfile.getProfile(p.getUniqueId());
                if(profile.isNicked()) {
                    NickManager nm = new NickManager(p);
                    nm.unnick();
                    p.sendMessage("NickName off!");
                }else {
                    NickManager nm = new NickManager(p);
                    String name = nm.getRandomNickname();
                    nm.changeName(name);
                    p.sendMessage("Name changed!");
                }
                 */
            }else if(args.length == 1) {
                String name = args[0];
                if(name.equals("off")) {
                    NickManager nm = new NickManager(p);
                    nm.unnick();
                    p.sendMessage("NickName of!");
                }else {
                    NickManager nm = new NickManager(p);
                    nm.changeName(name);
                    p.sendMessage("Name changed!");
                }
            }
        }

        return false;
    }

}