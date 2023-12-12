package de.koppy.quest;

import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.mysql.api.Column;
import de.koppy.mysql.api.ColumnType;
import de.koppy.mysql.api.Table;
import de.koppy.quest.commands.Quest;
import de.koppy.server.SubSystem;
import de.lunania.quest.listener.QuestEvents;

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
        new de.lunania.quest.api.Quest("tutorial", "§3Tutorial", 5);

        table = new Table("quests", new Column("uuid", ColumnType.VARCHAR, 200));
        for(de.lunania.quest.api.Quest quest : de.lunania.quest.api.Quest.list) {
            table.addColumn(new Column(quest.getName(), ColumnType.INT, 200));
        }
        table.createTable();

        loadCommands();
        loadListener();
    }

    public static Table getTable() {
        return table;
    }
}
