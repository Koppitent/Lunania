package de.koppy.job.listener;

import java.util.ArrayList;

import de.koppy.economy.api.PlayerAccount;
import de.koppy.job.api.JobType;
import de.koppy.job.api.PlayerJob;
import de.koppy.job.api.Task;
import de.koppy.lunaniasystem.LunaniaSystem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class Builder implements Listener {

    public static ArrayList<Location> placedblocks = new ArrayList<Location>();

    @EventHandler
    public void onBreak(final BlockPlaceEvent e) {
        if(e.getBlock().getLocation().getWorld().getName().equalsIgnoreCase("world")) {
            PlayerJob pj = new PlayerJob(e.getPlayer().getUniqueId());
            if(pj.getJob() != JobType.BUILDER) return;
            int taskid = 0;
            int mineddiamonds = pj.getTaskAmount().getTaskAmount(taskid) + 1;
            Task diamondtask = JobType.BUILDER.getTasksFromJob().get(taskid);
            if(placedblocks.contains(e.getBlock().getLocation())) return;
            placedblocks.add(e.getBlock().getLocation());
            Bukkit.getScheduler().runTaskLater(LunaniaSystem.getPlugin(), new Runnable() {
                public void run() {
                    placedblocks.remove(e.getBlock().getLocation());
                }
            }, 20*60*5);
            if(mineddiamonds >= diamondtask.getToachieve()) {
                pj.getTaskAmount().setTaskAmount(taskid, 0);
                pj.addXp(diamondtask.getXpgain(), JobType.BUILDER);
                double payout = diamondtask.getPayoutamount(pj.getLevel(JobType.BUILDER));
                new PlayerAccount(e.getPlayer().getUniqueId()).addMoney(payout, "from Server", "Placed 100 Blocks");
            }else {
                pj.getTaskAmount().setTaskAmount(taskid, mineddiamonds);
            }
        }
    }

}