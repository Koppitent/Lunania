package de.koppy.economy.commands;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.koppy.basics.api.InventoryHelper;
import de.koppy.basics.api.PlayerProfile;
import de.koppy.economy.EconomySystem;
import de.koppy.economy.api.BankAccount;
import de.koppy.economy.api.BankLog;
import de.koppy.economy.api.BankMenu;
import de.koppy.economy.api.PlayerAccount;
import de.koppy.economy.listener.MenuEvents;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Banks implements CommandExecutor {

    public ArrayList<String> bankdelconfirm = new ArrayList<String>();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player))
            return false;
        Player player = (Player) sender;
        PlayerProfile profile = PlayerProfile.getProfile(player.getUniqueId());

        if(args.length == 0) {

            BankMenu menu = new BankMenu(player.getUniqueId());
            Inventory inventory = menu.getStartMenu();
            MenuEvents.ininv.add(player);
            player.openInventory(inventory);

        }else if(args.length == 1) {
            if(args[0].equalsIgnoreCase("test")) {

                if(player.hasPermission("server.admin.test")) {

                    int i = 0;
                    List<BankLog> logs = new BankAccount("koppyag").getLogs();
                    for(BankLog s : logs) {
                        player.sendMessage(i+"  :  "+s.toString());
                        i++;
                    }
                    new BankAccount("koppyag").addLog(player.getUniqueId(), "tested logs");

                }

            }else if(args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("info")) {
                PlayerAccount pa = new PlayerAccount(player.getUniqueId());
                List<String> bankaccounts = pa.getBankaccounts();
                if(bankaccounts.isEmpty()) {
                    player.sendMessage("§cYou have no bankaccounts yet");
                }else {
                    player.sendMessage("§7§m----------§r§7[ §3B§bank §7]§m----------");
                    double total = 0;
                    for(String acc : bankaccounts) {
                        BankAccount bank = new BankAccount(acc);
                        if(bank.existName()) {
                            total = total + bank.getBalance();
                            player.sendMessage("§7"+bank.getAccountname() + ": §e"+ new DecimalFormat("#").format(bank.getBalance()) + EconomySystem.getEcosymbol());
                        }
                    }
                    player.sendMessage("");
                    player.sendMessage("§8§ §7Total: §e"+new DecimalFormat("#").format(total) + EconomySystem.getEcosymbol());
                }
            }
        }else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("create")) {
                PlayerAccount pa = new PlayerAccount(player.getUniqueId());
                if(pa.getBankaccounts().size() < 3) {
                    String bankname = args[1];
                    if(bankname.length() <= 8) {
                        if(bankname.matches("[a-zA-Z0-9]+")) {
                            BankAccount account = new BankAccount(bankname);
                            if(!account.existName()) {
                                account.create(player.getUniqueId());
                                player.sendMessage("§7You have §acreated §7a new bankaccount with the name "+bankname+".");
                            }else {
                                player.sendMessage("§cThis name does already exist.");
                            }
                        }else {
                            player.sendMessage("§cOnly alphabetic characters are allowed.");
                        }
                    }else {
                        player.sendMessage("§cMaximum of 8 characters.");
                    }
                }else {
                    player.sendMessage("§cYou alredy have the maximum of 3 accounts.");
                }
            }else if(args[0].equalsIgnoreCase("delete")) {
                String bankname = args[1];
                BankAccount account = new BankAccount(bankname);
                if(account.existName()) {
                    if(account.getMember().contains(player.getUniqueId().toString())) {
                        if(bankdelconfirm.contains(bankname)) {
                            bankdelconfirm.remove(bankname);
                            account.delete();
                            player.sendMessage("§7The account has been deleted!");
                        }else {
                            bankdelconfirm.add(bankname);
                            player.sendMessage("§c! §7If you really wish to delete this bankaccount, type the command again. §c!");
                        }
                    }else {
                        player.sendMessage("§cYou need to be a member to do that.");
                    }
                }else {
                    player.sendMessage("§cThis account doesnt exist.");
                }
            }else if(args[0].equalsIgnoreCase("accept")) {
                String bankname = args[1];
                BankAccount account = new BankAccount(bankname);
                if(account.existName()) {
                    if(account.getMemberinvite().contains(player.getUniqueId().toString())) {

                        List<String> memberinvites = account.getMemberinvite();
                        memberinvites.remove(player.getUniqueId().toString());
                        account.setMemberinvite(memberinvites);

                        List<String> members = account.getMember();
                        members.add(player.getUniqueId().toString());
                        account.setMember(members, player.getUniqueId(), "joined to account");
                        account.sendMessage("§7The player "+player.getName()+" §ajoined §7the bankaccount "+bankname+"§7.");
                        player.sendMessage("§7You joined the Bankaccount §3"+account.getAccountname()+"§7.");

                        PlayerAccount pa = new PlayerAccount(player.getUniqueId());
                        List<String> bankaccounts = pa.getBankaccounts();
                        bankaccounts.add(bankname.toLowerCase());
                        pa.setBankaccounts(bankaccounts);

                    }else {
                        player.sendMessage("§cYou have not been invited to this account.");
                    }
                }else {
                    player.sendMessage("§cThis bankaccount does not exist.");
                }
            }else if(args[0].equalsIgnoreCase("deny")) {
                String bankname = args[1];
                BankAccount account = new BankAccount(bankname);
                if(account.existName()) {
                    if(account.getMemberinvite().contains(player.getUniqueId().toString())) {

                        List<String> memberinvites = account.getMemberinvite();
                        memberinvites.remove(player.getUniqueId().toString());
                        account.setMemberinvite(memberinvites);
                        account.sendMessage("§7The player "+player.getName()+" §cdenied §7to join §7the bankaccount "+bankname+"§7.");
                        player.sendMessage("§7You denied the request to join the Bankaccount §3"+account.getAccountname()+"§7.");

                    }else {
                        player.sendMessage("§cYou have not been invited to this account.");
                    }
                }else {
                    player.sendMessage("§cThis bankaccount does not exist.");
                }
            }else if(args[0].equalsIgnoreCase("leave")) {
                String bankname = args[1];
                BankAccount account = new BankAccount(bankname);
                if(account.existName()) {
                    if(account.getMember().contains(player.getUniqueId().toString())) {
                        if(account.getMember().size() > 1) {

                            List<String> members = account.getMember();
                            members.remove(player.getUniqueId().toString());
                            account.setMember(members, player.getUniqueId(), "left the account");
                            account.sendMessage("§7The player "+player.getName()+" §cleft §7the bankaccount "+bankname+"§7.");
                            player.sendMessage("§7You §cleft §7the Bankaccount §3"+account.getAccountname()+"§7.");

                            PlayerAccount pa = new PlayerAccount(player.getUniqueId());
                            List<String> bankaccounts = pa.getBankaccounts();
                            bankaccounts.remove(bankname.toLowerCase());
                            pa.setBankaccounts(bankaccounts);

                        }else {
                            player.sendMessage("§cYou are the only person on that bankaccount. If you want to leave it, you have to delete it.");
                        }
                    }else {
                        player.sendMessage("§cYou are not a member of this account.");
                    }
                }else {
                    player.sendMessage("§cThis bankaccount does not exist.");
                }
            }else if(args[0].equalsIgnoreCase("resetlogs")) {
                String bankname = args[1];
                BankAccount account = new BankAccount(bankname);
                if(!account.existName()) { profile.sendMessage("bankdoesntexist"); return false; }
                if(!player.hasPermission("server.admin.test")) { profile.sendMessage("noperms"); return false; }

                account.resetLogs();
                profile.sendMessage("logsresettet");

            }
        }else if(args.length == 3) {
            if(args[0].equalsIgnoreCase("invite")) {
                String bankname = args[1];
                String playername = args[2];
                BankAccount account = new BankAccount(bankname);
                if(account.existName()) {
                    if(account.getMember().contains(player.getUniqueId().toString())) {
                        @SuppressWarnings("deprecation")
                        OfflinePlayer op = Bukkit.getOfflinePlayer(playername);
                        if(op.hasPlayedBefore()) {
                            if(account.getMember().size() > 5) { profile.sendMessage("toomanymember"); return false; }
                            if(!account.getMember().contains(op.getUniqueId().toString())) {
                                if(!account.getMemberinvite().contains(op.getUniqueId().toString())) {

                                    List<String> memberinvites = account.getMemberinvite();
                                    memberinvites.add(op.getUniqueId().toString());
                                    account.setMemberinvite(memberinvites);
                                    if(Bukkit.getPlayer(op.getUniqueId()) != null) {
                                        Bukkit.getPlayer(op.getUniqueId()).sendMessage("§7You have been invited to join the bankaccount §3"+bankname+"§7.");
                                    }
                                    player.sendMessage("§7You have invited the player §e" + op.getName() + "§7 to the bankaccount §3"+bankname+"§7.");

                                }else {
                                    player.sendMessage("§cThis player is already invited to this account.");
                                }
                            }else {
                                player.sendMessage("§cThis player is already a member of the bankaccount.");
                            }
                        }else {
                            player.sendMessage("§cThe player was never online on that server.");
                        }
                    }else {
                        player.sendMessage("§cYou need to be a member to do that.");
                    }
                }else {
                    player.sendMessage("§cThis bankaccount does not exist.");
                }
            }else if(args[0].equalsIgnoreCase("withdraw")) {
                String bankname = args[1];
                BankAccount account = new BankAccount(bankname);
                if(args[2].matches("[0.0-9.9]+")) {
                    double amount = Double.parseDouble(args[2]);
                    if(amount >= 1000) {
                        if(account.getMember().contains(player.getUniqueId().toString())) {
                            if(account.getBalance() >= amount) {

                                double bal = account.getBalance();
                                bal = bal - amount;
                                account.setBalance(bal, player.getUniqueId(), "removed " + amount + " Coins");

                                PlayerAccount pa = new PlayerAccount(player.getUniqueId());
                                pa.addMoney(amount, "from Bankaccount: "+bankname.toLowerCase(), "withdrawn money from bankaccount");

                                player.sendMessage("§7You have withdrawn "+amount+" Coins.");

                            }else {
                                player.sendMessage("§cTheres not enough money in the bank.");
                            }
                        }else {
                            player.sendMessage("§cYou need to be a player of that bankaccount.");
                        }
                    }else {
                        player.sendMessage("§cYou need to withdraw at least 1000 Coins.");
                    }
                }else {
                    player.sendMessage("§cWrong money format.");
                }
            }else if(args[0].equalsIgnoreCase("deposit")) {
                String bankname = args[1];
                BankAccount account = new BankAccount(bankname);
                if(args[2].matches("[0.0-9.9]+")) {
                    double amount = Double.parseDouble(args[2]);
                    if(account.getMember().contains(player.getUniqueId().toString())) {
                        PlayerAccount pa = new PlayerAccount(player.getUniqueId());
                        if(pa.getMoney() >= amount) {

                            pa.removeMoney(amount, "sendto Bankaccount: "+bankname.toLowerCase(), "deposited money on bankaccount");
                            account.addBalance(amount, player.getUniqueId(), "added " + amount + " Coins");

                            player.sendMessage("§7You have deposited "+amount+" Coins.");

                        }else {
                            player.sendMessage("§cYou don't own enough money.");
                        }
                    }else {
                        player.sendMessage("§cYou need to be a player of that bankaccount.");
                    }
                }else {
                    player.sendMessage("§cWrong money format.");
                }
            }
        }

        return false;
    }
}