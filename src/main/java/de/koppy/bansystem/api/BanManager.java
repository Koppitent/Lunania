package de.koppy.bansystem.api;

import de.koppy.bansystem.BanSystem;
import de.koppy.server.Column;
import de.koppy.server.ColumnType;
import de.koppy.server.Table;

import java.util.Date;
import java.util.UUID;

public class BanManager {

    private final static Column uuidc = new Column("uuid", ColumnType.VARCHAR, 200);
    private final static Column reasonc = new Column("reason", ColumnType.VARCHAR, 200);
    private final static Column expiredatec = new Column("expiredate", ColumnType.VARCHAR, 200);
    private final static Column bannedbyuuidc = new Column("bannedbyuuid", ColumnType.VARCHAR, 200);

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
        return new Date((long) table.getValue(expiredatec, uuidc, uuid.toString()));
    }

    public UUID getUuidBannedFrom() {
        return UUID.fromString((String) table.getValue(bannedbyuuidc, uuidc, uuid.toString()));
    }

    //* Setter needed (private and being used to ban people and add Ban-History)


}