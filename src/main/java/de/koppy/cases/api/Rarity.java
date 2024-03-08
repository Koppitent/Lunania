package de.koppy.cases.api;

public enum Rarity {
    LEGENDARY(0.005D), EPIC(0.055D), RARE(0.14D), UNCOMMON(0.30D), COMMON(0.50D);

    private double chance;
    private Rarity(double chance) {
        this.chance = chance;
    }

    public double getChance() {
        return chance;
    }

    public String toFormatString() {
        if(this == LEGENDARY) return "§6§lLEGENDARY";
        if(this == EPIC) return "§5§lEPIC";
        if(this == RARE) return "§1§lRARE";
        if(this == UNCOMMON) return "§a§lUNCOMMON";
        return "§f§lCOMMON";
    }

}