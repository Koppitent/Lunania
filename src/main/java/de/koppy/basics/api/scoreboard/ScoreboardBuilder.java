package de.koppy.basics.api.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public abstract class ScoreboardBuilder {

    protected final Scoreboard board;
    protected final Objective obj;

    protected final Player player;

    @SuppressWarnings("deprecation")
    public ScoreboardBuilder(Player player) {
        this.player = player;
        if(player.getScoreboard().equals(Bukkit.getScoreboardManager().getMainScoreboard())) {
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }
        this.board = player.getScoreboard();

        if(board.getObjective("display") != null) {
            board.getObjective("display").unregister();
        }

        this.obj = board.registerNewObjective("display", "dummy", "§f");
        this.obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        System.out.println("Checked for Players scoreboard");

        createScoreboard();
        update();
    }

    public void hideScoreboard() {
        this.obj.setDisplaySlot(null);
    }

    public void showScoreboard() {
        this.obj.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public abstract void createScoreboard();

    public abstract void update();

    public void setDisplayname(String displayname) {
        this.obj.setDisplayName(displayname);
    }

    public void setScore(String content, int score) {
        Team team = getTeamByScore(score);

        if(team == null) {
            return;
        }

        team.setPrefix(content);
        showScore(score);
    }

    public void removeScore(int score) {
        hideScore(score);
    }

    private EntryName getEntryNameByScore(int score) {
        for(EntryName entry : EntryName.values()) {
            if(entry.getEntry() == score) {
                return entry;
            }
        }
        return null;
    }

    private Team getTeamByScore(int score) {
        EntryName name = getEntryNameByScore(score);

        if(name == null) return null;

        Team team = board.getEntryTeam(name.getEntryName());

        if(team != null) {
            return team;
        }

        team = board.registerNewTeam(name.name());
        team.addEntry(name.getEntryName());

        return team;
    }

    private void showScore(int score) {
        EntryName name = getEntryNameByScore(score);

        if(name == null) {
            return;
        }

        if(obj.getScore(name.getEntryName()).isScoreSet()) {
            return;
        }

        obj.getScore(name.getEntryName()).setScore(score);
    }

    private void hideScore(int score) {
        EntryName name = getEntryNameByScore(score);

        if(name == null) {
            return;
        }

        if(!obj.getScore(name.getEntryName()).isScoreSet()) {
            return;
        }

        board.resetScores(name.getEntryName());
    }

}