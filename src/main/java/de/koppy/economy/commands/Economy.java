package de.koppy.economy.commands;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.UUID;

import de.koppy.basics.api.PlayerProfile;
import de.koppy.economy.EconomySystem;
import de.koppy.economy.api.PlayerAccount;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Economy implements CommandExecutor {

    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player))
            return false;
        Player player = (Player) sender;
        PlayerProfile profile = PlayerProfile.getProfile(player.getUniqueId());

        if(args.length == 0) {
            NumberFormat f = NumberFormat.getInstance();
            player.sendMessage(EconomySystem.getPrefix() + profile.getMessage("showbalance").replace("%balance%", ""+f.format(profile.getMoney())+EconomySystem.getEcosymbol()));
        }else if(args.length == 1) {
            if(args[0].equalsIgnoreCase("toggle")) {
                PlayerAccount account = new PlayerAccount(player.getUniqueId());
                if(account.isVisible()) {
                    account.setVisible(false);
                    player.sendMessage(EconomySystem.getPrefix() + profile.getMessage("visibilitysetfalse"));
                }else {
                    account.setVisible(true);
                    player.sendMessage(EconomySystem.getPrefix() + profile.getMessage("visibilitysettrue"));
                }
            }else if(args[0].equalsIgnoreCase("top")) {
                player.sendMessage(new PlayerAccount(player.getUniqueId()).getBaltopforyourself());
            }else {
                String name = args[0];
                if(Bukkit.getOfflinePlayer(name).getFirstPlayed() != 0) {
                    PlayerAccount account = new PlayerAccount(Bukkit.getOfflinePlayer(name).getUniqueId());
                    if(account.isVisible() || player.hasPermission("server.admin.money")) {
                        player.sendMessage("§7"+Bukkit.getOfflinePlayer(name).getName() + "'s money: §e" + account.getMoney() + EconomySystem.getEcosymbol());
                    }else {
                        player.sendMessage(profile.getMessage("cantseemoneyfrom").replace("%player%", name));
                    }
                }else {
                    player.sendMessage(profile.getMessage("playerneveronserver").replace("%player%", name));
                }
            }
        }else if(args.length == 3) {
            if(args[0].equalsIgnoreCase("pay")) {
                String name = args[1];
                String amountS = args[2];
                if(amountS.matches("[0.0-9.9]+")) {
                    double amount = Double.valueOf(amountS);
                    if(amount >= 1000) {
                        OfflinePlayer op = Bukkit.getOfflinePlayer(name);
                        if(op.hasPlayedBefore()) {
                            UUID tuuid = op.getUniqueId();
                            PlayerAccount ta = new PlayerAccount(tuuid);
                            PlayerAccount pa = new PlayerAccount(player.getUniqueId());
                            double bal = pa.getMoney();
                            if(bal >= amount) {
                                pa.removeMoney(amount, "sendto "+name, "paid with command");
                                ta.addMoney(amount, "from "+player.getName(), "got paid from command");
                                player.sendMessage(EconomySystem.getPrefix() + profile.getMessage("transactionsucessfull").replace("%amount%", ""+amount).replace("%name%", name));
                                if(Bukkit.getPlayer(name) != null) {
                                    Bukkit.getPlayer(name).sendMessage(EconomySystem.getPrefix() + PlayerProfile.getProfile(Bukkit.getPlayer(name).getUniqueId()).getMessage("youreceivedmoneyfrom").replace("%amount%", ""+amount).replace("%name%", player.getName()));
                                }
                                return false;
                            }else {
                                player.sendMessage(EconomySystem.getPrefix() + profile.getMessage("notenoughmoney"));
                                return false;
                            }
                        }else {
                            player.sendMessage(EconomySystem.getPrefix() + profile.getMessage("playerwasneveronline"));
                            return false;
                        }
                    }else {
                        player.sendMessage(EconomySystem.getPrefix() + profile.getMessage("numbertoosmall"));
                        return false;
                    }
                }else {
                    player.sendMessage(EconomySystem.getPrefix() + profile.getMessage("wrongamountformat"));
                    return false;
                }
            }

            if(player.hasPermission("server.money")) {
                if(args[0].equalsIgnoreCase("getlog")) {
                    String playername = args[1];

                    if(args[2].matches("[0-9]+")) {
                        String[] out = {""}; // get log of number args[2]
                        if(out != null) {
                            long datetime = Long.valueOf(out[0]);
                            double amount = Double.valueOf(out[1]);
                            double moneybef = Double.valueOf(out[2]);
                            double moneyafter = Double.valueOf(out[3]);
                            String sentto = out[4];
                            String reason = out[5];

                            DecimalFormat f = new DecimalFormat("#.##");
                            player.sendMessage("§5§lEcolog: §r§7"+new Date(datetime).toLocaleString());
                            player.sendMessage("§7Amount: §3"+f.format(amount) + EconomySystem.getEcosymbol());
                            player.sendMessage("§7Money before: §3"+f.format(moneybef) + EconomySystem.getEcosymbol());
                            player.sendMessage("§7Money after: §3"+f.format(moneyafter) + EconomySystem.getEcosymbol());
                            player.sendMessage("");
                            player.sendMessage("§7Sent to/Received from: " + sentto);
                            player.sendMessage("§7Reason: " + reason);

                        }else {
                            player.sendMessage(EconomySystem.getPrefix() + "§cThis player does not exist or theres no ecolog for that player.");
                        }
                    }else {
                        player.sendMessage(EconomySystem.getPrefix() + "§cYou need to give a number!");
                    }
                }else if(args[0].equalsIgnoreCase("add")) {
                    String name = args[1];
                    OfflinePlayer op = Bukkit.getOfflinePlayer(name);
                    UUID tuuid = op.getUniqueId();
                    PlayerAccount ta = new PlayerAccount(tuuid);
                    double bal = ta.getMoney();
                    String amountS = args[2];
                    if(amountS.matches("[0.0-9.9]+")) {
                        double amount = Double.valueOf(amountS);
                        double moneybefore = bal;
                        bal = bal+amount;
                        ta.setMoney(bal, "from admin", "added money with command", moneybefore, amount);
                        player.sendMessage(EconomySystem.getPrefix() + profile.getMessage("moneyaddedfrom").replace("%amount%", ""+amount).replace("%name%", name));
                    }else {
                        player.sendMessage(EconomySystem.getPrefix() + profile.getMessage("wrongamountformat"));
                        return false;
                    }
                }else if(args[0].equalsIgnoreCase("remove")) {
                    String name = args[1];
                    OfflinePlayer op = Bukkit.getOfflinePlayer(name);
                    UUID tuuid = op.getUniqueId();
                    PlayerAccount ta = new PlayerAccount(tuuid);
                    String amountS = args[2];
                    if(amountS.matches("[0.0-9.9]+")) {
                        double amount = Double.valueOf(amountS);
                        ta.removeMoney(amount, "from admin", "removed money with command");
                        player.sendMessage(EconomySystem.getPrefix() + profile.getMessage("moneyremovedfrom").replace("%amount%", ""+amount).replace("%name%", name));
                    }else {
                        player.sendMessage(EconomySystem.getPrefix() + profile.getMessage("wrongamountformat"));
                        return false;
                    }
                }else if(args[0].equalsIgnoreCase("set")) {
                    String name = args[1];
                    OfflinePlayer op = Bukkit.getOfflinePlayer(name);
                    UUID tuuid = op.getUniqueId();
                    PlayerAccount ta = new PlayerAccount(tuuid);
                    String amountS = args[2];
                    double bal = ta.getMoney();
                    if(amountS.matches("[0.0-9.9]+")) {
                        double amount = Double.valueOf(amountS);
                        ta.setMoney(amount, "from admin", "set money with command (amount maybe fcked up)", bal, -1);
                        player.sendMessage(EconomySystem.getPrefix() + profile.getMessage("moneysetfrom").replace("%amount%", ""+amount).replace("%name%", name));
                    }else {
                        player.sendMessage(EconomySystem.getPrefix() + profile.getMessage("wrongamountformat"));
                        return false;
                    }
                }
            }

        }

        return false;
    }

}