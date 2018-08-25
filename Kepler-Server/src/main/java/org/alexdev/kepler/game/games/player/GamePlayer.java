package org.alexdev.kepler.game.games.player;

import org.alexdev.kepler.game.games.Game;
import org.alexdev.kepler.game.games.GameManager;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;

public class GamePlayer {
    private Player player;
    private int userId;
    private int instanceId;
    private int gameId;
    private int teamId;
    private Position position;
    private int score = 0;

    public GamePlayer(Player player) {
        this.player = player;
        this.userId = player.getDetails().getId();
        this.teamId = -1;
        this.gameId = -1;
        this.score = 0;
        this.position = new Position();
    }

    public Game getGame() {
        return GameManager.getInstance().getGameById(this.getGameId());
    }

    public Player getPlayer() {
        return player;
    }

    public int getUserId() {
        return userId;
    }

    public int getInstanceId() {
        return instanceId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public Position getPosition() {
        return position;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }
}
