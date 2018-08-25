package org.alexdev.kepler.game.games;

import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.player.PlayerManager;
import org.alexdev.kepler.messages.outgoing.games.GAMEINSTANCE;
import org.alexdev.kepler.messages.types.MessageComposer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game {
    private int id;
    private int mapId;
    private int teamAmount;
    private int gameCreator;

    private GameType gameType;
    private GameState gameState;
    private String name;

    private List<Integer> powerUps;
    private Map<Integer, List<GamePlayer>> teamPlayers;

    private int gameCountdownSeconds = 15;
    private int restartGameSeconds = 1200;
    private int gameLengthSeconds = 180;

    public Game(int id, int mapId, GameType gameType, String name, int teamAmount, int gameCreator) {
        this.id = id;
        this.mapId = mapId;
        this.gameType = gameType;
        this.name = name;
        this.teamAmount = teamAmount;
        this.gameCreator = gameCreator;

        this.powerUps = new ArrayList<>();
        this.teamPlayers = new ConcurrentHashMap<>();

        for (int i = 0; i < teamAmount; i++) {
            this.teamPlayers.put(i, new CopyOnWriteArrayList<>());
        }

        this.gameState = GameState.WAITING;
    }

    /**
     * Moves a player from one team to another team.
     *
     * @param player the player to move
     * @param fromTeamId the team to move from, -1 if just to add to team
     * @param toTeamId the team to move to, -1 if just removing user from team
     */
    public void movePlayer(Player player, int fromTeamId, int toTeamId) {
        GamePlayer gamePlayer = this.getGamePlayer(player.getDetails().getId());

        if (gamePlayer == null) {
            gamePlayer = createGamePlayer(player);
        }

        if (fromTeamId != -1) {
            this.teamPlayers.get(fromTeamId).remove(gamePlayer);
        }

        if (toTeamId != -1) {
            if (!this.teamPlayers.get(toTeamId).contains(gamePlayer)) {
                this.teamPlayers.get(toTeamId).add(gamePlayer);
            }

            gamePlayer.setTeamId(toTeamId);
        } else {
            this.teamPlayers.get(gamePlayer.getTeamId()).remove(gamePlayer);
        }

        this.send(new GAMEINSTANCE(this));
    }

    /**
     * Send a packet to all members in game
     *
     * @param composer the composer to send
     */
    public void send(MessageComposer composer) {
        for (List<GamePlayer> players : this.teamPlayers.values()) {
            for (GamePlayer gamePlayer : players) {
                gamePlayer.getPlayer().send(composer);
            }
        }
    }

    /**
     * Gets if there's enough players in different teams for the game to start
     * (minimum of 2 players)
     *
     * @return true, if successful
     */
    public boolean canGameStart() {
        int activeTeamCount = 0;

        for (int i = 0; i < this.teamAmount; i++) {
            if (this.teamPlayers.get(i).size() > 0) {
                activeTeamCount++;
            }
        }

        return activeTeamCount > 1;
    }

    /**
     * Create the game player instance for a user
     *
     * @param player the player to create the game player instance for
     * @return the game player instance
     */
    public GamePlayer createGamePlayer(Player player) {
        return new GamePlayer(player);
    }

    /**
     * Get the game player instance by id
     * @param userId the id to get the player by
     * @return the game player instance, else if null
     */
    public GamePlayer getGamePlayer(int userId) {
        for (List<GamePlayer> players : this.teamPlayers.values()) {
                for (GamePlayer gamePlayer : players) {
                if (gamePlayer.getUserId() == userId) {
                    return gamePlayer;
                }
            }
        }

        return null;
    }

    public int getId() {
        return id;
    }

    public GameType getGameType() {
        return gameType;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public String getName() {
        return name;
    }

    public int getTeamAmount() {
        return teamAmount;
    }

    public List<Integer> getPowerUps() {
        return powerUps;
    }

    public Map<Integer, List<GamePlayer>> getTeamPlayers() {
        return teamPlayers;
    }

    public Player getGameCreator() {
        return PlayerManager.getInstance().getPlayerById(this.gameCreator);
    }

    public int getMapId() {
        return mapId;
    }

    public int getGameCountdownSeconds() {
        return gameCountdownSeconds;
    }

    public void setGameCountdownSeconds(int gameCountdownSeconds) {
        this.gameCountdownSeconds = gameCountdownSeconds;
    }

    public int getRestartGameSeconds() {
        return restartGameSeconds;
    }

    public void setRestartGameSeconds(int restartGameSeconds) {
        this.restartGameSeconds = restartGameSeconds;
    }

    public int getGameLengthSeconds() {
        return gameLengthSeconds;
    }

    public void setGameLengthSeconds(int gameLengthSeconds) {
        this.gameLengthSeconds = gameLengthSeconds;
    }

}
