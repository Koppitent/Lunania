package de.koppy.quest.api;

import de.koppy.basics.api.ItemBuilder;
import de.koppy.lunaniasystem.api.UI;
import org.bukkit.Material;

import java.util.UUID;

public class QuestUI extends UI {

    private UUID uuid;
    public QuestUI(UUID uuid) {
        super("§2Quest-Overview", 9*5);
        this.uuid = uuid;
    }

    public void getMenu(int page) {
        inventory.clear();
        setGlassRand(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayname("§d").getItemStack());
        //* 21 pro Seite
        int maxpage = Quest.list.size() / 21;
        if(Quest.list.size() % 21 != 0) maxpage++;
        if(page > maxpage) page = maxpage;
        page--;
        for(int i=0+(page*21); i<21+(page*20); i++) {
            if(Quest.list.size() == i) break;
            Quest quest = Quest.list.get(i);
            ItemBuilder ib = new ItemBuilder(quest.getMaterial()).setDisplayname("§6"+quest.getDisplayname());
            PlayerQuest pq = new PlayerQuest(uuid);
            int curstage = pq.getStage(quest.getIdentifierName());
            int x = 0;
            while(quest.getStages().containsKey(x)) {
                if(curstage > x) {
                    ib.addLore("§7○ §e§m"+quest.getStep(x).getName());
                    ib.addLore("§8.§7|");
                }else {
                    ib.addLore("§7○ §e"+quest.getStep(x).getName());
                    ib.addLore("§8.§7|");
                }
                x++;
            }
            inventory.addItem(ib.getItemStack());
        }
        if(page+1 != maxpage) inventory.setItem(inventory.getSize()-3, new ItemBuilder(Material.PLAYER_HEAD).setSkull("MHF_ArrowRight").setDisplayname("§3Next Site").setLocalizedName(""+(page+2)).getItemStack());
        if(page != 0) inventory.setItem(inventory.getSize()-7, new ItemBuilder(Material.PLAYER_HEAD).setSkull("MHF_ArrowLeft").setDisplayname("§3Last Site").setLocalizedName(""+(page)).getItemStack());
    }

}
