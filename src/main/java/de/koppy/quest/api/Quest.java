package de.koppy.quest.api;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Quest {

    public static List<Quest> list = new ArrayList<Quest>();

    public String identifiername;
    private String displayname;
    public HashMap<Integer, Step> stages = new HashMap<>();
    public Material material = Material.BOOK;

    public Quest(String identifiername, String displayname) {
        this.identifiername = identifiername;
        this.displayname = displayname;
        this.stages = stages;
        list.add(this);
    }

    public Quest(String identifiername, String displayname, Material material) {
        this.identifiername = identifiername;
        this.displayname = displayname;
        this.stages = stages;
        this.material = material;
        list.add(this);
    }

    public String getDisplayname() {
        return displayname;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String getIdentifierName() {
        return identifiername;
    }

    public void setStage(int stage, Step step) {
        stages.put(stage, step);
    }

    public Step getStep(int stage) {
        return stages.get(stage);
    }

    public HashMap<Integer, Step> getStages() {
        return stages;
    }

    public static Quest getQuest(String questidname) {
        if(questidname.trim().isEmpty()) return null;
        for(Quest quest : list) {
            if(quest.getIdentifierName().equalsIgnoreCase(questidname)) {
                return quest;
            }
        }
        return null;
    }

}