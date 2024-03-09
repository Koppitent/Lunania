package de.koppy.quest.quests;

import de.koppy.quest.api.Quest;
import de.koppy.quest.api.Step;
import org.bukkit.Material;

public class TutorialQuest extends Quest {

    public TutorialQuest() {
        super("tutorial", "Tutorial");
        setStage(0, new Step("Go to Joe", "Go to Joes whos at spawn right now"));
        setStage(1, new Step("Go to Farmworld", "Go to Farmworld by doing /warp farmworld.\nJoe is waiting there for you."));
        setStage(2, new Step("Give Crimson to Joe in the Nether", "Go to Nether and collect Crimson to give it to Joe."));
    }

}