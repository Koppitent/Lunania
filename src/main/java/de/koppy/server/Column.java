package de.koppy.server;

public class Column {

    private String name;
    private ColumnType type;
    private int length;

    public Column(String name, ColumnType type, int length) {
        this.name = name;
        this.type = type;
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public ColumnType getType() {
        return type;
    }

    public int getLength() {
        return length;
    }

    @Override
    public String toString() {
        return name+" "+type.getStringWithLength(length);
    }

}
