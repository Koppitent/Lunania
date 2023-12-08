package de.koppy.land.api;

public enum Flag {
    PVP, PVE, TNT;

    public static boolean isFlag(String flags) {
        for(Flag flag : Flag.values()) {
            if(flags.equalsIgnoreCase(flag.toString())) {
                return true;
            }
        }
        return false;
    }

    public static Flag getFlag(String flags) {
        for(Flag flag : Flag.values()) {
            if(flags.equalsIgnoreCase(flag.toString())) {
                return flag;
            }
        }
        return null;
    }
}
