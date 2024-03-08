package de.koppy.mission.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class Mission {

    private String identifier;
    private String name;
    private String description;
    private ItemStack itemshown;
    private Type missiontype;
    private int stages;
    private ItemStack[] rewards;

    public Mission(String identifier, String name, String description, ItemStack itemshown, Type missiontype, int stages, ItemStack[] rewards) {
        this.name = name;
        this.identifier = identifier;
        this.description = description;
        this.itemshown = itemshown;
        this.missiontype = missiontype;
        this.stages = stages;
        this.rewards = rewards;
        MissionHandler.missions.add(this);
    }

    public String getIdentifier() {
        return identifier;
    }

    public ItemStack getShowItem(Player player) {
        ItemStack istack = itemshown.clone();
        ItemMeta istackM = istack.getItemMeta();
        istackM.setLocalizedName(identifier);
        istackM.setDisplayName("§3"+name);
        List<String> lore = new ArrayList<String>();
        lore.add("");
        lore.add("§e"+description);
        lore.add("");
        int urstage = new MissionHandler().getStage(player, identifier, missiontype);
        if(urstage == getStages()) {
            lore.add("§3Progress: claimable!");
            istackM.addEnchant(Enchantment.ARROW_DAMAGE, 2, true);
            istackM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }else if(urstage > getStages()) {
            lore.add("§c§mProgress: finsihed");
        }else {
            lore.add("§3Progress "+urstage + "/" + getStages());
        }
        lore.add("");
        lore.add("§c"+expiringIn());
        istackM.setLore(lore);
        istack.setItemMeta(istackM);

        return istack.clone();
    }

    public ItemStack[] getRewards() {
        return rewards;
    }

    @SuppressWarnings("deprecation")
    public String expiringIn() {
        if(missiontype == Type.DAILY) {
            Date date = new Date();
            Date newmissiondate = new Date();
            newmissiondate.setHours(4);
            newmissiondate.setMinutes(0);
            newmissiondate.setSeconds(0);
            if(date.getHours() > 3) newmissiondate.setDate(date.getDate()+1);

            long mseconds = newmissiondate.getTime()-date.getTime();
            long hours = mseconds / (1000l * 60l * 60l);
            mseconds = mseconds % (1000l * 60l * 60l);
            long minutes = mseconds / (1000l * 60l);
            mseconds = mseconds % (1000l * 60l);
            long seconds = mseconds / 1000l;

            if(hours > 0) {
                return hours+"h "+minutes+"m "+seconds+"s ";
            }else {
                if(minutes > 0) {
                    return minutes+"m "+seconds+"s ";
                }else {
                    return seconds+"s ";
                }
            }
        }else if(missiontype == Type.WEEKLY) {
            Date date = new Date();
            Date newmissiondate = new Date();
            newmissiondate.setHours(4);
            newmissiondate.setMinutes(0);
            newmissiondate.setSeconds(0);
            if(newmissiondate.getDay() != 1) {
                int day = newmissiondate.getDay();
                int add = 0;
                if(day == 2) add = 6;
                if(day == 3) add = 5;
                if(day == 4) add = 4;
                if(day == 5) add = 3;
                if(day == 6) add = 2;
                if(day == 0) add = 1;
                newmissiondate.setDate(newmissiondate.getDate()+add);
            }else {
                if(date.getHours() > 2) newmissiondate.setDate(date.getDate()+7);
            }

            long mseconds = newmissiondate.getTime()-date.getTime();
            long days = mseconds / (1000l * 60l * 60l * 24l);
            mseconds = mseconds % (1000l * 60l * 60l * 24l);
            long hours = mseconds / (1000l * 60l * 60l);
            mseconds = mseconds % (1000l * 60l * 60l);
            long minutes = mseconds / 1000l / 60l;
            mseconds = mseconds % (1000l * 60l);
            long seconds = mseconds / 1000l;

            if(days > 0) {
                return days+"days "+hours+"h "+minutes+"m "+seconds+"s ";
            }else {
                if(hours > 0) {
                    return hours+"h "+minutes+"m "+seconds+"s ";
                }else {
                    if(minutes > 0) {
                        return minutes+"m "+seconds+"s ";
                    }else {
                        return seconds+"s ";
                    }
                }
            }
        }else if(missiontype == Type.SEASONAL) {
            return "Until end of Season";
        }
        return null;
    }

    public int getStages() {
        return stages;
    }

    public Type getMissiontype() {
        return missiontype;
    }

    public ItemStack getItemshown() {
        return itemshown;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}