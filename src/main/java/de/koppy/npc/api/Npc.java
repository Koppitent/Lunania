package de.koppy.npc.api;

import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R2.CraftServer;
import org.bukkit.craftbukkit.v1_20_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;

public class Npc {

    public static HashMap<String, Npc> npclist = new HashMap<String, Npc>();
    public static HashMap<Player, Npc> selectednpc = new HashMap<Player, Npc>();
    public static ArrayList<Npc> npcs = new ArrayList<Npc>();

    private String name;
    private String displayname;
    private String prefix;
    private String texture = "";
    private String signature = "";
    private UUID uuid;
    private GameProfile gp;
    private Location location;
    private ServerPlayer npc;
    private NpcType type;
    private String typecontent;
    private boolean isLooking = false;
    private ClientboundSetEntityDataPacket packettest;

    public Npc(String name, Location location) {
        this.name = name;
        this.displayname = name;
        this.location = location;
        this.uuid = UUID.randomUUID();
        this.prefix = "";
        this.typecontent = "help";
        this.type = NpcType.NONE;
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        ServerLevel world = ((CraftWorld) location.getWorld()).getHandle();
        this.gp = new GameProfile(uuid, displayname);
        this.gp.getProperties().put("textures", new Property("texture", texture, signature));
        this.npc = new ServerPlayer(server, world, gp, ClientInformation.createDefault());
        npc.moveTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        SynchedEntityData dw = npc.getEntityData();
        dw.set(new EntityDataAccessor<Byte>(17, EntityDataSerializers.BYTE), (byte) 0x7F);
        this.packettest = new ClientboundSetEntityDataPacket(npc.getId(), dw.packDirty());

        String[] skinname = Npc.getSkin("Koppitent");
        setSkin(skinname[0], skinname[1]);

        npcs.add(this);
        npclist.put(name, this);
        new NPCFile(name).create(this);
    }

    public Npc(String name, String displayname, Location location, String texture, String signature, String typecontent, NpcType type, boolean looking, String prefix) {
        this.name = name;
        this.displayname = displayname;
        this.location = location;
        this.prefix = prefix;
        this.uuid = UUID.randomUUID();
        this.type = type;
        this.typecontent = typecontent;
        this.isLooking = looking;
        this.texture = texture;
        this.signature = signature;

        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        ServerLevel world = ((CraftWorld) location.getWorld()).getHandle();
        if(displayname.equals("-")) {
            this.gp = new GameProfile(uuid, "");
        }else {
            this.gp = new GameProfile(uuid, displayname);
        }
        this.gp.getProperties().put("textures", new Property("texture", texture, signature));
        this.npc = new ServerPlayer(server, world, gp, ClientInformation.createDefault());
        npc.moveTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        SynchedEntityData dw = npc.getEntityData();
        dw.set(new EntityDataAccessor<Byte>(17, EntityDataSerializers.BYTE), (byte) 0x7F);
        this.packettest = new ClientboundSetEntityDataPacket(npc.getId(), dw.packDirty());

        setSkin(texture, signature);

        npcs.add(this);
        npclist.put(name, this);
        new NPCFile(name).create(this);
    }

    public String getDisplayname() {
        return displayname;
    }

    public NpcType getType() {
        return type;
    }

    public String getTypeContent() {
        return typecontent;
    }

    public void setType(NpcType type) {
        this.type = type;
        new NPCFile(name).saveType(type, this.typecontent);
    }

    public void setTypeContent(String content) {
        this.typecontent = content.replace("_", " ");
        new NPCFile(name).saveType(type, typecontent);
    }

    public void setLooking(boolean isLooking) {
        this.isLooking = isLooking;
        new NPCFile(name).saveLooking(isLooking);
    }

    public boolean isLooking() {
        return isLooking;
    }

    public Location getLocation() {
        return location;
    }

    public void animation(int animation) {
        ClientboundAnimatePacket packet = new ClientboundAnimatePacket(npc, animation);
        sendPacket(packet);
    }

    public void animation(Player player, int animation) {
        ClientboundAnimatePacket packet = new ClientboundAnimatePacket(npc, animation);
        sendPacket(packet, player);
    }

    private void sendPacket(net.minecraft.network.protocol.Packet<?> packet){
        for(Player player : Bukkit.getOnlinePlayers()){
            sendPacket(packet, player);
        }
    }

    private void sendPacket(net.minecraft.network.protocol.Packet<?> packet,Player player){
        ((CraftPlayer)player).getHandle().connection.send(packet);
    }

