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

        ItemStack[] rewardsweekly = {new ItemStack(Material.IRON_INGOT, 32), Case.getCasebyName("Basic").getCase(), Case.getCasebyName("Basic").getCase(), Case.getCasebyName("Basic").getCase(), Case.getCasebyName("Basic").getCase(), Case.getCasebyName("Basic").getCase()};
        ItemStack[] rewardsdaily = {new ItemStack(Material.COBBLESTONE, 64), Case.getCasebyName("Basic").getCase(), Case.getCasebyName("Basic").getCase(), Case.getCasebyName("Basic").getCase(), Case.getCasebyName("Basic").getCase()};

        new Daily("cows", "Kill Cows", "You have to kill 20 Cows!", new ItemStack(Material.COW_SPAWN_EGG), 20, rewardsdaily) ;
        new Daily("zombies", "Kill Zombies", "You have to kill 50 Zombies!", new ItemStack(Material.ZOMBIE_SPAWN_EGG), 50, rewardsdaily);
        new Daily("pigs","Kill Pigs", "You have to kill 15 Pigs!", new ItemStack(Material.PIG_SPAWN_EGG), 15, rewardsdaily);
        new Daily("skelletons", "Kill Skelletons", "You have to kill 30 Skeletons!", new ItemStack(Material.SKELETON_SPAWN_EGG), 30, rewardsdaily);
        new Daily("lama", "Kill Lama", "You have to kill 4 Lama!", new ItemStack(Material.LLAMA_SPAWN_EGG), 4, rewardsdaily);
        new Daily("oak", "Cut Oak", "You have to cut 100 Oak!", new ItemStack(Material.OAK_LOG), 100, rewardsdaily);
        new Daily("spruce", "Cut Spruce", "You have to cut 100 Spruce!", new ItemStack(Material.SPRUCE_LOG), 100, rewardsdaily);
        new Daily("acacia", "Cut Acacia", "You have to cut 100 Acacia!", new ItemStack(Material.ACACIA_LOG), 100, rewardsdaily);

        new Weekly("cowsw", "Kill Cows", "You have to kill 200 Cows!", new ItemStack(Material.COW_SPAWN_EGG), 200, rewardsweekly);
        new Weekly("zombiesw", "Kill Zombies", "You have to kill 500 Zombies!", new ItemStack(Material.ZOMBIE_SPAWN_EGG), 500, rewardsweekly);
        new Weekly("pigsw","Kill Pigs", "You have to kill 150 Pigs!", new ItemStack(Material.PIG_SPAWN_EGG), 150, rewardsweekly);
        new Weekly("skelletonsw", "Kill Skelletons", "You have to kill 300 Skeletons!", new ItemStack(Material.SKELETON_SPAWN_EGG), 300, rewardsweekly);
        new Weekly("lamaw", "Kill Lama", "You have to kill 40 Lama!", new ItemStack(Material.LLAMA_SPAWN_EGG), 40, rewardsweekly);
        new Weekly("oakw", "Cut Oak", "You have to cut 1.000 Oak!", new ItemStack(Material.OAK_LOG), 1000, rewardsweekly);
        new Weekly("sprucew", "Cut Spruce", "You have to cut 1.000 Spruce!", new ItemStack(Material.SPRUCE_LOG), 1000, rewardsweekly);
        new Weekly("acaciaw", "Cut Acacia", "You have to cut 1.000 Acacia!", new ItemStack(Material.ACACIA_LOG), 1000, rewardsweekly);

        new Seasonal("cowsS", "Kill Cows", "You have to kill 4 Cows!", new ItemStack(Material.COW_SPAWN_EGG), 4, rewardsdaily);
        new Seasonal("cows2S", "Kill Cows", "You have to kill 5 Cows!", new ItemStack(Material.COW_SPAWN_EGG), 5, rewardsdaily);


        table = new Table("missions", new Column("uuid", ColumnType.VARCHAR, 200));
        table.addColumn(new Column("dailymissions", ColumnType.VARCHAR, 200));
        table.addColumn(new Column("weeklymissions", ColumnType.VARCHAR, 200));
        table.addColumn(new Column("seasonalmissions", ColumnType.VARCHAR, 200));
        table.addColumn(new Column("datedaily", ColumnType.INT, 200));
        table.addColumn(new Column("dateweekly", ColumnType.INT, 200));
        table.addColumn(new Column("dateseason", ColumnType.INT, 200));
        table.createTable();

        loadCommands();
        loadListener();
    }

    public static Table getTable() {
        return table;
    }
}
