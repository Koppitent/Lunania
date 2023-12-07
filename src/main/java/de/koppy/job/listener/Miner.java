package de.koppy.job.listener;

import java.util.ArrayList;

import de.koppy.economy.api.PlayerAccount;
import de.koppy.job.api.JobType;
import de.koppy.job.api.PlayerJob;
import de.koppy.job.api.Task;
import de.koppy.lunaniasystem.LunaniaSystem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class Miner implements Listener {

    public static ArrayList<Location> alreadybroken = new ArrayList<Location>();

    @EventHandler
    public void onBreak(final BlockBreakEvent e) {
        if(e.getBlock().getLocation().getWorld().getName().equalsIgnoreCase("world")) return;
        if(alreadybroken.contains(e.getBlock().getLocation())) return;
        if(e.getBlock().getType() == Material.DIAMOND_ORE || e.getBlock().getType() == Material.DEEPSLATE_DIAMOND_ORE) {
            checkTaskByID(0, e.getPlayer(), e.getBlock().getLocation());
        }else if(e.getBlock().getType() == Material.IRON_ORE || e.getBlock().getType() == Material.GOLD_ORE || e.getBlock().getType() == Material.DEEPSLATE_IRON_ORE || e.getBlock().getType() == Material.DEEPSLATE_GOLD_ORE) {
            checkTaskByID(1, e.getPlayer(), e.getBlock().getLocation());
        }else if(e.getBlock().getType() == Material.LAPIS_ORE || e.getBlock().getType() == Material.DEEPSLATE_LAPIS_ORE || e.getBlock().getType() == Material.DEEPSLATE_REDSTONE_ORE || e.getBlock().getType() == Material.REDSTONE_ORE) {
            checkTaskByID(2, e.getPlayer(), e.getBlock().getLocation());
        }else if(e.getBlock().getType() == Material.COAL_ORE || e.getBlock().getType() == Material.DEEPSLATE_COAL_ORE || e.getBlock().getType() == Material.DEEPSLATE_COPPER_ORE || e.getBlock().getType() == Material.COPPER_ORE) {
            checkTaskByID(3, e.getPlayer(), e.getBlock().getLocation());
        }else if(e.getBlock().getType() == Material.EMERALD_ORE || e.getBlock().getType() == Material.DEEPSLATE_EMERALD_ORE) {
            checkTaskByID(4, e.getPlayer(), e.getBlock().getLocation());
        }
    }

    public void checkTaskByID(int taskid, Player player, final Location location) {
        PlayerJob pj = new PlayerJob(player.getUniqueId());
        if(pj.getJob() != JobType.MINER) return;
        alreadybroken.add(location);
        Bukkit.getScheduler().runTaskLater(LunaniaSystem.getPlugin(), new Runnable() {
            public void run() {
                alreadybroken.remove(location);
            }
        }, 20*60*5);
        int mineddiamonds = pj.getTaskAmount().getTaskAmount(taskid) + 1;
        Task diamondtask = JobType.MINER.getTasksFromJob().get(taskid);
        if(mineddiamonds >= diamondtask.getToachieve()) {
            pj.getTaskAmount().setTaskAmount(taskid, 0);
            pj.addXp(diamondtask.getXpgain(), JobType.MINER);
            double payout = diamondtask.getPayoutamount(pj.getLevel(JobType.MINER));
            new PlayerAccount(player.getUniqueId()).addMoney(payout, "from Server", "Mining job");
        }else {
            pj.getTaskAmount().setTaskAmount(taskid, mineddiamonds);
        }
    }

}