package org.alexdev.kepler.game.games;

import org.alexdev.kepler.game.games.battleball.BattleballTileColour;
import org.alexdev.kepler.game.games.battleball.BattleballTileState;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.games.player.GameTeam;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.player.PlayerManager;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.mapping.RoomTileState;
import org.alexdev.kepler.game.room.models.RoomModel;
import org.alexdev.kepler.messages.outgoing.games.GAMEDELETED;
import org.alexdev.kepler.messages.outgoing.games.GAMEINSTANCE;
import org.alexdev.kepler.messages.outgoing.games.GAMELOCATION;
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

    private Room room;
    private RoomModel roomModel;

    private String name;

    private List<Integer> powerUps;
    private Map<Integer, GameTeam> teamPlayers;
    private List<Player> spectators;

    private BattleballTileState[][] battleballTileStates;
    private BattleballTileColour[][] battleballTileColours;

    private int preparingGameSecondsLeft = 15;

    public static final int GAME_COUNTDOWN_SECONDS = 15;
    public static final int RESTART_GAME_SECONDS = 1200;
    public static final int GAME_LENGTH_SECONDS = 180;

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
     * Method to start the game
     */
    public void startGame() {
        this.gameState = GameState.STARTED;

        this.roomModel = GameManager.getInstance().getModel(this.gameType, this.mapId);
        this.battleballTileColours = new BattleballTileColour[roomModel.getMapSizeX()][roomModel.getMapSizeY()];
        this.battleballTileStates = new BattleballTileState[roomModel.getMapSizeX()][roomModel.getMapSizeY()];

        for (int y = 0; y < roomModel.getMapSizeY(); y++) {
            for (int x = 0; x < roomModel.getMapSizeX(); x++) {
                RoomTileState tileState = roomModel.getTileState(x, y);

                this.battleballTileStates[x][y] = BattleballTileState.DEFAULT;

                if (tileState == RoomTileState.CLOSED) {
                    this.battleballTileColours[x][y] = BattleballTileColour.DISABLED;
                } else {
                    this.battleballTileColours[x][y] = BattleballTileColour.DEFAULT;
                }
            }
        }

        this.room = new Room();
        this.room.getData().fill(this.id, "Battleball Arena", "");
        this.room.setRoomModel(roomModel);

        for (GameTeam team : this.teamPlayers.values()) {
            for (GamePlayer p : team.getPlayers()) {
                p.setEnteringGame(true);
            }
        }

        this.send(new GAMELOCATION());
    }

    /**
     * Make the player leave the game, abort the game if the owner leaves
     *
     * @param gamePlayer the game player to leave
     */
    public void leaveGame(GamePlayer gamePlayer, boolean hotelView) {
        /*if (this.gameCreator == gamePlayer.getUserId()) {
            this.abort(gamePlayer, hotelView);
            return;
        } else {*/

        this.send(new GAMEDELETED());
        this.movePlayer(gamePlayer.getPlayer(), gamePlayer.getTeamId(), -1);

        if (this.gameState == GameState.STARTED) {
            gamePlayer.getGame().getRoom().getEntityManager().leaveRoom(gamePlayer.getPlayer(), hotelView);
        }

        gamePlayer.getPlayer().getRoomUser().setGamePlayer(null);

        if (!this.canGameContinue()) {
            GameManager.getInstance().getGames().remove(this);
        }

        /*}


        // Not enough players left, after game started, delete the game
        if (this.gameState == GameState.STARTED && !this.canGameStart()) {
            this.abort(null, true);
        }*/
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

        return this.teamPlayers.get(teamId).getActivePlayers().size() < maxPerTeam;
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
            this.teamPlayers.get(fromTeamId).getPlayers().remove(gamePlayer);
        }

        if (toTeamId != -1) {
            if (!this.teamPlayers.get(toTeamId).getPlayers().contains(gamePlayer)) {
                this.teamPlayers.get(toTeamId).getPlayers().add(gamePlayer);
            }

            gamePlayer.setTeamId(toTeamId);
            gamePlayer.setInGame(true);
        } else {
            if (this.gameState == GameState.WAITING) {
                this.teamPlayers.get(gamePlayer.getTeamId()).getPlayers().remove(gamePlayer);
            } else {
                gamePlayer.setInGame(false);
            }

            player.getRoomUser().setGamePlayer(null);
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
            for (GamePlayer gamePlayer : team.getActivePlayers()) {
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
            if (this.teamPlayers.get(i).getActivePlayers().size() > 0) {
                activeTeamCount++;
            }
        }

        return activeTeamCount > 1;
    }

    /**
     * Gets if there's enough players in different teams for the game to continue
     * (minimum of 1 players)
     *
     * @return true, if successful
     */
    public boolean canGameContinue() {
        int activeTeamCount = 0;

        for (int i = 0; i < this.teamAmount; i++) {
            if (this.teamPlayers.get(i).getActivePlayers().size() > 0) {
                activeTeamCount++;
            }
        }

        return activeTeamCount > 0;
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
            for (GamePlayer gamePlayer : team.getPlayers()) {
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

    public BattleballTileState[][] getTileStates() {
        return battleballTileStates;
    }

    public BattleballTileColour[][] getTileColours() {
        return battleballTileColours;
    }

    public int getPreparingGameSecondsLeft() {
        return preparingGameSecondsLeft;
    }

    public RoomModel getRoomModel() {
        return roomModel;
    }

    public Room getRoom() {
        return room;
    }
}
