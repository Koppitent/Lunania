package de.koppy.npc.api;

public enum NpcType {
    COMMAND,
    NONE,
    ADMINSHOP,
    BANK;

    public NpcType getType() {
        return this;
    }

    public static NpcType fromString(String string) {
        for(NpcType type : NpcType.values()) {
            if(type.toString().equalsIgnoreCase(string)) {
                return type;
            }
        }
        return COMMAND;
    }
}