    public void showAll() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            show(player);
        }
    }

    public void hide(Player player) {
        ServerPlayerConnection connection = ((CraftPlayer) player).getHandle().connection;
        connection.send(new ClientboundRemoveEntitiesPacket(npc.getId()));
    }

    @SuppressWarnings("deprecation")
    public void show(Player player) {
        ServerPlayerConnection connection = ((CraftPlayer) player).getHandle().connection;
        connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, npc));
        connection.send(new ClientboundAddEntityPacket(npc));
        connection.send(new ClientboundEntityEventPacket(npc, (byte) (npc.getBukkitYaw() * 256 * 360)));
        connection.send(packettest);

        Scoreboard sb = player.getScoreboard();
        Team team = sb.getTeam(this.getName());
        if(team == null) team = sb.registerNewTeam(this.getName());
        team.setPrefix(prefix);
        team.setDisplayName(this.displayname);
        team.setColor(ChatColor.GRAY);
        team.addPlayer(npc.getBukkitEntity().getPlayer());

        player.setScoreboard(sb);
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setLocation(Location location) {
        this.location = location;
        this.npc.moveTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        new NPCFile(name).saveLocation(location);
    }

    public void setPrefix(String prefix) {
        if(prefix.equals("-")) prefix = "";
        this.prefix = prefix.replace("&", "§").replace("_", " ");
        showAll();
        new NPCFile(name).savePrefix(prefix.replace("&", "§").replace("_", " "));
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
        update();
        showAll();
        new NPCFile(name).saveDisplayname(displayname);
    }

    public int getEntityID() {
        return this.npc.getBukkitEntity().getEntityId();
    }

    public int getID() {
        return this.npc.getId();
    }

    public void look(Player player) {
        ServerPlayerConnection connection = ((CraftPlayer) player).getHandle().connection;
        Location npcloc = this.location;
        npcloc = npcloc.setDirection(player.getLocation().subtract(npcloc).toVector());
        float yaw = npcloc.getYaw();
        float pitch = npcloc.getPitch();
        connection.send(new ClientboundMoveEntityPacket.Rot(npc.getId(), (byte) ((yaw%360.)*256/360), (byte) ((pitch%360.)*256/360), false));
        connection.send(new ClientboundRotateHeadPacket(npc, (byte) ((yaw%360.)*256/360)));
    }

    public void setSkin(String texture, String signature) {
        this.texture = texture;
        this.signature = signature;
        update();
        new NPCFile(name).saveSkin(texture, signature);
    }

    public String getTexture() {
        return texture;
    }

    public String getSignature() {
        return signature;
    }

    @SuppressWarnings("deprecation")
    public static String[] getSkin(String name) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/"+name);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            String uuid = new JsonParser().parse(reader).getAsJsonObject().get("id").getAsString();

            URL url2 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid
                    + "?unsigned=false");
            InputStreamReader reader2 = new InputStreamReader(url2.openStream());
            JsonObject property = new  JsonParser().parse(reader2).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            String texture = property.get("value").getAsString();
            String signature = property.get("signature").getAsString();
            return new String[] {texture, signature};
        }catch (Exception e) {
            return getSkin("Koppitent");
        }
    }

    public void update() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            destroy(player);
        }
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        ServerLevel world = ((CraftWorld) Bukkit.getWorld(this.location.getWorld().getName())).getHandle();
        if(displayname.equals("-")) {
            this.gp = new GameProfile(UUID.randomUUID(), "");
        }else {
            this.gp = new GameProfile(UUID.randomUUID(), displayname);
        }
        gp.getProperties().put("textures", new Property("textures", texture, signature));
        npc = new ServerPlayer(server, world, gp, ClientInformation.createDefault());
        npc.moveTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        SynchedEntityData dw = npc.getEntityData();
        dw.set(new EntityDataAccessor<Byte>(17, EntityDataSerializers.BYTE), (byte) 0x7F);
        packettest = new ClientboundSetEntityDataPacket(npc.getId(), dw.packDirty());

        try {
            Field ff = gp.getClass().getDeclaredField("name");
            ff.setAccessible(true);
            ff.set(gp, displayname.replace("&", "§"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void destroy(Player player) {
        ServerPlayerConnection connection = ((CraftPlayer) player).getHandle().connection;
        connection.send(new ClientboundRemoveEntitiesPacket(npc.getId()));
    }

    public byte getFixedRotation2(float yawpitch) {
        return (byte) (360 - (yawpitch * 256 / 360));
    }

    public byte getFixedRotation(float yawpitch) {
        return (byte) ((yawpitch * 256 / 360));
    }

    public void delete() {
        for(Player all : Bukkit.getOnlinePlayers()) { hide(all); }
        new NPCFile(name).delete();
        npcs.remove(this);
        npclist.remove(name);
    }

}