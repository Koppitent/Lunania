package de.koppy.quest.commands;

import de.koppy.quest.api.PlayerQuest;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Quest implements CommandExecutor {

    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if(player.hasPermission("server.admin.quest")) {
            if(args.length == 3) {
                String qname = args[0];
                String name = args[1];
                String number = args[2];
                if(number.matches("[0-9]+")) {
                    int num = Integer.valueOf(number);
                    OfflinePlayer t = Bukkit.getOfflinePlayer(name);
                    new PlayerQuest(t.getUniqueId()).setStage(qname, num);
                    player.sendMessage("§7Quest progress set.");
                }
            }
        }

        return false;
    }

}