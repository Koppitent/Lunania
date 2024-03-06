package de.koppy.quest.api;

import de.koppy.basics.api.ItemBuilder;
import de.koppy.lunaniasystem.api.UI;
import org.bukkit.Material;

public class QuestAdminUI extends UI {

    public QuestAdminUI() {
        super("Quest-List", 9*5);
    }

    public void getQuestList(){
        inventory.clear();
        setGlassRand(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayname("§c").getItemStack());
        for(Quest quests : Quest.list) {
            inventory.addItem(new ItemBuilder(quests.getMaterial()).setDisplayname("§3"+quests.getDisplayname()).addLore(""+quests.getStages()).getItemStack());
        }
    }

}
