package org.alexdev.kepler.game.games.player;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

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

    public List<GamePlayer> getActivePlayers() {
        return playerList.stream().filter(GamePlayer::isInGame).collect(Collectors.toList());
    }


    public List<GamePlayer> getPlayers() {
        return playerList;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
