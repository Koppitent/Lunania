package de.koppy.job.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.koppy.basics.api.ItemBuilder;
import de.koppy.economy.EconomySystem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class JobMenu {

    private UUID uuid;
    public JobMenu(UUID uuid) {
        this.uuid = uuid;
    }

    public Inventory getMenu() {
        Inventory inventory = Bukkit.createInventory(null, 9*6, "JobMenu");
        return getMainMenu(inventory);
    }

    public Inventory getMainMenu(Inventory inventory) {
        inventory.clear();
        int i = 10;
        for(JobType job : JobType.values()) {
            if(job != JobType.NONE) {
                inventory.setItem(i, job.getItem(uuid));
                if((i-7) % 9 == 0) {
                    i = i + 3;
                }else {
                    i++;
                }
            }
        }
        return inventory;
    }

    public static ItemStack jobback = new ItemBuilder(Material.PAPER).setDisplayname("§cZurück").getItemStack();
    public static ItemStack jobleave = new ItemBuilder(Material.FLINT_AND_STEEL).setDisplayname("§cLeave Job").getItemStack();

    public Inventory getMenuJob(JobType job, Inventory inventory) {
        inventory.clear();
        List<Task> tasks = job.getTasksFromJob();
        JobXpLevel xplevel = job.getJobXpLevel();
        int maxlevel = xplevel.getMaxLevel();

        PlayerJob pj = new PlayerJob(uuid);
        int xp = pj.getXP(job);
        int level = pj.getLevel(job);

        ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta glassM = glass.getItemMeta();
        glassM.setDisplayName("§4");
        glass.setItemMeta(glassM);

        ItemStack hangingsign = new ItemStack(Material.OAK_HANGING_SIGN);
        ItemMeta hangingsignM = hangingsign.getItemMeta();
        hangingsignM.setDisplayName("§b"+job.toString());
        List<String> lore = new ArrayList<String>();
        lore.add(" ");
        lore.add("§7Current Rewards:");
        for(Task task : tasks) {
            lore.add("§7"+task.getTitle() + ": "+task.getXpgain()+"§3XP§7, "+task.getPayoutamount(level)+ EconomySystem.getEcosymbol());
        }
        hangingsignM.setLore(lore);
        hangingsign.setItemMeta(hangingsignM);

        inventory.setItem(10, hangingsign);

        inventory.setItem(0, glass);
        inventory.setItem(1, glass);
        inventory.setItem(2, glass);
        inventory.setItem(3, glass);
        inventory.setItem(4, glass);
        inventory.setItem(5, glass);
        inventory.setItem(6, glass);
        inventory.setItem(7, glass);
        inventory.setItem(8, glass);
        inventory.setItem(9, glass);

        inventory.setItem(11, glass);

        inventory.setItem(17, glass);
        inventory.setItem(18, glass);
        inventory.setItem(19, glass);
        inventory.setItem(20, glass);

        inventory.setItem(26, glass);
        inventory.setItem(27, glass);
        inventory.setItem(28, glass);
        inventory.setItem(29, glass);

        inventory.setItem(35, glass);
        inventory.setItem(36, glass);
        inventory.setItem(37, glass);
        inventory.setItem(38, glass);

        inventory.setItem(44, glass);
        inventory.setItem(45, glass);
        inventory.setItem(46, glass);
        inventory.setItem(47, glass);
        inventory.setItem(48, glass);
        inventory.setItem(49, glass);
        inventory.setItem(50, glass);
        inventory.setItem(51, glass);
        inventory.setItem(52, glass);
        inventory.setItem(53, glass);

        JobType playersjob = new PlayerJob(uuid).getJob();
        if(playersjob == job) {
            inventory.setItem(28, jobleave);
        }else {
            if(playersjob == JobType.NONE) inventory.setItem(28, new ItemBuilder(Material.LIME_DYE).setDisplayname("§aJoin §7Job").setLocalizedName(job.toString()).getItemStack());
        }
        inventory.setItem(37, jobback);

        for(int i = 0; i<maxlevel; i++) {
            ItemStack lvlup = new ItemStack(Material.RED_CONCRETE_POWDER);
            ItemMeta lvlupM = lvlup.getItemMeta();
            lvlupM.setDisplayName("§7Level "+(i+1));
            ArrayList<String> lore2 = new ArrayList<String>();
            if(i < level) {
                lvlup.setType(Material.LIME_CONCRETE_POWDER);
                lore2.add(" ");
                lore2.add("§7XP "+getXpPercent(2, 1)+" §7(100%) §3Completed!");
                lore2.add("§c§mold extramoney: "+((i)/10d)+"% -> new extrafee: " + ((i+1)/10d)+"%");
            }else if(i == level) {
                lvlup.setType(Material.YELLOW_CONCRETE_POWDER);
                lore2.add(" ");
                lore2.add("§7XP "+getXpPercent(xp, xplevel.xpNeededForLevelup(i))+" §c"+pj.getXP(job)+"§7/"+xplevel.xpNeededForLevelup(i) + " §7(" + (int) (((double) xp / (double) xplevel.xpNeededForLevelup(level)) * 100) + "%§7)");
                lore2.add("§cold extramoney: §7"+((i)/10d)+"% §8-> §anew extrafee: §7" + ((i+1)/10d)+"%");
            }else {
                lore2.add(" ");
                lore2.add("§cnot unlocked yet!");
                lore2.add(" ");
                lore2.add("§cold extramoney: §7"+((i)/10d)+"% §8-> §anew extrafee: §7" + ((i+1)/10d)+"%");
            }

            if(i == 9) {
                lvlup.setType(Material.GOLD_BLOCK);
                lore2.add("§3+5 Lands");
                lore2.add("§3+1 Home");
            }

            lvlupM.setLore(lore2);
            lvlup.setItemMeta(lvlupM);
            inventory.addItem(lvlup);
        }

        return inventory;
    }

    public String getXpPercent(int xphave, int xpneeded) {
        double percent = (double) xphave / (double) xpneeded;
        if(percent < 0.1) {
            return "§7----------";
        }else if(percent < 0.2) {
            return "§3-§7---------";
        }else if(percent < 0.3) {
            return "§3--§7--------";
        }else if(percent < 0.4) {
            return "§3---§7-------";
        }else if(percent < 0.5) {
            return "§3----§7------";
        }else if(percent < 0.6) {
            return "§3-----§7-----";
        }else if(percent < 0.7) {
            return "§3------§7----";
        }else if(percent < 0.8) {
            return "§3-------§7---";
        }else if(percent < 0.9) {
            return "§3--------§7--";
        }else if(percent < 0.9) {
            return "§3---------§7-";
        }else {
            return "§3----------";
        }
    }

}