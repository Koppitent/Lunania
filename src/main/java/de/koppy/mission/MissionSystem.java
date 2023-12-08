package de.koppy.mission;

import de.koppy.cases.api.Case;
import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.mission.api.Daily;
import de.koppy.mission.api.Seasonal;
import de.koppy.mission.api.Weekly;
import de.koppy.mission.commands.Mission;
import de.koppy.mission.listener.MissionEvents;
import de.koppy.mysql.api.Column;
import de.koppy.mysql.api.ColumnType;
import de.koppy.server.SubSystem;
import de.koppy.mysql.api.Table;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MissionSystem implements SubSystem {

    //TODO: change newdaycheck by checking and saving dates!
    //TODO: add more and unique Missions
    //TODO: add better UserInterface
    private static Table table;
    @Override
    public void loadListener() {
        LunaniaSystem.registerListener(new MissionEvents());
    }

    @Override
    public void loadCommands() {
        LunaniaSystem.registerCommand("mission", new Mission());
    }

    @Override
    public void loadClasses() {
        loadCommands();
        loadListener();

        ItemStack[] i = {new ItemStack(Material.IRON_INGOT, 8), Case.getCasebyName("Beta").getCase()};
        ItemStack[] rewardsdaily = {new ItemStack(Material.COBBLESTONE, 64), Case.getCasebyName("Basic").getCase(), Case.getCasebyName("Basic").getCase(), Case.getCasebyName("Basic").getCase(), Case.getCasebyName("Basic").getCase()};

        new Daily("cows", "Kill Cows", "You have to kill 4 Cows!", new ItemStack(Material.COW_SPAWN_EGG), 4, rewardsdaily) ;
        new Daily("zombies", "Kill Zombies", "You have to kill 2 Zombies!", new ItemStack(Material.ZOMBIE_SPAWN_EGG), 2, rewardsdaily);
        new Daily("pigs","Kill Pigs", "You have to kill 3 Pigs!", new ItemStack(Material.PIG_SPAWN_EGG), 3, rewardsdaily);
        new Daily("skelletons", "Kill Skelletons", "You have to kill 6 Skelletons!", new ItemStack(Material.SKELETON_SPAWN_EGG), 6, rewardsdaily);
        new Daily("lama", "Kill Lama", "You have to kill 1 Lama!", new ItemStack(Material.LLAMA_SPAWN_EGG), 1, rewardsdaily);

        new Weekly("cowsw", "Kill Cows", "You have to kill 4 Cows!", new ItemStack(Material.COW_SPAWN_EGG), 4, i);
        new Weekly("zombiesw", "Kill Zombies", "You have to kill 2 Zombies!", new ItemStack(Material.ZOMBIE_SPAWN_EGG), 2, i);
        new Weekly("pigsw","Kill Pigs", "You have to kill 3 Pigs!", new ItemStack(Material.PIG_SPAWN_EGG), 3, i);
        new Weekly("lamaw", "Kill Lama", "You have to kill 1 Lama!", new ItemStack(Material.LLAMA_SPAWN_EGG), 1, i);

        new Seasonal("cowsS", "Kill Cows", "You have to kill 4 Cows!", new ItemStack(Material.COW_SPAWN_EGG), 4, i);
        new Seasonal("cows2S", "Kill Cows", "You have to kill 5 Cows!", new ItemStack(Material.COW_SPAWN_EGG), 5, i);


        table = new Table("missions", new Column("uuid", ColumnType.VARCHAR, 200));
        table.addColumn(new Column("dailymissions", ColumnType.VARCHAR, 200));
        table.addColumn(new Column("weeklymissions", ColumnType.VARCHAR, 200));
        table.addColumn(new Column("seasonalmissions", ColumnType.VARCHAR, 200));
        table.addColumn(new Column("datedaily", ColumnType.INT, 200));
        table.addColumn(new Column("dateweekly", ColumnType.INT, 200));
        table.addColumn(new Column("dateseason", ColumnType.INT, 200));
        table.createTable();

    }

    public static Table getTable() {
        return table;
    }
}
