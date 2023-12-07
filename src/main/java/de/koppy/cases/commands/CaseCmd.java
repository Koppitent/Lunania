package de.koppy.cases.commands;

import de.koppy.basics.api.SavedItems;
import de.koppy.cases.CaseSystem;
import de.koppy.cases.api.Case;
import de.koppy.cases.api.PlayerCase;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CaseCmd implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player) {
            Player p = (Player) sender;

            if(args.length == 0) {
                p.sendMessage(CaseSystem.getPrefix() + "§7Your keys: ");
                PlayerCase pc = new PlayerCase(p.getUniqueId());
                for(Case c : Case.cases) {
                    if(c.needsKey()) {
                        p.sendMessage("§7> "+c.getName()+"§7: §e" + pc.getAmount(c.getName()));
                    }
                }
            }

            if(p.hasPermission("server.case")) {
                if(args.length == 2) {
                    String name = args[1];
                    if(Case.existCasebyName(name)) {
                        if(args[0].equalsIgnoreCase("getcase")) {
                            p.getInventory().addItem(Case.getCasebyName(name).getCase());
                            p.sendMessage(CaseSystem.getPrefix() + "§7Youve got the Case " + name + "§7.");
                        }else if(args[0].equalsIgnoreCase("getkey")) {
                            PlayerCase pc = new PlayerCase(p.getUniqueId());
                            pc.addAmount(name, 1);
                            p.sendMessage(CaseSystem.getPrefix() + "§7Youve got the CaseKey of the case " + name + "§7.");
                        }
                    }else {
                        p.sendMessage(CaseSystem.getPrefix() + "§cThis Case doesnt exist.");
                    }
                }else if(args.length == 3) {
                    String name = args[1];
                    if(args[2].matches("[0-9]+")) {
                        Integer number = Integer.valueOf(args[2]);
                        if(Case.existCasebyName(name)) {
                            if(args[0].equalsIgnoreCase("getcase")) {
                                for(int i=0; i<number; i++) {
                                    p.getInventory().addItem(Case.getCasebyName(name).getCase());
                                }
                                p.sendMessage(CaseSystem.getPrefix() + "§7Youve got the case " + name + "§7. ("+number+"x)");
                            }else if(args[0].equalsIgnoreCase("getkey")) {
                                PlayerCase pc = new PlayerCase(p.getUniqueId());
                                pc.addAmount(name, number);
                                p.sendMessage(CaseSystem.getPrefix() + "§7Youve got the CaseKey of the case " + name + "§7. ("+number+"x)");
                            }
                        }else {
                            p.sendMessage(CaseSystem.getPrefix() + "§cThis Case doesnt exist.");
                        }
                    }else {
                        p.sendMessage(CaseSystem.getPrefix() + "§cargs2 needs to be a number");
                    }

                }
            }
        }else {
            if(args.length == 4) {
                String playername = args[3];
                @SuppressWarnings("deprecation")
                OfflinePlayer op = Bukkit.getOfflinePlayer(playername);
                if(op.hasPlayedBefore()) {
                    String name = args[1];
                    if(args[2].matches("[0-9]+")) {
                        Integer number = Integer.valueOf(args[2]);
                        if(Case.existCasebyName(name)) {
                            if(args[0].equalsIgnoreCase("getcase")) {
                                for(int i=0; i<number; i++) {
                                    new SavedItems(op.getUniqueId()).addItem(Case.getCasebyName(name).getCase());
                                }
                                sender.sendMessage(CaseSystem.getPrefix() + "§7Youve send the case " + name + "§7. ("+number+"x)");
                            }else if(args[0].equalsIgnoreCase("getkey")) {
                                PlayerCase pc = new PlayerCase(op.getUniqueId());
                                pc.addAmount(name, number);
                                sender.sendMessage(CaseSystem.getPrefix() + "§7Youve sent the CaseKey of the case " + name + "§7. ("+number+"x)");
                            }
                        }else {
                            sender.sendMessage(CaseSystem.getPrefix() + "§cThis Case doesnt exist.");
                        }
                    }else {
                        sender.sendMessage(CaseSystem.getPrefix() + "§cargs2 needs to be a number");
                    }
                }
            }
        }
        return false;
    }

}