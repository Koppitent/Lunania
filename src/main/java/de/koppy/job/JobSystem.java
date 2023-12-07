package de.koppy.job;

import de.koppy.job.api.JobType;
import de.koppy.job.commands.Job;
import de.koppy.job.listener.Builder;
import de.koppy.job.listener.Inventory;
import de.koppy.job.listener.Miner;
import de.koppy.job.listener.Woodcutter;
import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.mysql.api.Column;
import de.koppy.mysql.api.ColumnType;
import de.koppy.server.SubSystem;
import de.koppy.mysql.api.Table;

public class JobSystem implements SubSystem {

    private static Table table;

    @Override
    public void loadListener() {
        LunaniaSystem.registerListener(new Miner());
        LunaniaSystem.registerListener(new Woodcutter());
        LunaniaSystem.registerListener(new Builder());
        LunaniaSystem.registerListener(new Inventory());
    }

    @Override
    public void loadCommands() {
        LunaniaSystem.registerCommand("job", new Job());
        LunaniaSystem.registerCommand("jobs", new Job());
    }

    @Override
    public void loadClasses() {
        loadCommands();
        loadListener();
        table = new Table("jobs", new Column("uuid", ColumnType.VARCHAR, 200));
        table.addColumn(new Column("currentjob", ColumnType.VARCHAR, 200));
        for(JobType jobtype : JobType.values()) {
            if(jobtype != JobType.NONE) {
                table.addColumn(new Column(jobtype.toString(), ColumnType.VARCHAR, 200));
            }
        }
        table.createTable();
    }

    public static Table getTable() {
        return table;
    }
}
