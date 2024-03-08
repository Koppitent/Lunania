package de.koppy.quest.commands;

import de.koppy.quest.api.PlayerQuest;
import de.koppy.quest.api.QuestUI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Quest implements CommandExecutor, TabCompleter {

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
            }else if(args.length == 0) {
                QuestUI questui = new QuestUI(player.getUniqueId());
                questui.getMenu(1);
                player.openInventory(questui.getInventory());
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> tcomplete = new ArrayList<String>();
        if(sender instanceof Player) {
            if(args.length == 1) {
                List<String> check = new ArrayList<String>();
                for(de.koppy.quest.api.Quest quest : de.koppy.quest.api.Quest.list) {
                    check.add(quest.getIdentifierName());
                }
                for(String s : check) if(s.startsWith(args[0])) tcomplete.add(s);
            }else if(args.length == 2) {
                List<String> check = new ArrayList<String>();
                for(Player all : Bukkit.getOnlinePlayers()) {
                    check.add(all.getName());
                }
                for(String s : check) if(s.startsWith(args[1])) tcomplete.add(s);
            }else if(args.length == 3) {
                List<String> check = new ArrayList<String>();
                check.add("stage-number");
                for(String s : check) if(s.startsWith(args[2])) tcomplete.add(s);
            }
        }
        return tcomplete;
    }
}