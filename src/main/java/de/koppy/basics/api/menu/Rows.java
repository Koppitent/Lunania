package de.koppy.basics.api.menu;

public enum Rows {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6);

    private final int size;
    Rows(int i) {
        size = i;
    }
    public int getSize() {
        return size*9;
    }
}
