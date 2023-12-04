package de.koppy.server;

public enum ColumnType {

    VARCHAR, DOUBLE, INT, BOOL, TEXT;

    @Override
    public String toString() {
        if(this == TEXT) return "TEXT";
        if(this == DOUBLE) return "DOUBLE";
        if(this == VARCHAR) return "VARCHAR";
        if(this == INT) return "INT";
        if(this == BOOL) return "BOOL";
        return null;
    }

    public String getStringWithLength(int length) {
        if(this == DOUBLE) return "DOUBLE("+length+","+2+")";
        if(this == VARCHAR) return "VARCHAR("+length+")";
        if(this == INT) return "INT("+length+")";
        if(this == BOOL) return "BOOL";
        if(this == TEXT) return "TEXT("+length+")";
        return null;
    }

}
