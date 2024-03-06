package de.koppy.bansystem.api;

import de.koppy.basics.api.ItemBuilder;
import de.koppy.lunaniasystem.api.UI;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.UUID;

public class MuteUI extends UI {

    private UUID uuid;
    private String mutedbyuuid;

    public MuteUI(UUID uuid, String mutedbyuuid) {
        super("§e" + Bukkit.getOfflinePlayer(uuid).getName() + "'s §cMuteMenu", 9*4);
        this.uuid = uuid;
        this.mutedbyuuid = mutedbyuuid;
    }

    public String getMutedbyuuid() {
        return this.mutedbyuuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setMainMenu() {
        inventory.clear();
        fillGlass(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayname("§c").getItemStack());
        inventory.setItem(4, new ItemBuilder(Material.PLAYER_HEAD).setSkull(Bukkit.getOfflinePlayer(uuid).getName()).setDisplayname("§3"+Bukkit.getOfflinePlayer(uuid).getName()).getItemStack());
        inventory.setItem(20, new ItemBuilder(Material.ANVIL).setDisplayname("§bMute by ID").getItemStack());
        inventory.setItem(24, new ItemBuilder(Material.CLOCK).setDisplayname("§bMute by time").getItemStack());
    }

    public void setIdList() {
        inventory.clear();
        setGlassRand(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayname("§c").getItemStack());
        for(MuteIDs banids : MuteIDs.muteids.values()) {
            inventory.addItem(new ItemBuilder(banids.getMaterial()).setDisplayname("§c#"+banids.getId()).setLocalizedName("#"+banids.getId()).addLore("§c" + banids.getReason()).getItemStack());
        }
        inventory.setItem(inventory.getSize()-2, new ItemBuilder(Material.PLAYER_HEAD).setSkull("MHF_ArrowLeft").setDisplayname("§cBack").getItemStack());
    }

    public void setTimer(int time, int u) {
        inventory.clear();
        fillGlass(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayname("§c").getItemStack());
        inventory.setItem(12, new ItemBuilder(Material.PLAYER_HEAD).setSkull("MHF_ArrowLeft").setDisplayname("§7Less Number").setLocalizedName(""+(time-1)).getItemStack());

        switch (time) {
            case 0:
                inventory.setItem(13, new ItemBuilder(Material.PLAYER_HEAD).getSkullValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmI4N2I0NDExMWE5ZjMzOWVkNzAxNWQwZjJjYjY0NmNlNmI4YzU5YTBiNzUwYjI3MjQ0OWFlYWMyNTYyNWRmYSJ9fX0=").setDisplayname("§30").setLocalizedName(""+time).getItemStack());
                break;
            case 1:
                inventory.setItem(13, new ItemBuilder(Material.PLAYER_HEAD).getSkullValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDJkNGE2OTkzN2UwYmVhZGMzODQyNmMwOTk0YjUwZDk1MDQwNmZkOGRhOWYzMWM1ODJkNDZmM2I5YmZjNGM1YiJ9fX0=").setDisplayname("§31").setLocalizedName(""+time).getItemStack());
                break;
            case 2:
                inventory.setItem(13, new ItemBuilder(Material.PLAYER_HEAD).getSkullValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzBhNmM3YTBkNjU4YmI5MGUyN2I1OTM0ZjYyYTVlMTVjYzljMTFjODdhZTE0NjRhNGU3OWVhNjY1MjNiYTM2MSJ9fX0=").setDisplayname("§32").setLocalizedName(""+time).getItemStack());
                break;
            case 3:
                inventory.setItem(13, new ItemBuilder(Material.PLAYER_HEAD).getSkullValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTYxYjMxYTg3Yjc4MjYyYzYzZTk0NzE0ZTU2MjRhMmFiNTk1MGY3NWRlZTMyY2MzMDI2YTVmYTc4MjM0NjhkZSJ9fX0=").setDisplayname("§33").setLocalizedName(""+time).getItemStack());
                break;
            case 4:
                inventory.setItem(13, new ItemBuilder(Material.PLAYER_HEAD).getSkullValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2FkZmQzYzk5OTY3ZDMyNzQ5MDJlY2I2ZTk4NjU4YWNmZGIzOTE4NzE3YjJlOTAzN2Y2MWMzYjRlMDllMmExIn19fQ==").setDisplayname("§34").setLocalizedName(""+time).getItemStack());
                break;
            case 5:
                inventory.setItem(13, new ItemBuilder(Material.PLAYER_HEAD).getSkullValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGJiYWYwMTkwOTIyMWI5YWJlOTQ1YWZlN2RmZGI3MmYzMTczMzExZTU2MjAxOTRkZDI3MDExYTZkNTU0ZmZjOCJ9fX0=").setDisplayname("§35").setLocalizedName(""+time).getItemStack());
                break;
            case 6:
                inventory.setItem(13, new ItemBuilder(Material.PLAYER_HEAD).getSkullValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTZhZTBmZTIyNTZhZTM1NmEyNWYxMzBhZTcxY2Y0NDMxNTE1N2M1ZmFlOTFkNjJhNGZmYjU4NWIxNjQ4NjM3MyJ9fX0=").setDisplayname("§36").setLocalizedName(""+time).getItemStack());
                break;
            case 7:
                inventory.setItem(13, new ItemBuilder(Material.PLAYER_HEAD).getSkullValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGYwOWVmZTczMWU3M2M4MGIxYWVlMTAwYmIzMzBhYjQxNDU5NWVlNTRhNGUyZGVjNDM5YmVkM2UzNjQ5YWM5NiJ9fX0=").setDisplayname("§37").setLocalizedName(""+time).getItemStack());
                break;
            case 8:
                inventory.setItem(13, new ItemBuilder(Material.PLAYER_HEAD).getSkullValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDcyN2Q0ZTQ4ZjIzMWNlNGQ4NzE5OTI1NjBmNTFiZjZhM2YxNTdjMmZmZDZmOTJiODYwY2JiNTMxMjg0MjZhMiJ9fX0=").setDisplayname("§38").setLocalizedName(""+time).getItemStack());
                break;
            case 9:
                inventory.setItem(13, new ItemBuilder(Material.PLAYER_HEAD).getSkullValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTQ3M2VhMzUxZjQxZTk5NTdmOTE0ZTNiOTBmNzRlOTg2NzgzOGIzMzM5ZDQyNTEzY2EyNWVkMGY0NWJjNjBjYiJ9fX0=").setDisplayname("§39").setLocalizedName(""+time).getItemStack());
                break;
        }
        inventory.setItem(14, new ItemBuilder(Material.PLAYER_HEAD).setSkull("MHF_ArrowRight").setDisplayname("§7More Number").setLocalizedName(""+(time+1)).getItemStack());

        inventory.setItem(12+9, new ItemBuilder(Material.PLAYER_HEAD).setSkull("MHF_ArrowLeft").setDisplayname("§7Voran").setLocalizedName(""+(u-1)).getItemStack());
        switch (u) {
            case 0:
                inventory.setItem(13+9, new ItemBuilder(Material.PLAYER_HEAD).getSkullValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWE3YmI1N2E3M2YxNmRjZWRjM2IyYzAzYzIxMjNkNDU4ODEwODJhNjhjNTIzNzRhYmVmODQxMjA5ZjRjNDdlOCJ9fX0=").setDisplayname("§3Hours").setLocalizedName(""+u).getItemStack());
                break;
            case 1:
                inventory.setItem(13+9, new ItemBuilder(Material.PLAYER_HEAD).getSkullValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmRhOWU5MjNmMDE1OWFmYTQ1ZjRjN2M3MmIzYjVkYzRjYjY1MTkyODFlOTFhZTIyZGM3NDcwMWY0ZjlhMGRhYSJ9fX0=").setDisplayname("§3Days").setLocalizedName(""+u).getItemStack());
                break;
            case 2:
                inventory.setItem(13+9, new ItemBuilder(Material.PLAYER_HEAD).getSkullValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTNlZGE1ZmQyNTM2NmRjZjNjMWJhZjYzMjI4OWZjYmUyNzQxNGNhNzg0M2RjN2RkNWNiM2VjZjkzYTJhODY3MCJ9fX0=").setDisplayname("§3Weeks").setLocalizedName(""+u).getItemStack());
                break;
            case 3:
                inventory.setItem(13+9, new ItemBuilder(Material.PLAYER_HEAD).getSkullValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2E0ZjNjYzUwY2Q3NTRhNTBmNjQ2ZjEyNGJmYmJkYTY2ZGZjNmQyN2QxMzc2MWM0Zjc1YmE3MGYzMGQzYzllNSJ9fX0=").setDisplayname("§3Months").setLocalizedName(""+u).getItemStack());
                break;
            case 4:
                inventory.setItem(13+9, new ItemBuilder(Material.PLAYER_HEAD).getSkullValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjkzZDJkYjE5NjExYjQ2NzM5NjgyYmZkM2QxNGI1MjNjYzlkZTgwNDYyZDJiMGRjMmFjOTliMThjN2MwNTQwNSJ9fX0=").setDisplayname("§3Years").setLocalizedName(""+u).getItemStack());
                break;
        }
        inventory.setItem(14+9, new ItemBuilder(Material.PLAYER_HEAD).setSkull("MHF_ArrowRight").setDisplayname("§7Danach").setLocalizedName(""+(u+1)).getItemStack());

        inventory.setItem(28, new ItemBuilder(Material.GREEN_CONCRETE).setDisplayname("§7Ban Member!").addLore("§4ACHTUNG!!! §cWatch out. This bans the person.").getItemStack());
        inventory.setItem(inventory.getSize()-2, new ItemBuilder(Material.PLAYER_HEAD).setSkull("MHF_ArrowLeft").setDisplayname("§cBack").getItemStack());
    }

}