package de.koppy.quest;

import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.mysql.api.Column;
import de.koppy.mysql.api.ColumnType;
import de.koppy.mysql.api.Table;
import de.koppy.quest.commands.Quest;
import de.koppy.quest.listener.QuestEvents;
import de.koppy.quest.quests.TutorialQuest;
import de.koppy.server.SubSystem;

public class QuestSystem implements SubSystem {

    private static Table table;
    @Override
    public void loadListener() {
        LunaniaSystem.registerListener(new QuestEvents());
    }

    @Override
    public void loadCommands() {
        LunaniaSystem.registerCommand("quest", new Quest());
    }

    @Override
    public void loadClasses() {
        new TutorialQuest();

        table = new Table("quests", new Column("uuid", ColumnType.VARCHAR, 200));
        for(de.koppy.quest.api.Quest quest : de.koppy.quest.api.Quest.list) {
            table.addColumn(new Column(quest.getIdentifierName(), ColumnType.INT, 200));
        }
        table.createTable();

        loadCommands();
        loadListener();
    }

    public static Table getTable() {
        return table;
    }
}
