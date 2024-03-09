package de.koppy.basics.api.scoreboard;

import de.koppy.lunaniasystem.LunaniaSystem;
import de.koppy.quest.api.PlayerQuest;
import de.koppy.quest.api.Quest;
import de.koppy.server.Server;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ServerBossBar {

    private BossBar bar;
    private UUID uuid;

    public ServerBossBar(UUID uuid) {
        this.uuid = uuid;
        bar = Bukkit.createBossBar("§cNONE", BarColor.YELLOW, BarStyle.SOLID);
        trackQuest(null);
        addPlayer(Bukkit.getPlayer(uuid));
    }

    public void setColor(BarColor color) {
        bar.setColor(color);
    }

    public void setStyle(BarStyle style) {
        bar.setStyle(style);
    }

    public void addPlayer(Player player) {
        bar.addPlayer(player);
    }

    public void setServerInfo() {
        bar.setColor(BarColor.WHITE);
        int day = LunaniaSystem.getServerInstance().getDay();
        int year = day / 365;
        day = day % 365;
        bar.setTitle("§eYear "+year + ", Day "+day);
    }

    public void trackQuest(Quest quest) {
        if(quest == null) {
            setServerInfo();
            return;
        }
        bar.setColor(BarColor.GREEN);
        PlayerQuest pq = new PlayerQuest(uuid);
        int stage = pq.getStage(quest.getIdentifierName());
        bar.setTitle("§2§l"+quest.getDisplayname()+": §r§7" + quest.getStep(stage).getName());
    }

}
