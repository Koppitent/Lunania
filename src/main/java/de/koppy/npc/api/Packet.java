package de.koppy.npc.api;

import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.network.protocol.game.ClientboundSetSimulationDistancePacket;
import net.minecraft.server.level.ServerPlayer;

public class Packet {

    private Player player;

    public Packet(Player player) {
        this.player = player;
    }

    public int getPing() {
        return ((CraftPlayer) player).getHandle().connection.latency();
    }

    public String getIpAdress() {
        return ((CraftPlayer) player).getHandle().getIpAddress();
    }

    public void sendFakeOP() {
        ServerPlayer entity = ((CraftPlayer)player).getHandle();
        entity.connection.send(new ClientboundEntityEventPacket(entity, (byte)28), null);
    }

    public void setViewdistance(int distance) {
        ServerPlayer entity = ((CraftPlayer)player).getHandle();
        entity.connection.send(new ClientboundSetSimulationDistancePacket(distance), null);
    }

}