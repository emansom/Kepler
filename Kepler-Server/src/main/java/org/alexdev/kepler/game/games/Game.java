package org.alexdev.kepler.game.games;

import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.games.player.GameTeam;
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
    private Map<Integer, GameTeam> teamPlayers;
    private List<Player> spectators;

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
        this.spectators = new CopyOnWriteArrayList<>();

        for (int i = 0; i < teamAmount; i++) {
            this.teamPlayers.put(i, new GameTeam(i));
        }

        this.gameState = GameState.WAITING;
    }

    /**
     * Get if there is enough space for this user to switch to the team
     *
     * @param teamId the team id to check for
     * @return true, if successful
     */
    public boolean canSwitchTeam(int teamId) {
        int maxPerTeam = 0;

        if (this.teamAmount == 2) {
            maxPerTeam = 5;
        }
        else if (this.teamAmount == 3) {
            maxPerTeam = 3;
        }
        else if (this.teamAmount == 4) {
            maxPerTeam = 2;
        }

        return this.teamPlayers.get(teamId).getPlayerList().size() <= maxPerTeam;
    }

    /**
     * Moves a player from one team to another team.
     *
     * @param player the player to move
     * @param fromTeamId the team to move from, -1 if just to add to team
     * @param toTeamId the team to move to, -1 if just removing user from team
     */
    public void movePlayer(Player player, int fromTeamId, int toTeamId) {
        GamePlayer gamePlayer = player.getRoomUser().getGamePlayer();

        if (fromTeamId != -1) {
            this.teamPlayers.get(fromTeamId).getPlayerList().remove(gamePlayer);
        }

        if (toTeamId != -1) {
            if (!this.teamPlayers.get(toTeamId).getPlayerList().contains(gamePlayer)) {
                this.teamPlayers.get(toTeamId).getPlayerList().add(gamePlayer);
            }

            gamePlayer.setTeamId(toTeamId);
        } else {
            this.teamPlayers.get(gamePlayer.getTeamId()).getPlayerList().remove(gamePlayer);
        }

        this.send(new GAMEINSTANCE(this));
    }

    /**
     * Send a packet to all members in game
     *
     * @param composer the composer to send
     */
    public void send(MessageComposer composer) {
        for (GameTeam team : this.teamPlayers.values()) {
            for (GamePlayer gamePlayer : team.getPlayerList()) {
                gamePlayer.getPlayer().send(composer);
            }
        }

        for (Player player : this.spectators) {
            player.send(composer);
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
            if (this.teamPlayers.get(i).getPlayerList().size() > 0) {
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
        for (GameTeam team : this.teamPlayers.values()) {
            for (GamePlayer gamePlayer : team.getPlayerList()) {
                if (gamePlayer.getUserId() == userId) {
                    return gamePlayer;
                }
            }
        }

        return null;
    }

    public List<Player> getSpectators() {
        return spectators;
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

    public Map<Integer, GameTeam> getTeamPlayers() {
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
