package de.koppy.basics.commands;

import java.util.ArrayList;
import java.util.List;

import de.koppy.basics.api.LanguageUI;
import de.koppy.basics.api.PlayerProfile;
import de.koppy.lunaniasystem.LunaniaSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class Language implements CommandExecutor, TabCompleter {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player))
            return false;
        Player player = (Player) sender;

        if(args.length == 1) {
            String language = args[0];
            if(de.koppy.basics.api.Language.isLanguage(language)) {
                PlayerProfile profile = PlayerProfile.getProfile(player.getUniqueId());
                de.koppy.basics.api.Language lang = de.koppy.basics.api.Language.fromString(language);
                profile.setLanguage(lang);
                player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§7Dein Sprache wurde zu §e" + lang.toString() + " §7geändert.");
            }else {
                player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§cDiese Sprache ist nicht verf§gbar oder existiert nicht.");
            }
            return false;
        }

        LanguageUI langui = new LanguageUI(player.getUniqueId());
        langui.getLanguageSelection();
        player.openInventory(langui.getInventory());

        return false;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> tcomplete = new ArrayList<String>();
        if(sender instanceof Player) {
            if(args.length == 1) {
                List<String> check = new ArrayList<String>();
                for(de.koppy.basics.api.Language lang : de.koppy.basics.api.Language.values()) {
                    check.add(lang.toString().toLowerCase());
                }
                for(String s : check) if(s.startsWith(args[0])) tcomplete.add(s);
            }
        }
        return tcomplete;
    }

}