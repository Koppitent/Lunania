package de.koppy.mysql.api;

import de.koppy.lunaniasystem.LunaniaSystem;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Table {

    private String name;
    private ArrayList<Column> columns = new ArrayList<>();

    public Table(String name, Column initcolumn) {
        this.name = name;
        this.columns.add(initcolumn);
    }

    public void createTable() {
        if(columns.isEmpty()) return;
        String columnsS = "";
        for(Column column : columns) { columnsS = columnsS + "," + column.toString(); }
        columnsS = columnsS.substring(1);
        LunaniaSystem.getMySQLInstance().sendStatement("CREATE TABLE IF NOT EXISTS "+name+" ("+columnsS+")");
        updateTable();
    }

    public void updateTable() {
        for(Column column : columns) {
            LunaniaSystem.getMySQLInstance().sendStatement("ALTER TABLE "+name+" ADD IF NOT EXISTS "+column.toString());
        }
    }

    public void addColumn(Column column) { if(!this.columns.contains(column)) this.columns.add(column); }

    public void removeColumn(Column column) { this.columns.remove(column); }

    public void delete(Column wherecolumn, String wherekey) {
        try {
            PreparedStatement ps = LunaniaSystem.getMySQLInstance().getConnection().prepareStatement("DELETE FROM "+name+" WHERE "+wherecolumn.getName()+" = ?");
            ps.setString(1, wherekey);
            ps.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getServer().getConsoleSender().sendMessage("§4Maybe no entry for user in database. (SQLException)");
        }
    }

    public boolean existEntry(Column wherecolumn, String wherekey) {
        try {
            PreparedStatement ps = LunaniaSystem.getMySQLInstance().getConnection().prepareStatement("SELECT "+wherecolumn.getName()+" FROM "+name+" WHERE "+wherecolumn.getName()+" = ?");
            ps.setString(1, wherekey);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            Bukkit.getServer().getConsoleSender().sendMessage("§4Maybe no entry for user in database. (SQLException)");
        }
        return false;
    }

    public boolean existEntryIgnoreCase(Column wherecolumn, String wherekey) {
        try {
            PreparedStatement ps = LunaniaSystem.getMySQLInstance().getConnection().prepareStatement("SELECT "+wherecolumn.getName()+" FROM "+name+" WHERE LCASE("+wherecolumn.getName()+") LIKE LCASE(?)");
            ps.setString(1, wherekey);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            Bukkit.getServer().getConsoleSender().sendMessage("§4Maybe no entry for user in database. (SQLException)");
        }
        return false;
    }

    public boolean existEntry(Column targetcolumn, Column wherecolumn, String wherekey) {
        try {
            PreparedStatement ps = LunaniaSystem.getMySQLInstance().getConnection().prepareStatement("SELECT "+targetcolumn.getName()+" FROM "+name+" WHERE "+wherecolumn.getName()+" = ?");
            ps.setString(1, wherekey);
            ResultSet rs = ps.executeQuery();
            return getValue(targetcolumn, wherecolumn, wherekey) != null;
        } catch (SQLException e) {
            Bukkit.getServer().getConsoleSender().sendMessage("§4Maybe no entry for user in database. (SQLException)");
        }
        return false;
    }

    public void setValue(Column targetcolumn, Object value, Column wherecolumn, String wherekey) {
        if(existEntry(wherecolumn, wherekey)) {
            try {
                PreparedStatement ps = LunaniaSystem.getMySQLInstance().getConnection().prepareStatement("UPDATE "+name+" SET "+targetcolumn.getName()+" = ? WHERE "+wherecolumn.getName()+" = ?");
                if(targetcolumn.getType() == ColumnType.VARCHAR || targetcolumn.getType() == ColumnType.TEXT) {
                    ps.setString(1, (String) value);
                }else if(targetcolumn.getType() == ColumnType.BOOL) {
                    ps.setBoolean(1, (Boolean) value);
                }else if(targetcolumn.getType() == ColumnType.DOUBLE) {
                    ps.setDouble(1, (Double) value);
                }else if(targetcolumn.getType() == ColumnType.INT) {
                    ps.setInt(1, (Integer) value);
                }
                ps.setString(2, wherekey);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else {
            try {
                PreparedStatement ps = LunaniaSystem.getMySQLInstance().getConnection().prepareStatement("INSERT INTO "+name+" ("+wherecolumn.getName()+","+targetcolumn.getName()+") VALUES (?,?)");
                ps.setString(1, wherekey);
                if(targetcolumn.getType() == ColumnType.VARCHAR || targetcolumn.getType() == ColumnType.TEXT) {
                    ps.setString(2, (String) value);
                }else if(targetcolumn.getType() == ColumnType.BOOL) {
                    ps.setBoolean(2, (Boolean) value);
                }else if(targetcolumn.getType() == ColumnType.DOUBLE) {
                    ps.setDouble(2, (Double) value);
                }else if(targetcolumn.getType() == ColumnType.INT) {
                    ps.setInt(2, (Integer) value);
                }
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Object getValue(Column targetcolumn, Column wherecolumn, String wherekey) {
        try {
            PreparedStatement ps = LunaniaSystem.getMySQLInstance().getConnection().prepareStatement("SELECT "+targetcolumn.getName()+" FROM "+name+" WHERE "+wherecolumn.getName()+" = ?");
            ps.setString(1, wherekey);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                if(targetcolumn.getType() == ColumnType.VARCHAR || targetcolumn.getType() == ColumnType.TEXT) {
                    return rs.getString(targetcolumn.getName());
                }else if(targetcolumn.getType() == ColumnType.INT) {
                    return rs.getInt(targetcolumn.getName());
                }else if(targetcolumn.getType() == ColumnType.DOUBLE) {
                    return rs.getDouble(targetcolumn.getName());
                }else if(targetcolumn.getType() == ColumnType.BOOL) {
                    return rs.getBoolean(targetcolumn.getName());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getServer().getConsoleSender().sendMessage("§4Maybe no entry for user in database. (SQLException)");
            return null;
        }
        return null;
    }

    public List<String> getList(Column column) {
        List<String> list = new ArrayList<String>();
        try {
            PreparedStatement ps = LunaniaSystem.getMySQLInstance().getConnection().prepareStatement("SELECT DISTINCT "+column.getName()+" FROM "+name+" ORDER BY "+column.getName());
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String name = rs.getString(column.getName());
                list.add(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getServer().getConsoleSender().sendMessage("§4Maybe no entry for user in database. (SQLException)");
        }

        return list;
    }

    public List<String> getListFilter(Column column, Column column2, boolean bool) {
        List<String> list = new ArrayList<String>();
        try {
            PreparedStatement ps = LunaniaSystem.getMySQLInstance().getConnection().prepareStatement("SELECT DISTINCT "+column.getName()+" FROM " + name + " WHERE " + column2.getName() + " = ?" + " ORDER BY "+column.getName());
            ps.setBoolean(1, bool);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String name = rs.getString(column.getName());
                list.add(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getServer().getConsoleSender().sendMessage("§4Maybe no entry for user in database. (SQLException)");
        }

        return list;
    }

}
