package org.alexdev.kepler.game.games.player;

import org.alexdev.kepler.game.games.Game;
import org.alexdev.kepler.game.games.GameManager;
import org.alexdev.kepler.game.games.GameObject;
import org.alexdev.kepler.game.games.battleball.enums.BattleballPlayerState;
import org.alexdev.kepler.game.games.snowstorm.TurnContainer;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.player.Player;

public class GamePlayer {
    private Player player;
    private GameObject gameObject;
    private int userId;
    private int objectId;
    private int gameId;
    private int teamId;
    private Position position;
    private int score;
    private boolean enteringGame;
    private boolean isSpectator;
    private boolean inGame;
    private boolean clickedRestart;
    private TurnContainer turnContainer;
    private BattleballPlayerState playerState;
    private int harlequinTeamId;

    public GamePlayer(Player player) {
        this.player = player;
        this.userId = player.getDetails().getId();
        this.teamId = -1;
        this.gameId = -1;
        this.objectId = 0;
        this.harlequinTeamId = -1;
        this.score = 0;
        this.enteringGame = false;
        this.clickedRestart = false;
        this.position = new Position();
        this.turnContainer = new TurnContainer();
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

    public GameTeam getTeam() {
        int teamId = this.getHarlequinTeamId() != -1 ? this.getHarlequinTeamId() : this.getTeamId();
        return this.getGame().getTeams().get(teamId);
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

        if (this.score < 0) {
            this.score = 0;
        }
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

    public boolean isClickedRestart() {
        return clickedRestart;
    }

    public void setClickedRestart(boolean clickedRestart) {
        this.clickedRestart = clickedRestart;
    }

    public boolean isSpectator() {
        return isSpectator;
    }

    public void setSpectator(boolean spectator) {
        isSpectator = spectator;
    }

    public TurnContainer getTurnContainer() {
        return turnContainer;
    }

    public BattleballPlayerState getPlayerState() {
        return playerState;
    }

    public void setPlayerState(BattleballPlayerState playerState) {
        this.playerState = playerState;
    }

    public int getHarlequinTeamId() {
        return harlequinTeamId;
    }

    public void setHarlequinTeamId(int harlequinTeamId) {
        this.harlequinTeamId = harlequinTeamId;
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }
}
