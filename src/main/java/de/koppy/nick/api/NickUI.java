package de.koppy.nick.api;

import de.koppy.basics.api.ItemBuilder;
import de.koppy.lunaniasystem.api.UI;
import org.bukkit.Material;

import java.util.UUID;

public class NickUI extends UI {

    private UUID uuid;
    public NickUI(UUID uuid) {
        super("§cNickUI", 9*3);
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void getMenu() {
        inventory.clear();
        fillGlass(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayname("§d").getItemStack());
        inventory.setItem(10, new ItemBuilder(Material.GREEN_CONCRETE).setDisplayname("§7Unnick").getItemStack());
        inventory.setItem(13, new ItemBuilder(Material.RED_CONCRETE).setDisplayname("§7Nick randomly.").addLore("§b-> §7with Skin").addLore("§b-> §7without Skin §o(Shiftclick)").getItemStack());
        inventory.setItem(16, new ItemBuilder(Material.PAPER).setDisplayname("§7Select a predefined Nick.").getItemStack());
    }

    public void applyNick(String nickidentifier) {

    }

}