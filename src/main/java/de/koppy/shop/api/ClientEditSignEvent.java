package de.lunania.shop.api;

import de.koppy.lunaniasystem.LunaniaSystem;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClientEditSignEvent extends Event{

    //*
    public static HandlerList handlers = new HandlerList();
    Player player;
    String[] lines;
    Sign sign;
    Block block;

    //* Object CREATE
    public ClientEditSignEvent(Player player, String lines[], Sign sign, Block block) {
        this.player = player;
        this.lines = lines;
        this.sign = sign;
        this.block = block;
    }

    //* Functions
    public Player getPlayer() {
        return this.player;
    }

    public Sign getSign() {
        return this.sign;
    }

    public Block getBlock() {
        return this.block;
    }

    public String[] getLines() {
        return this.lines;
    }

    public void setLine(final Integer line, final String text) {
        final Sign signd = this.sign;
        Bukkit.getScheduler().runTaskLater(LunaniaSystem.getPlugin(), new Runnable() {
            @SuppressWarnings("deprecation")
            public void run() {
                signd.setLine(line, text);
                signd.update();
            }
        }, 1);
    }

    public String getLine(Integer line) {
        if(line >= 0 && line <= 3 && getLines()[line] != null) return getLines()[line];
        else return null;
    }

    //* Handler
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}