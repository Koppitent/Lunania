package de.koppy.bansystem.api;

import java.util.ArrayList;
import java.util.List;

public class History {

    private final List<HistoryElement> history;
    public History(String historystring) {
        history = new ArrayList<>();
        if(historystring == null) return;
        if(!historystring.contains(";")) return;
        for(String s : historystring.substring(0, historystring.length()-1).split(";")) {
            history.add(HistoryElement.fromString(s));
        }
    }

    public List<HistoryElement> getList() {
        return history;
    }

    public HistoryElement getEntry(int index) {
        return history.get(index);
    }

}