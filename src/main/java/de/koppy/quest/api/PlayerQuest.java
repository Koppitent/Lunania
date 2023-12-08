package de.koppy.quest.api;

import de.koppy.mysql.api.Column;
import de.koppy.mysql.api.ColumnType;
import de.koppy.quest.QuestSystem;

import java.util.UUID;

public class PlayerQuest {

    private UUID uuid;
    public PlayerQuest(UUID uuid) {
        this.uuid = uuid;
    }

    public int getStage(String questname) {
        Column uuidc = new Column("uuid", ColumnType.VARCHAR, 200);
        Column questc = new Column(questname, ColumnType.INT, 200);
        if(QuestSystem.getTable().existEntry(questc, uuidc, uuid.toString())) {
            return (Integer) QuestSystem.getTable().getValue(questc, uuidc, uuid.toString());
        }else {
            return 0;
        }
    }

    public void addStage(String questname) {
        Column uuidc = new Column("uuid", ColumnType.VARCHAR, 200);
        Column questc = new Column(questname, ColumnType.INT, 200);
        int i = getStage(questname);
        i++;
        QuestSystem.getTable().setValue(questc, i, uuidc, uuid.toString());
    }

    public void setStage(String questname, int i) {
        Column uuidc = new Column("uuid", ColumnType.VARCHAR, 200);
        Column questc = new Column(questname, ColumnType.INT, 200);
        QuestSystem.getTable().setValue(questc, i, uuidc, uuid.toString());
    }

}