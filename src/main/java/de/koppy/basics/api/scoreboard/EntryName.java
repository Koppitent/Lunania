package de.koppy.basics.api.scoreboard;

import org.bukkit.ChatColor;

public enum EntryName {

    ENRTY_0(0, ChatColor.DARK_AQUA.toString()),
    ENRTY_1(1, ChatColor.DARK_BLUE.toString()),
    ENRTY_2(2, ChatColor.AQUA.toString()),
    ENRTY_3(3, ChatColor.BLACK.toString()),
    ENRTY_4(4, ChatColor.BLUE.toString()),
    ENRTY_5(5, ChatColor.BOLD.toString()),
    ENRTY_6(6, ChatColor.DARK_GREEN.toString()),
    ENRTY_7(7, ChatColor.GOLD.toString()),
    ENRTY_8(8, ChatColor.DARK_GRAY.toString()),
    ENRTY_9(9, ChatColor.DARK_PURPLE.toString()),
    ENRTY_10(10, ChatColor.DARK_RED.toString()),
    ENRTY_11(11, ChatColor.GRAY.toString()),
    ENRTY_12(12, ChatColor.GREEN.toString()),
    ENRTY_13(13, ChatColor.ITALIC.toString());

    private final int entry;
    private final String entryName;

    private EntryName(int entry, String entryName) {
        this.entry = entry;
        this.entryName = entryName;
    }

    public int getEntry() {
        return entry;
    }

    public String getEntryName() {
        return entryName;
    }

}