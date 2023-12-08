package de.lunania.quest.api;

import java.util.ArrayList;
import java.util.List;

public class Quest {

    String name;
    String displayname;
    int stages;

    public static List<Quest> list = new ArrayList<Quest>();

    public Quest(String name, String displayname, int stages) {
        this.name = name;
        this.displayname = displayname;
        this.stages = stages;
        list.add(this);
    }

    public String getDisplayname() {
        return displayname;
    }

    public String getName() {
        return name;
    }

    public int getStages() {
        return stages;
    }

}