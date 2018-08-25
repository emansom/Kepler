package org.alexdev.kepler.game.games.player;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameTeam {
    private int id;
    private int score;
    private List<GamePlayer> playerList;

    public GameTeam(int id) {
        this.id = id;
        this.score = 0;
        this.playerList = new CopyOnWriteArrayList<>();
    }

    public int getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<GamePlayer> getPlayerList() {
        return playerList;
    }
}
