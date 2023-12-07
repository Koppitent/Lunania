package de.koppy.nick.commands;

import java.util.ArrayList;

import de.koppy.nick.api.NickManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Skin implements CommandExecutor {

    public static ArrayList<Player> useskin = new ArrayList<Player>();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player p = (Player) sender;

        if(p.hasPermission("server.skin")) {

            if(args.length == 0) {
                if(useskin.contains(p)) {
                    useskin.remove(p);
                    NickManager nm = new NickManager(p);
                    String[] skindata = NickManager.getSkinOrNothing(NickManager.realnames.get(p.getUniqueId()));
                    nm.changeSkin(skindata[0], skindata[1]);
                    p.sendMessage("Skinchange executet.");
                }else {
                    useskin.add(p);
                    NickManager nm = new NickManager(p);
                    String[] skindata = nm.getRandomSkinData();
                    nm.changeSkin(skindata[0], skindata[1]);
                    p.sendMessage("Skinchange executet.");
                }
            }else if(args.length == 1) {
                String name = args[0];
                NickManager nm = new NickManager(p);
                String skindata[] = NickManager.getSkinOrNothing(name);
                nm.changeSkin(skindata[0], skindata[1]);
                p.sendMessage("Skinchange executet.");
            }else if(args.length == 3) {
                if(args[0].equalsIgnoreCase("save")) {
                    String name = args[1];
                    String skinname = args[2];
                    NickManager.saveSkinData(skinname, NickManager.getSkinOrNothing(name));
                    p.sendMessage("§7Saved skin as " + skinname + ".");
                }
            }

        }

        return false;
    }

}