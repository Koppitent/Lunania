package de.koppy.bansystem.api;

import de.koppy.bansystem.BanSystem;
import de.koppy.mysql.api.Column;
import de.koppy.mysql.api.ColumnType;
import de.koppy.mysql.api.Table;
import org.bukkit.Bukkit;

import java.util.Date;
import java.util.UUID;

public class BanManager {

    private final static Column uuidc = new Column("uuid", ColumnType.VARCHAR, 200);
    private final static Column reasonc = new Column("reason", ColumnType.VARCHAR, 200);
    private final static Column expiredatec = new Column("expiredate", ColumnType.VARCHAR, 200);
    private final static Column bannedbyuuidc = new Column("bannedbyuuid", ColumnType.VARCHAR, 200);
    private final static Column historyc = new Column("history", ColumnType.TEXT, 40000);

    private final UUID uuid;
    private final Table table = BanSystem.getTable();
    public BanManager(UUID uuid) {
        this.uuid = uuid;
    }

    public boolean isBanned() {
        return table.existEntry(uuidc, uuid.toString());
    }

    public String getReason() {
        return (String) table.getValue(reasonc, uuidc, uuid.toString());
    }

    public Date getExpireDate() {
        return new Date(Long.parseLong((String) table.getValue(expiredatec, uuidc, uuid.toString())));
    }

    public String getUuidBannedFrom() {
        return (String) table.getValue(bannedbyuuidc, uuidc, uuid.toString());
    }

    public History getHistory() {
        return new History(getHistoryRaw());
    }

    private String getHistoryRaw() {
        return (String) table.getValue(historyc, uuidc, uuid.toString());
    }

    //* Setter needed (private and being used to ban people and add Ban-History)
    private void setReason(String reason) {
        table.setValue(reasonc, reason, uuidc, uuid.toString());
    }

    private void setExpiredate(Date date) {
        table.setValue(expiredatec, ""+date.getTime(), uuidc, uuid.toString());
    }

    private void setUUIDBannedFrom(String tuuid) {
        table.setValue(bannedbyuuidc, tuuid, uuidc, uuid.toString());
    }

    private void addHistory(HistoryElement element) {
        String history = getHistoryRaw();
        if(history == null) history = element.toString()+";";
        else history = element.toString()+";"+history;
        table.setValue(historyc, history, uuidc, uuid.toString());
    }

    private void setLatestHistoryUnbanned() {
        String history = getHistoryRaw();
        if(history == null || history.isEmpty()) return;
        String[] split = history.split(";");
        String historyinfo = split[0];
        history = history.substring(historyinfo.length()+1);
        HistoryElement element = HistoryElement.fromString(historyinfo);
        element.setUnban(true);
        history = element.toString()+";"+history;
        table.setValue(historyc, history, uuidc, uuid.toString());
    }

    public void clearHistory() {
        table.setValue(historyc, "", uuidc, uuid.toString());
    }

    public String banID(int id, String bannedby) {
        long length = 1000;
        String reason = "NONE";
        switch (id) {
            case 1:
                reason = "Hacking/Cheating";
                length = 1000L*60L*60L*24L*30L;
                break;
            case 2:
                reason = "Peterismus";
                length = 1000L*60L*60L*24L*30L*3L;
                break;
            case 99:
                reason = "Admin";
                length = 1000L * 60L * 60L * 24000000L;
                break;
            default:
                return "§cID doesnt exist.";
        }
        ban(length, reason, bannedby);
        return "§7Der Spieler §e" + Bukkit.getOfflinePlayer(uuid).getName() + " §7wurde für §3" + reason + " §7gebannt.";
    }

    public void ban(long durationinmilliseconds, String reason, String bannedby) {

        Date date = new Date();
        long l = date.getTime();
        l = l + durationinmilliseconds;
        date.setTime(l);

        boolean reban = isBanned();
        setExpiredate(date);
        setReason(reason);
        setUUIDBannedFrom(bannedby);
        addHistory(new HistoryElement(date, reban, false, bannedby, reason, durationinmilliseconds));
        if(Bukkit.getPlayer(uuid) != null) Bukkit.getPlayer(uuid).kickPlayer(getBanMessage());
    }

    public void unban(boolean byexpired) {
        if(!byexpired) setLatestHistoryUnbanned();
        table.delete(uuidc, uuid.toString());
    }

    public String getBanMessage() {
        String from = getUuidBannedFrom();
        if(!from.equalsIgnoreCase("Server") && !from.equalsIgnoreCase("Console")) from = Bukkit.getOfflinePlayer(UUID.fromString(from)).getName();

        String until = "§cpermanent";
        Date date = getExpireDate();
        if(date.getTime() - new Date().getTime() < 31556952000L*5L) {
            until = date.toLocaleString();
        }
        return "§8§l» §3§lLunania.net §8§l«" + "§r\n \n \n" + "§3Reason: §7"+getReason() + "\n \n" + "§3Until: §7" + until + "\n§3From: §7"+from;
    }

}