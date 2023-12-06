package de.koppy.basics.commands;

import de.koppy.basics.BasicSystem;
import de.koppy.basics.api.HomeMenu;
import de.koppy.basics.api.PlayerProfile;
import de.koppy.lunaniasystem.LunaniaSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Home implements CommandExecutor {

    public static ArrayList<Player> inmenu = new ArrayList<Player>();
    private int maxnamelength = 10;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if(args.length == 0) {
            player.openInventory(new HomeMenu(player.getUniqueId()).getMenu());
            inmenu.add(player);
            return false;
        }else if(args.length == 1) {
            String homename = args[0];
            PlayerProfile profile = PlayerProfile.getProfile(player.getUniqueId());
            if(homename.equalsIgnoreCase("list")) {
                List<de.koppy.basics.api.Home> homes = profile.getHomes();
                if(homes.isEmpty()) {
                    player.sendMessage(BasicSystem.getPrefix() + "§cDu hast bisher noch keine Homes.");
                    return false;
                }
                for(de.koppy.basics.api.Home home : homes) {
                    player.sendMessage(BasicSystem.getPrefix() + "§3"+home.getServer()+" §7| §3"+home.getName() + ": §7x"+(int) home.getLocation().getX()+" y"+(int) home.getLocation().getY()+" z"+(int) home.getLocation().getZ() + " ("+home.getLocation().getWorld().getName()+")");
                }
                return false;
            }else {
                if (!profile.existHome(homename)) {
                    player.sendMessage(BasicSystem.getPrefix() + "§cEs existiert kein home mit dieser Bezeichnung.");
                    return false;
                }
                de.koppy.basics.api.Home home = profile.getHome(homename);
                //TODO: change server if server != this server
                player.teleport(home.getLocation());
                player.sendMessage(BasicSystem.getPrefix() + "§7Du wurdest zu deinem home §e" + homename + " §7teleportiert.");
                return false;
            }
        }else if(args.length == 2) {
            PlayerProfile profile = PlayerProfile.getProfile(player.getUniqueId());
            String homename = args[1];
            if(args[0].equalsIgnoreCase("create")) {
                if(profile.existHome(homename)) { player.sendMessage(BasicSystem.getPrefix() + "§cDu hast bereits ein Home mit diesem namen."); return false; }
                if(profile.getHomes().size() >= profile.getMaxhomes()) { player.sendMessage(BasicSystem.getPrefix() + "§cDu hast bereits die maximale Anzahl an homes erreicht."); return false; }
                if(homename.length() > maxnamelength) { player.sendMessage(BasicSystem.getPrefix() + "§cDu darfst die maximale Anzahl von 10 Buchstaben nicht überschreiten."); return false; }
                if(!homename.matches("[a-zA-Z0-9]+")) { player.sendMessage(BasicSystem.getPrefix() + "§cVerwende bitt nur Buchstaben und Zahlen."); return false; }
                profile.addHome(new de.koppy.basics.api.Home(homename, LunaniaSystem.getServerInstance().getName(), player.getLocation()));
                player.sendMessage(BasicSystem.getPrefix() + "§7Das home §e" + homename + " §7wurde §aerstellt§7.");
                return false;
            }else if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("delete")) {
                if(!profile.existHome(homename)) { player.sendMessage(BasicSystem.getPrefix() + "§cDu hast kein Home mit diesem namen."); return false; }
                profile.removeHome(homename);
                player.sendMessage(BasicSystem.getPrefix() + "§7Das home §e" + homename + " §7wurde §centfernt§7.");
                return false;
            }
        }

        return false;
    }
}
