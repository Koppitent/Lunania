package de.koppy.basics.commands;

import de.koppy.basics.BasicSystem;
import de.koppy.basics.api.Language;
import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.server.Column;
import de.koppy.server.ColumnType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Languageset implements CommandExecutor, TabCompleter {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player))
            return false;
        Player player = (Player) sender;

        if(player.hasPermission("server.admin")) {

            if(args.length >= 3) {
                String languagestring = args[0];
                if(de.koppy.basics.api.Language.isLanguage(languagestring)) {
                    de.koppy.basics.api.Language language = de.koppy.basics.api.Language.fromString(languagestring);
                    String abbreviation = args[1];
                    String message = "";
                    for(int i=2; i<args.length; i++) {
                        message = message + args[i] + " ";
                    }
                    setAbbreviation(language, abbreviation, message);
                    player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§7Message was set for Language §e"+ language.toString().toLowerCase() + " §7and abbreviation §e"+abbreviation+"§7.");
                }else {
                    player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§cLanguage doesn't exist. Use: /langset <language> <abbrevation> <message>");
                }
            }else {
                player.sendMessage(LunaniaSystem.getServerInstance().getPrefix() + "§cSyntax error: /langset <language> <abbrevation> <message>");
            }

        }

        return false;
    }

    public static void setAbbreviation(Language language, String abbreviation, String message) {
        Column langcolumn = new Column(language.toString().toLowerCase(), ColumnType.VARCHAR, 200);
        Column abbreviationc = new Column("abbreviation", ColumnType.VARCHAR, 200);
        BasicSystem.getLangtable().setValue(langcolumn, message, abbreviationc, abbreviation);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        List<String> out = new ArrayList<>();
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(player.hasPermission("server.admin.languageset")) {
                if(args.length == 1) {
                    for(Language language : Language.values()) {
                        out.add(language.toString().toLowerCase());
                    }
                }else if(args.length == 2) {
                    out.add("abbreviation");
                }else if(args.length >= 3) {
                    out.add("text");
                }
            }
        }
        return out;
    }
}