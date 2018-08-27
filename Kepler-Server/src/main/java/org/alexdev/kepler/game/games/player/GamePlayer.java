package org.alexdev.kepler.game.games.player;

import org.alexdev.kepler.game.games.Game;
import org.alexdev.kepler.game.games.GameManager;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;

public class GamePlayer {
    private Player player;
    private int userId;
    private int gameId;
    private int teamId;
    private Position position;
    private int score;
    private boolean enteringGame;
    private boolean inGame;

    public GamePlayer(Player player) {
        this.player = player;
        this.userId = player.getDetails().getId();
        this.teamId = -1;
        this.gameId = -1;
        this.score = 0;
        this.inGame = false;
        this.enteringGame = false;
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

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public Position getSpawnPosition() {
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

    public boolean isEnteringGame() {
        return enteringGame;
    }

    public void setEnteringGame(boolean enteringGame) {
        this.enteringGame = enteringGame;
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }
}
