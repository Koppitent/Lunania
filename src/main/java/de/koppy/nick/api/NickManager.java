package de.koppy.nick.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import de.koppy.basics.api.PlayerProfile;
import de.koppy.basics.api.scoreboard.Tablist;
import de.koppy.nick.NickSystem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;

import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;

public class NickManager {

    public static HashMap<UUID, String> realnames = new HashMap<UUID, String>();
    private Player player;
    public NickManager(Player player) {
        this.player = player;
    }

    public void changeSkin(String texture, String signature) {

        ServerPlayer ep = ((CraftPlayer) player).getHandle();
        GameProfile gp = ep.getBukkitEntity().getProfile();

        PropertyMap pm = gp.getProperties();
        Property property = pm.get("textures").iterator().next();

        pm.remove("textures", property);
        pm.put("textures", new Property("textures", texture, signature));

        reloadSkinForSelf();

        for (Player all : Bukkit.getOnlinePlayers()) {
            all.hidePlayer(player);
            all.showPlayer(player);
        }
    }


    public void changeName(String name) {
        PlayerProfile profile = PlayerProfile.getProfile(player.getUniqueId());
        profile.setNickname(name);
        CraftPlayer cp = (CraftPlayer) player;
        try {
            GameProfile playerProfile = cp.getProfile();
            Field ff = playerProfile.getClass().getDeclaredField("name");
            ff.setAccessible(true);
            ff.set(playerProfile, name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(Player all : Bukkit.getOnlinePlayers()) {
            all.hidePlayer(player);
            all.showPlayer(player);
            hideTablistNPC(player, all);
            showTablistNPC(player, all);
        }
        Tablist.updateTablistForAll();
    }

    public String[] getRandomSkinData() {
        File file = new File("plugins/Lunania", "skinsaves.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        Random rndm = new Random();
        List<String> skinnames = new ArrayList<String>();
        skinnames.addAll(cfg.getKeys(false));
        int index = rndm.nextInt(cfg.getKeys(false).size());
        return getSkinData(skinnames.get(index));
    }

    public String getRandomNickname() {
        Random rndm = new Random();
        int index = rndm.nextInt(NickSystem.getPredefinednicknames().size());
        return NickSystem.getPredefinednicknames().get(index);
    }

    public static void hideTablistNPC(Player playertoshow, Player target) {
        ServerPlayer ep = ((CraftPlayer) playertoshow).getHandle();
        ServerPlayerConnection connection = ((CraftPlayer) target).getHandle().connection;
        List<UUID> uuids = new ArrayList<UUID>();
        uuids.add(ep.getUUID());
        connection.send(new ClientboundPlayerInfoRemovePacket(uuids));
    }

    public static void showTablistNPC(Player playertoshow, Player target) {
        ServerPlayer ep = ((CraftPlayer) playertoshow).getHandle();
        ServerPlayerConnection connection = ((CraftPlayer) target).getHandle().connection;
        connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, ep));
        connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LISTED, ep));
    }

    @SuppressWarnings("deprecation")
    public void unnick() {
        String name = realnames.get(player.getUniqueId());
        PlayerProfile profile = PlayerProfile.getProfile(player.getUniqueId());
        profile.setNickname("none");
        CraftPlayer cp = (CraftPlayer) player;
        try {
            GameProfile playerProfile = cp.getProfile();
            Field ff = playerProfile.getClass().getDeclaredField("name");
            ff.setAccessible(true);
            ff.set(playerProfile, name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(Player all : Bukkit.getOnlinePlayers()) {
            all.hidePlayer(player);
            all.showPlayer(player);
            hideTablistNPC(player, all);
            showTablistNPC(player, all);
        }
        Tablist.updateTablistForAll();
    }

    public final void reloadSkinForSelf() {
        final ServerPlayer ep = ((CraftPlayer) player).getHandle();
        List<UUID> uuids = new ArrayList<UUID>();
        uuids.add(ep.getUUID());
        ep.connection.send(new ClientboundPlayerInfoRemovePacket(uuids));
        ep.connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, ep));
        ep.connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LISTED, ep));
        final Location loc = player.getLocation().clone();
        if(player.getWorld().getName().equals("world")) player.teleport(Bukkit.getWorld("world_nether").getSpawnLocation());
        else player.teleport(Bukkit.getWorld("world").getSpawnLocation());
        player.teleport(loc);
        player.updateInventory();
    }

    public static boolean existSkinByName(String skinname) {
        File file = new File("plugins/Lunania", "skinsaves.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        return cfg.getKeys(false).contains(skinname);
    }

    public static void saveSkinData(String skinname, String[] skindata) {
        File file = new File("plugins/Lunania", "skinsaves.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        cfg.set(skinname + ".texture", skindata[0]);
        cfg.set(skinname + ".signature", skindata[1]);

        try {cfg.save(file);} catch (IOException e) {e.printStackTrace();}
    }

    public static String[] getSkinData(String skinname) {
        File file = new File("plugins/Lunania", "skinsaves.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        if(cfg.getKeys(false).contains(skinname)) {
            String texture = cfg.getString(skinname + ".texture");
            String signature = cfg.getString(skinname + ".signature");
            return new String[] {texture, signature};
        }else {
            return null;
        }
    }

    @SuppressWarnings("deprecation")
    public static String[] getSkinOrNothing(String name) {
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
            return null;
        }
    }

}