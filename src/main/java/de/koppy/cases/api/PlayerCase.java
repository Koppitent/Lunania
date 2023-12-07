package de.koppy.cases.api;

import de.koppy.cases.CaseSystem;
import de.koppy.server.Column;
import de.koppy.server.ColumnType;
import de.koppy.server.Table;

import java.util.UUID;

public class PlayerCase {

    private static final Table table = CaseSystem.getTable();
    private static final Column uuidc = new Column("uuid", ColumnType.VARCHAR, 200);
    private UUID uuid;

    public PlayerCase(UUID uuid) {
        this.uuid = uuid;
    }

    public int getAmount(String casename) {
        Column casec = new Column(casename, ColumnType.INT, 200);
        if(table.existEntry(casec, uuidc, uuid.toString())) {
            return (Integer) table.getValue(casec, uuidc, uuid.toString());
        }else {
            return 0;
        }
    }

    public void setAmount(String casename, int amount) {
        Column casec = new Column(casename, ColumnType.INT, 200);
        table.setValue(casec, amount, uuidc, uuid.toString());
    }

    public void addAmount(String casename, int amount) {
        int total = getAmount(casename);
        total = total+amount;
        setAmount(casename, total);
    }

    public void removeAmount(String casename, int amount) {
        int total = getAmount(casename);
        total = total-amount;
        setAmount(casename, total);
    }

    public boolean hasKey(String casename) {
        return getAmount(casename) > 0;
    }

}