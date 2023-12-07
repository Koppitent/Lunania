package de.koppy.npc.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NpcHitEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Npc npc;
    private Action action;
    private Player player;

    public NpcHitEvent(Npc npc, Action action, Player player) {
        this.npc = npc;
        this.action = action;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public Action getAction() {
        return action;
    }

    public Npc getNpc() {
        return npc;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}