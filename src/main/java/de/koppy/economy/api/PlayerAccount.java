package de.koppy.economy.api;

import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import de.koppy.bansystem.commands.Ban;
import de.koppy.basics.api.PlayerProfile;
import de.koppy.economy.EconomySystem;
import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.mysql.api.Column;
import de.koppy.mysql.api.ColumnType;
import de.koppy.mysql.api.Table;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class PlayerAccount {

    private UUID uuid;
    private static final Column uuidc = new Column("uuid", ColumnType.VARCHAR, 200);
    private static final Column balancec = new Column("balance", ColumnType.DOUBLE, 200);
    private static final Column bankaccountsc = new Column("bankaccounts", ColumnType.VARCHAR, 200);
    private static final Column visiblec = new Column("visible", ColumnType.BOOL, 200);
    private static final Column logsc = new Column("logs", ColumnType.TEXT, 40000);
    private static final Table table = EconomySystem.getEcotable();
    public PlayerAccount(UUID uuid) {
        this.uuid = uuid;
    }

    public List<String> getBankaccounts() {
        String out = (String) table.getValue(bankaccountsc, uuidc, uuid.toString());
        List<String> outlist = new ArrayList<String>();
        if(out != null) {
            outlist = StringToList(out);
        }
        return outlist;
    }

    public void checkIfMember() {
        List<String> newbanklist = new ArrayList<>();
        for(String s : getBankaccounts()) {
            BankAccount ba = new BankAccount(s);
            if(ba.existName()) {
                if(ba.getMember().contains(uuid.toString())) {
                    newbanklist.add(s);
                }
            }
        }
        setBankaccounts(newbanklist);
    }

    public String getBaltopforyourself() {
        String finalstring="§3Richest Player: \n";
        try {
            PreparedStatement ps = LunaniaSystem.getMySQLInstance().getConnection().prepareStatement("SELECT DISTINCT "+balancec.getName()+" FROM "+"economy"+" ORDER BY "+balancec.getName()+ " DESC LIMIT 10");
            ResultSet rs = ps.executeQuery();
            int i = 0;
            while(rs.next()) {
                i++;
                String amounts = rs.getString(balancec.getName());
                try {
                    PreparedStatement ps2 = LunaniaSystem.getMySQLInstance().getConnection().prepareStatement("SELECT "+uuidc.getName()+" FROM "+"economy"+" WHERE "+balancec.getName()+" = ?");
                    ps2.setString(1, amounts);
                    ResultSet rs2 = ps2.executeQuery();
                    finalstring = finalstring + "§7#"+i+". " + new DecimalFormat("#,###.##").format(Double.parseDouble(amounts)) + EconomySystem.getEcosymbol() + " §8- ";
                    while(rs2.next()) {
                        String uuids = rs2.getString(uuidc.getName());
                        if(uuids.equals(uuid.toString()) == false) {
                            if(new PlayerAccount(UUID.fromString(uuids)).isVisible()) {
                                OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(uuids));
                                finalstring = finalstring + "§e" + op.getName()+", ";
                            }else {
                                finalstring = finalstring + "§e" + "anonym"+", ";
                            }
                        }else {
                            OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(uuids));
                            finalstring = finalstring + "§3§l" + op.getName()+"§e, ";
                        }
                    }
                    finalstring = finalstring.substring(0, finalstring.length()-2);
                    finalstring = finalstring + "\n";
                } catch (SQLException e) {
                    e.printStackTrace();
                    Bukkit.getServer().getConsoleSender().sendMessage("§4Maybe no entry for user in database. (SQLException)");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getServer().getConsoleSender().sendMessage("§4Maybe no entry for user in database. (SQLException)");
        }

        return finalstring;
    }

    public boolean isVisible() {
        return (!(Boolean) table.getValue(visiblec, uuidc, uuid.toString()));
    }

    public void setVisible(boolean bool) {
        table.setValue(visiblec, bool, uuidc, uuid.toString());
    }

    public void setBankaccounts(List<String> accounts) {
        table.setValue(bankaccountsc, ListToString(accounts), uuidc, uuid.toString());

    }

    public void addMoney(double amount, String sendto, String reason) {
        double bal = getMoney();
        double newbal = bal + amount;
        setMoney(newbal, sendto, reason, bal, amount);
    }

    public void removeMoney(double amount, String sendto, String reason) {
        double bal = getMoney();
        double newbal = bal - amount;
        setMoney(newbal, sendto, reason, bal, -amount);
    }

    public double getMoney() {
        if(table.existEntry(uuidc, uuid.toString())) {
            return (Double) table.getValue(balancec, uuidc, uuid.toString());
        }else {
            setMoneyDatabaseFirstTime(0d);
            return 0.d;
        }
    }

    public void setMoneyDatabase(double money) {
        table.setValue(balancec, money, uuidc, uuid.toString());
        if(Bukkit.getPlayer(uuid) != null) {
            PlayerProfile.getProfile(uuid).getScoreboard().updateEco();
        }
    }

    public void setMoneyDatabaseFirstTime(double money) {
        table.setValue(balancec, money, uuidc, uuid.toString());
    }

    public void setMoney(double money, String sendto, String reason, double moneybefore, double amount) {
        setMoneyDatabase(money);
        if(money > moneybefore) {
            addLog(new Log(sendto, uuid.toString(), amount, reason, moneybefore, new Date()));
        }else {
            addLog(new Log(uuid.toString(), sendto, amount, reason, moneybefore, new Date()));
        }
    }

    //* Logs
    public List<Log> getLogs() {
        String data = "";
        if(table.existEntry(logsc, uuidc, uuid.toString())) {
            data = (String) table.getValue(logsc, uuidc, uuid.toString());
        }
        return StringToListLog(data);
    }

    public void addLog(Log log) {
        List<Log> logs = getLogs();
        logs.add(log);
        Date expiredate = new Date();
        long time = expiredate.getTime();
        expiredate.setTime(time-(1000l*60l*60l*24l*30l*6l));
        List<Log> logsremove = new ArrayList<>();
        for(int i=logs.size()-1; i>0; i--) {
            Log checklog = logs.get(i);
            if(checklog.getDate().before(expiredate)) {
                logsremove.add(checklog);
            }else {
                break;
            }
        }
        logs.removeAll(logsremove);
        saveLogs(logs);
    }

    public void removeLog(int index) {
        List<Log> logs = getLogs();
        logs.remove(index);
        saveLogs(logs);
    }

    private void saveLogs(List<Log> logs) {
        table.setValue(logsc, ListToStringLog(logs), uuidc, uuid.toString());
    }

    private String ListToStringLog(List<Log> list) {
        String out = "";
        if(list.isEmpty()) return out;
        for(Log o : list) {
            out = out + o.toString() + ":";
        }
        out = out.substring(0, out.length()-1);
        return out;
    }

    private List<Log> StringToListLog(String rawstring) {
        List<Log> out = new ArrayList<>();
        if(rawstring == null) return out;
        if(rawstring.isEmpty()) return out;
        if(!rawstring.contains(":")) {
            out.add(Log.fromString(rawstring));
            return out;
        }
        for(String o : rawstring.split(":")) {
            out.add(Log.fromString(o));
        }
        return out;
    }

    public String getFormatMoney() {
        double money = getMoney();
        NumberFormat f = NumberFormat.getInstance();
        DecimalFormat ff = new DecimalFormat("#");
        ff.setRoundingMode(RoundingMode.DOWN);
        if(money < 1000d) {
            return f.format(money);
        }else if(money < 1000000d) {
            money = money / 1000d;
            return ff.format(money) + "K";
        }else if(money < 1000000000d) {
            money = money / 1000000d;
            return ff.format(money) + "M";
        }else {
            money = money / 1000000000d;
            return ff.format(money) + "B";
        }
    }

    public static List<String> StringToList(String string) {
        List<String> out = new ArrayList<String>();
        if(string.isEmpty()) return out;
        if(string.contains(":")) {
            for(String memberuuid : string.split(":")) {
                out.add(memberuuid);
            }
        }else {
            out.add(string);
        }
        return out;
    }

    public static String ListToString(List<String> list) {
        String out = "";
        if(list.isEmpty()) return out;
        for(String memberuuid : list) {
            out = out + ":" + memberuuid;
        }
        out = out.substring(1);
        return out;
    }

    public static String getBaltop() {
        Column uuidc = new Column("uuid", ColumnType.VARCHAR, 200);
        Column balancec = new Column("balance", ColumnType.DOUBLE, 200);
        String finalstring="§3Richest Player: \n";
        try {
            PreparedStatement ps = LunaniaSystem.getMySQLInstance().getConnection().prepareStatement("SELECT DISTINCT "+balancec.getName()+" FROM "+"economy"+" ORDER BY "+balancec.getName()+ " DESC LIMIT 10");
            ResultSet rs = ps.executeQuery();
            int i = 0;
            while(rs.next()) {
                i++;
                String amounts = rs.getString(balancec.getName());
                try {
                    PreparedStatement ps2 = LunaniaSystem.getMySQLInstance().getConnection().prepareStatement("SELECT "+uuidc.getName()+" FROM "+"economy"+" WHERE "+balancec.getName()+" = ?");
                    ps2.setString(1, amounts);
                    ResultSet rs2 = ps2.executeQuery();
                    finalstring = finalstring + "§7#"+i+". " + new DecimalFormat("#,###.##").format(Double.parseDouble(amounts)) + EconomySystem.getEcosymbol() + " §8- ";
                    while(rs2.next()) { }
                    finalstring = finalstring.substring(0, finalstring.length()-2);
                    finalstring = finalstring + "\n";
                } catch (SQLException e) {
                    e.printStackTrace();
                    Bukkit.getServer().getConsoleSender().sendMessage("§4Maybe no entry for user in database. (SQLException)");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getServer().getConsoleSender().sendMessage("§4Maybe no entry for user in database. (SQLException)");
        }

        return finalstring;
    }

}