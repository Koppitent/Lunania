package de.koppy.job.listener;

import java.util.ArrayList;

import de.koppy.economy.api.PlayerAccount;
import de.koppy.job.api.JobType;
import de.koppy.job.api.PlayerJob;
import de.koppy.job.api.Task;
import de.koppy.job.api.TaskAchievedEvent;
import de.koppy.lunaniasystem.LunaniaSystem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class Woodcutter implements Listener {

    public static ArrayList<Location> alreadybroken = new ArrayList<Location>();

    @EventHandler
    public void onBreak(final BlockBreakEvent e) {
        if(!e.getBlock().getLocation().getWorld().getName().equalsIgnoreCase("world")) {
            if(alreadybroken.contains(e.getBlock().getLocation())) return;
            if(e.getBlock().getType() == Material.BIRCH_LOG || e.getBlock().getType() == Material.OAK_LOG) {
                checkTaskByID(0, e.getPlayer(), e.getBlock().getLocation());
            }else if(e.getBlock().getType() == Material.SPRUCE_LOG || e.getBlock().getType() == Material.ACACIA_LOG) {
                checkTaskByID(1, e.getPlayer(), e.getBlock().getLocation());
            }else if(e.getBlock().getType() == Material.DARK_OAK_LOG || e.getBlock().getType() == Material.JUNGLE_LOG) {
                checkTaskByID(2, e.getPlayer(), e.getBlock().getLocation());
            }else if(e.getBlock().getType() == Material.MANGROVE_LOG || e.getBlock().getType() == Material.CHERRY_LOG) {
                checkTaskByID(3, e.getPlayer(), e.getBlock().getLocation());
            }
        }
    }

    public void checkTaskByID(int taskid, Player player, final Location location) {
        PlayerJob pj = new PlayerJob(player.getUniqueId());
        if(pj.getJob() != JobType.WOODCUTTER) return;
        alreadybroken.add(location);
        Bukkit.getScheduler().runTaskLater(LunaniaSystem.getPlugin(), new Runnable() {
            public void run() {
                alreadybroken.remove(location);
            }
        }, 20*60*5);
        int cuttedoak = pj.getTaskAmount().getTaskAmount(taskid) + 1;
        Task cuttask = JobType.WOODCUTTER.getTasksFromJob().get(taskid);
        if(cuttedoak >= cuttask.getToachieve()) {
            pj.getTaskAmount().setTaskAmount(taskid, 0);
            pj.addXp(cuttask.getXpgain(), JobType.WOODCUTTER);
            double payout = cuttask.getPayoutamount(pj.getLevel(JobType.WOODCUTTER));
            new PlayerAccount(player.getUniqueId()).addMoney(payout, "from Server", "Cutted Wood");
            Bukkit.getServer().getPluginManager().callEvent(new TaskAchievedEvent(player, JobType.WOODCUTTER, payout, cuttask.getXpgain()));
        }else {
            pj.getTaskAmount().setTaskAmount(taskid, cuttedoak);
        }
    }

}