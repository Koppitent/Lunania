package de.koppy.basics.inventories;

import de.koppy.basics.api.ChangelogItem;
import de.koppy.basics.api.ItemBuilder;
import de.koppy.basics.api.menu.Rows;
import de.koppy.basics.api.menu.SimpleMenu;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class ChangelogInv extends SimpleMenu {

    private final List<ChangelogItem> changelogItems = new ArrayList<>();
    private int site = 1;
    public ChangelogInv(List<ChangelogItem> changelogItems) {
        super("§eChangelog", Rows.THREE);
        this.changelogItems.addAll(changelogItems);
    }

    @Override
    public void onSetItems() {
        setRand();
        int start = 9*(site-1);
        int end = (9*(site-1))+9;
        int position = 9;
        for(int i = start; i < end; i++) {
            if(changelogItems.size() <= i) break;
            getInventory().setItem(position, changelogItems.get(i).getBook());
            position++;
        }
    }

    private void setRand() {
        for(int i=0; i<9; i++) {
            setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayname("§e").getItemStack());
        }
        for(int i=getInventory().getSize()-9; i<getInventory().getSize(); i++) {
            setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayname("§e").getItemStack());
        }
        if(site > 1) {
            setItem(18, new ItemBuilder(Material.PLAYER_HEAD).setSkull("MHF_ARROW_LEFT").setDisplayname("Zu Seite " + (site - 1)).getItemStack(), player -> {
                site--;
                update();
            });
        }
        if(site < ((int) Math.ceil((double) changelogItems.size() / 9d))) {
            setItem(26, new ItemBuilder(Material.PLAYER_HEAD).setSkull("MHF_ARROW_RIGHT").setDisplayname("Zu Seite " + (site + 1)).getItemStack(), player -> {
                site++;
                update();
            });
        }
    }

}
