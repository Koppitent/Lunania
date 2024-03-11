package de.koppy.basics.api;

import de.koppy.economy.EconomySystem;
import de.koppy.economy.api.PlayerAccount;
import de.koppy.job.api.JobType;
import de.koppy.job.api.PlayerJob;
import de.koppy.lunaniasystem.api.UI;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.text.DecimalFormat;
import java.util.UUID;

public class PlayerProfileUI extends UI {

    private UUID uuid;
    private boolean inAdminPage = false;

    public PlayerProfileUI(UUID uuid) {
        super("§3§lPlayer Profile", 9 * 5);
        this.uuid = uuid;
    }

    public void getMainMenu() {
        inAdminPage = false;
        inventory.clear();
        fillGlass(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayname("§d").getItemStack());
        inventory.setItem(13, new ItemBuilder(Material.PLAYER_HEAD).setSkull(Bukkit.getPlayer(uuid).getName()).setDisplayname("§3§lYour Profile").addLore("§8§o" + uuid.toString()).getItemStack());
        PlayerJob pj = new PlayerJob(uuid);
        inventory.setItem(19, new ItemBuilder(Material.GOLDEN_PICKAXE).setDisplayname("§2Job Miner: ").addLore("§7Current Level: §e" + pj.getLevel(JobType.MINER)).getItemStack());
        inventory.setItem(19 + 9, new ItemBuilder(Material.GRASS_BLOCK).setDisplayname("§2Job Builder: ").addLore("§7Current Level: §e" + pj.getLevel(JobType.BUILDER)).getItemStack());
        inventory.setItem(19 + 9 + 9, new ItemBuilder(Material.WOODEN_AXE).setDisplayname("§2Job Woodcutter: ").addLore("§7Current Level: §e" + pj.getLevel(JobType.WOODCUTTER)).getItemStack());

        PlayerProfile profile = PlayerProfile.getProfile(uuid);
        inventory.setItem(20, new ItemBuilder(Material.SPRUCE_HANGING_SIGN).setDisplayname("§3Info:").addLore("§7Playtime: §e" + formatTime(profile.getTotalPlaytime()))
                .addLore("§7Total Lands: §e" + profile.getLands().size())
                .addLore("§7Total Homes: §e" + profile.getHomes().size())
                .addLore("§7Warptokens: §e" + profile.getWarptokens()).getItemStack());
        PlayerAccount pa = new PlayerAccount(uuid);
        inventory.setItem(20 + 9, new ItemBuilder(Material.PAPER).setDisplayname("§dEconomy:").addLore("§7Balance: §e" + new DecimalFormat("#,###.##").format(pa.getMoney()) + EconomySystem.getEcosymbol()).getItemStack());
        inventory.setItem(20 + 9 + 9, new ItemBuilder(Material.WHITE_SHULKER_BOX).setDisplayname("§3Cases opened:").addLore("§7Total: §e" + profile.getCasesOpened()).getItemStack());

        inventory.setItem(24, new ItemBuilder(Material.PAPER).setDisplayname("§7Receive messages").getItemStack());
        inventory.setItem(24 + 9, new ItemBuilder(Material.ENDER_PEARL).setDisplayname("§7Receive TPA-Requests").getItemStack());
        inventory.setItem(24 + 9 + 9, new ItemBuilder(Material.GOLD_INGOT).setDisplayname("§7Money visible").getItemStack());

        if (profile.isMsgAccept())
            inventory.setItem(25, new ItemBuilder(Material.LIME_DYE).setDisplayname("§aon").getItemStack());
        else inventory.setItem(25, new ItemBuilder(Material.RED_DYE).setDisplayname("§coff").getItemStack());
        if (profile.isTpaAccept())
            inventory.setItem(25 + 9, new ItemBuilder(Material.LIME_DYE).setDisplayname("§aon").getItemStack());
        else inventory.setItem(25 + 9, new ItemBuilder(Material.RED_DYE).setDisplayname("§coff").getItemStack());
        if (pa.isVisible())
            inventory.setItem(25 + 9 + 9, new ItemBuilder(Material.LIME_DYE).setDisplayname("§aon").getItemStack());
        else inventory.setItem(25 + 9 + 9, new ItemBuilder(Material.RED_DYE).setDisplayname("§coff").getItemStack());

        if (Bukkit.getPlayer(uuid).hasPermission("server.admin"))
            inventory.setItem(8, new ItemBuilder(Material.REDSTONE_BLOCK).setDisplayname("§cAdmin-Settings").getItemStack());
    }

    public void getAdminPage() {
        if(!Bukkit.getPlayer(uuid).hasPermission("server.admin")) return;
        inAdminPage = true;
        inventory.clear();
        fillGlass(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayname("§d").getItemStack());
        inventory.setItem(13, new ItemBuilder(Material.PLAYER_HEAD).setSkull(Bukkit.getPlayer(uuid).getName()).setDisplayname("§c§lAdmin Profile").addLore("§8§o" + uuid.toString()).getItemStack());

        PlayerProfile profile = PlayerProfile.getProfile(uuid);
        inventory.setItem(24, new ItemBuilder(Material.BRICKS).setDisplayname("§7Use Texturepack").getItemStack());
        inventory.setItem(24 + 9, new ItemBuilder(Material.PLAYER_HEAD).setSkull("undercover").setDisplayname("§7Nicked").getItemStack());


        if (profile.isUseTexturepack())
            inventory.setItem(25, new ItemBuilder(Material.LIME_DYE).setDisplayname("§aon").getItemStack());
        else inventory.setItem(25, new ItemBuilder(Material.RED_DYE).setDisplayname("§coff").getItemStack());

        if (profile.isNicked())
            inventory.setItem(25 + 9, new ItemBuilder(Material.LIME_DYE).setDisplayname("§aon").getItemStack());
        else inventory.setItem(25 + 9, new ItemBuilder(Material.RED_DYE).setDisplayname("§coff").getItemStack());

        inventory.setItem(inventory.getSize() - 2, new ItemBuilder(Material.PLAYER_HEAD).setSkull("MHF_ArrowLeft").setDisplayname("§cBack").getItemStack());
    }

    public boolean isInAdminPage() {
        return inAdminPage;
    }

    private String formatTime(int seconds) {
        int minutes = 0;
        int hours = 0;
        int days = 0;

        if (seconds >= 60) {
            minutes = seconds / 60;
            seconds = seconds % 60;
        }

        if (minutes >= 60) {
            hours = minutes / 60;
            minutes = minutes % 60;
        }

        if (hours >= 24) {
            days = hours / 24;
            hours = hours % 24;
        }

        String out = "§6";

        if (days > 0) {
            out = out + days + "Days, " + hours + "Hours, " + minutes + "Minutes, " + seconds + "Seconds§7.";
        } else if (hours > 0) {
            out = out + hours + "Hours, " + minutes + "Minutes, " + seconds + "Seconds§7.";
        } else if (minutes > 0) {
            out = out + minutes + "Minutes, " + seconds + "Seconds§7.";
        } else {
            out = out + seconds + "Seconds§7.";
        }

        return out;
    }

}