package de.koppy.economy.api;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import de.koppy.basics.api.PlayerProfile;
import de.koppy.economy.EconomySystem;
import de.koppy.server.Column;
import de.koppy.server.ColumnType;
import de.koppy.server.Table;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BankAccount {

    private static final Column accnamesc = new Column("accountname", ColumnType.VARCHAR, 200);
    private static final Column balancec = new Column("balance", ColumnType.DOUBLE, 200);
    private static final Column membersc = new Column("members", ColumnType.VARCHAR, 200);
    private static final Column membersinvitec = new Column("invitemembers", ColumnType.VARCHAR, 200);
    private static final Column logsc = new Column("logs", ColumnType.TEXT, 40000);
    private static Table table = EconomySystem.getBanktable();
    private String accountname;

    public BankAccount(String accountname) {
        this.accountname = accountname;
    }

    public boolean existName() {
        return table.existEntry(accnamesc, accountname);
    }

    public String getAccountname() {
        return accountname;
    }

    private String getPureLogs() {
        return (String) table.getValue(logsc, accnamesc, accountname);
    }

    @SuppressWarnings("deprecation")
    public void addLog(UUID who, String content) {
        Date date = new Date();
        List<String> logs = getLogsClean();
        if(logs.size() > 1187) {
            logs.remove(0);
        }
        logs.add(date.toLocaleString() + ": " + content + " #"+who.toString());
        saveLogs(logs);
    }

    @SuppressWarnings("deprecation")
    public void addLog(String who, String content) {
        Date date = new Date();
        List<String> logs = getLogs();
        if(logs.size() > 1187) {
            logs.remove(0);
        }
        logs.add(date.toLocaleString() + ": " + content + " #"+who.toString());
        saveLogs(logs);
    }

    public void saveLogs(String logs) {
        table.setValue(logsc, logs, accnamesc, accountname);
    }

    public void saveLogs(List<String> logs) {
        String out = "";
        if(logs.isEmpty() == false) {
            for(String log : logs) {
                out = out + ";" + log;
            }
            out = out.substring(1);
        }
        saveLogs(out);
    }

    public List<String> getLogsClean() {
        List<String> logs = new ArrayList<String>();
        String log = getPureLogs();
        if(log != null) {
            for(String s : log.split(";")) {
                if(s.equals("") == false) {
                    logs.add(s);
                }
            }
        }
        return logs;
    }

    public List<String> getLogs() {
        List<String> logs = new ArrayList<String>();
        String log = getPureLogs();
        if(log != null) {
            for(String s : log.split(";")) {
                if(s.equals("") == false) {
                    String[] outs = s.split("#");
                    String out = outs[0];
                    String uuid = "Server";
                    if(outs.length > 1) uuid = outs[1];
                    String name = "";
                    if(uuid.equals("Server") == false) {
                        name = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
                    }else {
                        name = "Server";
                    }
                    out = out + "(by "+ name + ")";
                    logs.add(out);
                }
            }
        }
        return logs;
    }

    public void sendMessageTranslated(String abbrevation) {
        List<String> muuids = getMember();
        for(String uuidS : muuids) {
            UUID uuid = UUID.fromString(uuidS);
            if(Bukkit.getPlayer(uuid) != null) {
                Player p = Bukkit.getPlayer(uuid);
                PlayerProfile profile = PlayerProfile.getProfile(p.getUniqueId());
                profile.sendMessage(abbrevation);
            }
        }
    }

    public void sendMessageTranslated(String prefix, String abbrevation) {
        List<String> muuids = getMember();
        for(String uuidS : muuids) {
            UUID uuid = UUID.fromString(uuidS);
            if(Bukkit.getPlayer(uuid) != null) {
                Player p = Bukkit.getPlayer(uuid);
                PlayerProfile profile = PlayerProfile.getProfile(p.getUniqueId());
                profile.sendMessage(prefix, abbrevation);
            }
        }
    }

    public void sendMessage(String message) {
        List<String> muuids = getMember();
        for(String uuidS : muuids) {
            UUID uuid = UUID.fromString(uuidS);
            if(Bukkit.getPlayer(uuid) != null) {
                Player p = Bukkit.getPlayer(uuid);
                p.sendMessage(message);
            }
        }
    }

    public void delete() {
        double bal = getBalance();
        List<String> members = getMember();
        double moneyperplayer = bal / ((double) members.size());
        table.delete(accnamesc, accountname);
        for(String uuid : members) {
            PlayerAccount pa = new PlayerAccount(UUID.fromString(uuid));
            List<String> bankacc = pa.getBankaccounts();
            bankacc.remove(accountname);
            pa.setBankaccounts(bankacc);
            pa.addMoney(moneyperplayer, "from Bankaccount: "+accountname, "money from deleting bankaccount");
            if(Bukkit.getPlayer(uuid) != null) {
                Bukkit.getPlayer(uuid).sendMessage("§7The Bankaccount "+ accountname +" was dissolved and you received " + new DecimalFormat("#").format(moneyperplayer) + EconomySystem.getEcosymbol() + "§7.");
            }
        }
    }

    public void create(UUID uuid) {
        List<String> out = new ArrayList<String>();
        out.add(uuid.toString());
        if(!existName()) {
            table.setValue(balancec, 0d, accnamesc, accountname);
            table.setValue(membersc, ListToString(out), accnamesc, accountname);
        }
        PlayerAccount pa = new PlayerAccount(uuid);
        List<String> bankaccounts = pa.getBankaccounts();
        bankaccounts.add(accountname);
        pa.setBankaccounts(bankaccounts);
    }

    public void setMember(List<String> members, UUID who, String reason) {
        table.setValue(membersc, ListToString(members), accnamesc, accountname);
        addLog(who, reason);
    }

    public List<String> getMember() {
        List<String> memberlist = new ArrayList<String>();
        String out = (String) table.getValue(membersc, accnamesc, accountname);
        if(out != null) {
            memberlist = StringToList(out);
        }
        return memberlist;
    }

    public void addMember(UUID uuid, UUID who, String reason) {
        List<String> memberinvites = getMemberinvite();
        memberinvites.remove(uuid.toString());
        setMemberinvite(memberinvites);

        List<String> members = getMember();
        members.add(uuid.toString());
        setMember(members, who, reason);

        PlayerAccount pa = new PlayerAccount(uuid);
        List<String> bankaccounts = pa.getBankaccounts();
        bankaccounts.add(accountname);
        pa.setBankaccounts(bankaccounts);

    }

    /*
     * WARNING: This doesn't remove a player from the Invites!
     */
    public void removeMember(UUID uuid, UUID who, String reason) {
        List<String> members = getMember();
        members.remove(uuid.toString());
        setMember(members, who, reason);

        PlayerAccount pa = new PlayerAccount(uuid);
        List<String> bankaccounts = pa.getBankaccounts();
        bankaccounts.remove(accountname);
        pa.setBankaccounts(bankaccounts);
    }

    public void setMemberinvite(List<String> members) {
        table.setValue(membersinvitec, ListToString(members), accnamesc, accountname);
    }

    public List<String> getMemberinvite() {
        List<String> memberlist = new ArrayList<String>();
        String out = (String) table.getValue(membersinvitec, accnamesc, accountname);
        if(out != null) {
            memberlist = StringToList(out);
        }
        return memberlist;
    }

    public void setBalance(double balance, UUID who, String reason) {
        table.setValue(balancec, balance, accnamesc, accountname);
        addLog(who, reason);
    }

    public void addBalance(double amount, UUID who, String reason) {
        double bal = getBalance();
        bal = bal + amount;
        setBalance(bal, who, reason);
    }

    public void removeBalance(double amount, UUID who, String reason) {
        double bal = getBalance();
        bal = bal - amount;
        setBalance(bal, who, reason);
    }

    public double getBalance() {
        return (Double) table.getValue(balancec, accnamesc, accountname);
    }

    public static List<String> StringToList(String memberlist) {
        List<String> out = new ArrayList<String>();
        if(memberlist.contains(":")) {
            for(String memberuuid : memberlist.split(":")) {
                out.add(memberuuid);
            }
        }else {
            out.add(memberlist);
        }
        return out;
    }

    public static String ListToString(List<String> memberlist) {
        String out = "";
        if(memberlist.isEmpty() == false) {
            for(String memberuuid : memberlist) {
                out = out + ":" + memberuuid;
            }
            out = out.substring(1);
        }
        return out;
    }

    public void resetLogs() {
        saveLogs(new ArrayList<String>());
    }

}