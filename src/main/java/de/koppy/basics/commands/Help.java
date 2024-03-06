package de.koppy.basics.commands;

import de.koppy.basics.api.Page;
import de.koppy.basics.api.WrittenBook;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Help implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        WrittenBook book = new WrittenBook();
        Page page1 = new Page();
        page1.addToPage("§a§lHilfe");
        page1.addToPage("§8§m--------------");
        page1.addToPage("§7Willkommen auf Lunania. Hier sind deiner Kreativität keine Grenzen gesetz! Auf den nächsten Seiten erhälst du alle Infos über die implementierten Systeme. Viel Spaß ;)");
        book.addPage(page1);
        Page page2 = new Page();
        page2.addToPage("§7We are currently still in Pre-ALPHA.");
        page2.addToPage("§7");
        page2.addToPage("§7If you want to see the latest changes type §d/changelog");
        book.addPage(page2);
        player.openBook(book.getBook());
        return false;
    }
}
