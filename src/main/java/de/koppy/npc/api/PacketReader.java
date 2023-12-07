package de.koppy.npc.api;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import de.koppy.lunaniasystem.LunaniaSystem;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;

public class PacketReader {

    private Player player;
    private ChannelPipeline pipeline;
    private Channel channel;

    public PacketReader(Player player) {
        this.player = player;
    }

    public Object getValue2(Object packet, String fieldName) {
        try {
            Field field = packet.getClass().getSuperclass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(packet);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public void inject() {
        UUID uuid = player.getUniqueId();
        String readerName = "Reader-" + uuid;
        CraftPlayer craftPlayer = (CraftPlayer) player;
        channel = ((Connection) getValue2(craftPlayer.getHandle().connection, "c")).channel;
        pipeline = channel.pipeline();
        if (channel.pipeline() == null || channel.pipeline().get(readerName) != null) {
            return;
        }
        channel.pipeline().addAfter("decoder", "PacketInjector", new MessageToMessageDecoder<Packet<?>>() {
            @Override
            protected void decode(ChannelHandlerContext ctx, Packet<?> packet, List<Object> list) throws Exception {
                list.add(packet);
                readPacket(packet);
            }
        });
        if(LunaniaSystem.getServerInstance().isConsoledebug()) Bukkit.getConsoleSender().sendMessage("§4INFO §dPlayer Injected");
    }

    public void uninject(){
        if(pipeline.get("PacketInjector") != null) {
            pipeline.remove("PacketInjector");
        }
    }

    public static HashMap<Player, Npc> cooldown = new HashMap<Player, Npc>();
    public void readPacket(final Packet<?> packet) {
        if(LunaniaSystem.getServerInstance().isConsoledebug())Bukkit.getConsoleSender().sendMessage("§4INFO §d" + packet.toString());
        if(packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInUseEntity")) {
            int id = (Integer) getValue(packet, "a");
            for(final Npc npc : Npc.npcs) {
                if(npc.getID() == id) {
                    if(cooldown.containsKey(player)) {
                        if(cooldown.get(player).getID() == id) {
                            return;
                        }
                    }
                    cooldown.put(player, npc);
                    Bukkit.getScheduler().runTaskLater(LunaniaSystem.getPlugin(), new Runnable() {
                        public void run() {
                            cooldown.remove(player);
                        }
                    }, 20*1);
                    if(getValue(packet, "b").toString().startsWith("net.minecraft.network.protocol.game.PacketPlayInUseEntity$1")) {
                        Bukkit.getScheduler().runTaskLater(LunaniaSystem.getPlugin(), new Runnable() {
                            public void run() {
                                Bukkit.getServer().getPluginManager().callEvent(new NpcHitEvent(npc, Action.ATTACK, player));
                                npc.animation(player, 0);
                            }
                        }, 2);
                        return;
                    }else if(getValue(packet, "b").toString().startsWith("net.minecraft.network.protocol.game.PacketPlayInUseEntity$d") || getValue(packet, "b").toString().startsWith("net.minecraft.network.protocol.game.PacketPlayInUseEntity$e")){
                        Bukkit.getScheduler().runTaskLater(LunaniaSystem.getPlugin(), new Runnable() {
                            public void run() {
                                Bukkit.getServer().getPluginManager().callEvent(new NpcHitEvent(npc, Action.INTERACT, player));
                            }
                        }, 2);
                        return;
                    }
                }
            }
        }
//		else if(packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInUpdateSign")){
//
//			final String[] lines = (String[]) getValue(packet, "b");
//			System.out.println(lines);
//			Bukkit.getScheduler().runTaskLater(LunaniaSystem.getPlugin(), new Runnable() {
//				@SuppressWarnings("deprecation")
//				public void run() {
//					BlockPos blockposition = (BlockPos) getValue(packet, "a");
//					Sign sign = (Sign) player.getWorld().getBlockAt(new Location(player.getWorld() ,blockposition.getX(), blockposition.getY(), blockposition.getZ())).getState();
//					Bukkit.getPluginManager().callEvent(new ClientEditSignEvent(player, lines, sign, player.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ())));
//					Bukkit.getPluginManager().callEvent(new SignChangeEvent(player.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()), player, lines));
//				}}, 1);
//
//		}
    }

    public void setValue(Object obj,String name,Object value){
        try{
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(obj, value);
        }catch(Exception e){}
    }

    public Object getValue(Object obj,String name){
        try {
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(obj);
        }catch(Exception e){ }
        return null;
    }

}