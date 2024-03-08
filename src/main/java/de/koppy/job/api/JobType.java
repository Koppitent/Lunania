package de.koppy.job.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum JobType {

    MINER, BUILDER, WOODCUTTER, NONE;

    public static JobType fromString(String string) {
        for(JobType jt : JobType.values()) {
            if(jt.toString().equalsIgnoreCase(string)) {
                return jt;
            }
        }
        return null;
    }

    public ItemStack getItem(UUID uuid) {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta itemM = item.getItemMeta();
        itemM.setLocalizedName(this.toString());

        switch (this) {
            case WOODCUTTER:
                item.setType(Material.WOODEN_AXE);
                itemM.setDisplayName("§3Woodcutter");
                break;
            case BUILDER:
                item.setType(Material.GRASS_BLOCK);
                itemM.setDisplayName("§3Builder");
                break;
            case MINER:
                item.setType(Material.GOLDEN_PICKAXE);
                itemM.setDisplayName("§3Miner");
                break;
            case NONE:
                item.setType(Material.AIR);
                break;
            default:
                item.setType(Material.AIR);
                break;
        }

        if(new PlayerJob(uuid).getJob() == this) {
            itemM.addEnchant(Enchantment.ARROW_DAMAGE, 2, true);
            itemM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        item.setItemMeta(itemM);
        return item;
    }

    public List<Task> getTasksFromJob() {
        List<Task> tasks = new ArrayList<Task>();
        if(this == JobType.MINER) {
            Task task1 = new Task("Mine Diamonds", "Mine Diamonds in the farmworld for money and xp.", 4, 5d, 15, this);
            Task task2 = new Task("Mine Iron/Gold", "Mine Iron/Gold in the farmworld for money and xp.", 32, 10d, 15, this);
            Task task3 = new Task("Mine Redstone/Lapis", "Mine Redstone/Lapis in the farmworld for money and xp.", 32, 5d, 15, this);
            Task task4 = new Task("Mine Coal/Copper", "Mine Coal/Copper in the farmworld for money and xp.", 25, 6.5d, 15, this);
            Task task5 = new Task("Mine Emeralds", "Mine Emeralds in the farmworld for money and xp.", 2, 5d, 15, this);
            tasks.add(task1);
            tasks.add(task2);
            tasks.add(task3);
            tasks.add(task4);
            tasks.add(task5);
        }else if(this == JobType.BUILDER) {
            Task task1 = new Task("Place Blocks", "Place unique blocks on your land.", 100, 5d, 10, this);
            tasks.add(task1);
        }else if(this == JobType.WOODCUTTER) {
            Task task1 = new Task("Cut Oak/Birch-Logs", "Cut Oak or Birch-Logs", 32, 5d, 15, this);
            Task task2 = new Task("Cut Acacia/Spruce-Logs", "Cut Acacia/Spruce-Logs", 32, 5d, 15, this);
            Task task3 = new Task("Cut DarkOak/Jungle-Logs", "Cut DarkOak/Jungle-Logs", 32, 5d, 15, this);
            Task task4 = new Task("Cut Mangrove/Cherry-Logs", "Cut Mangrove/Cherry-Logs", 32, 5d, 20, this);
            tasks.add(task1);
            tasks.add(task2);
            tasks.add(task3);
            tasks.add(task4);
        }
        return tasks;
    }

    public int getTaskAmount() {
        return getTasksFromJob().size();
    }

    public JobXpLevel getJobXpLevel() {
        JobXpLevel jobxplevel = new JobXpLevel();
        if(this == JobType.MINER) {
            jobxplevel.addLevel(20);
            jobxplevel.addLevel(50);
            jobxplevel.addLevel(100);
            jobxplevel.addLevel(500);
            jobxplevel.addLevel(600);
            jobxplevel.addLevel(700);
            jobxplevel.addLevel(800);
            jobxplevel.addLevel(900);
            jobxplevel.addLevel(1000);
            jobxplevel.addLevel(1500);
        }else if(this == JobType.BUILDER) {
            jobxplevel.addLevel(20);
            jobxplevel.addLevel(50);
            jobxplevel.addLevel(100);
            jobxplevel.addLevel(500);
            jobxplevel.addLevel(600);
            jobxplevel.addLevel(700);
            jobxplevel.addLevel(800);
            jobxplevel.addLevel(900);
            jobxplevel.addLevel(1000);
            jobxplevel.addLevel(1500);
        }else if(this == JobType.WOODCUTTER) {
            jobxplevel.addLevel(20);
            jobxplevel.addLevel(50);
            jobxplevel.addLevel(100);
            jobxplevel.addLevel(500);
            jobxplevel.addLevel(600);
            jobxplevel.addLevel(700);
            jobxplevel.addLevel(800);
            jobxplevel.addLevel(900);
            jobxplevel.addLevel(1000);
            jobxplevel.addLevel(1500);
        }
        return jobxplevel;
    }

}