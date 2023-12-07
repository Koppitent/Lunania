package de.koppy.job.commands;

import java.text.DecimalFormat;
import java.util.ArrayList;

import de.koppy.basics.api.PlayerProfile;
import de.koppy.job.api.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Job implements CommandExecutor {

    public static ArrayList<Player> inmenu = new ArrayList<Player>();
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player))
            return false;
        Player player = (Player) sender;

        if(args.length == 0) {

            player.openInventory(new JobMenu(player.getUniqueId()).getMenu());
            inmenu.add(player);

        }else if(args.length == 1) {
            if(args[0].equalsIgnoreCase("info")) {

                PlayerJob pj = new PlayerJob(player.getUniqueId());
                JobType jtype = pj.getJob();
                player.sendMessage("§2§lJob:");
                if(jtype == JobType.NONE) player.sendMessage("§7Current job: §cArbeitlos");
                else player.sendMessage("§7Current job: §e"+jtype.toString());
                player.sendMessage(" ");
                if(jtype != JobType.NONE) {
                    JobXpLevel jxpl = jtype.getJobXpLevel();
                    int level = pj.getLevel(jtype);
                    if(level < jxpl.getMaxLevel()) {
                        player.sendMessage("§7Level: §e"+level+"§7/"+jxpl.getMaxLevel());
                        player.sendMessage("§7XP: §e"+pj.getXP(jtype)+"§7/"+jxpl.xpNeededForLevelup(level));
                    }else {
                        player.sendMessage("§7Level: §c"+level+"§7/"+jxpl.getMaxLevel());
                        player.sendMessage("§7XP: §c-§7/§c-");
                    }
                    player.sendMessage(" ");
                    int i = 0;
                    for(Task task : jtype.getTasksFromJob()) {
                        String achieved = "§c0";
                        int amount = pj.getTaskAmount().getTaskAmount(i);
                        i++;
                        if(amount > 0) {
                            achieved = "§e"+amount;
                        }
                        player.sendMessage("§7"+task.getTitle()+"§7: " + achieved + "§7/" + task.getToachieve() + " §7§o("+new DecimalFormat("#.##").format(task.getPayoutamount(level))+" Coins)");
                    }
                    player.sendMessage(" ");
                }

            }else if(args[0].equalsIgnoreCase("leave")) {
                PlayerJob pj = new PlayerJob(player.getUniqueId());
                if(pj.getJob() != JobType.NONE) {
                    pj.setJob(JobType.NONE);
                    player.sendMessage("§7You §cleft §7your current job.");
                    updateScoreboardJob(player);
                }else {
                    player.sendMessage("§cYou currently dont have a job.");
                }
            }
        }else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("join")) {
                String jobname = args[1];
                if(JobType.fromString(jobname) != null) {
                    PlayerJob pj = new PlayerJob(player.getUniqueId());
                    if(pj.getJob() == JobType.NONE) {
                        JobType jobtype = JobType.fromString(jobname);
                        pj.setJob(jobtype);
                        player.sendMessage("§7Your new job now is §a" + jobtype.toString()+"§7.");
                        updateScoreboardJob(player);
                    }else {
                        player.sendMessage("§cYou need to leave your current job first.");
                    }
                }else {
                    player.sendMessage("§cThis job doesnt exist.");
                }
            }else if(args[0].equalsIgnoreCase("reset")) {
                if(player.hasPermission("server.admin.job")) {
                    String name = args[1];
                    if(Bukkit.getPlayer(name) != null) {
                        OfflinePlayer t = Bukkit.getOfflinePlayer(name);
                        PlayerJob pj = new PlayerJob(t.getUniqueId());
                        pj.setXPandLevel(pj.getJob(), 0, 0);
                        player.sendMessage("§7Hey there, reset!");
                    }
                }
            }
        }

        return false;
    }

    private void updateScoreboardJob(Player player) {
        PlayerProfile profile = PlayerProfile.getProfile(player.getUniqueId());
        profile.getScoreboard().updateJob();
    }

}