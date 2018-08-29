package org.alexdev.kepler.game.games;

import org.alexdev.kepler.dao.mysql.GameSpawn;
import org.alexdev.kepler.game.GameScheduler;
import org.alexdev.kepler.game.games.battleball.BattleballTile;
import org.alexdev.kepler.game.games.battleball.enums.BattleballTileColour;
import org.alexdev.kepler.game.games.battleball.BattleballTileMap;
import org.alexdev.kepler.game.games.battleball.enums.BattleballTileState;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.games.player.GameTeam;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.player.PlayerManager;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.mapping.RoomTileState;
import org.alexdev.kepler.game.room.models.RoomModel;
import org.alexdev.kepler.messages.outgoing.games.*;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.util.DateUtil;
import org.alexdev.kepler.util.schedule.FutureRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

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
    private Map<Integer, GameTeam> teams;
    private List<Player> spectators;

    private BattleballTile[][] battleballTiles;

    private AtomicInteger preparingGameSecondsLeft;
    private AtomicInteger totalSecondsLeft;
    private AtomicLong restartCountdown;

    public static final int PREPARING_GAME_SECONDS_LEFT = 2;
    public static final int RESTART_GAME_SECONDS = 3;
    public static final int GAME_LENGTH_SECONDS = 5;

    private FutureRunnable preparingTimerRunnable;
    private FutureRunnable gameTimerRunnable;

    public Game(int id, int mapId, GameType gameType, String name, int teamAmount, int gameCreator) {
        this.id = id;
        this.mapId = mapId;
        this.gameType = gameType;
        this.name = name;
        this.teamAmount = teamAmount;
        this.gameCreator = gameCreator;

        this.powerUps = new ArrayList<>();
        this.teams = new ConcurrentHashMap<>();
        this.spectators = new CopyOnWriteArrayList<>();

        for (int i = 0; i < teamAmount; i++) {
            this.teams.put(i, new GameTeam(i));
        }

        this.gameState = GameState.WAITING;
    }

    /**
     * Method to initialise the game
     */
    public void initialiseGame() {
        this.preparingGameSecondsLeft = new AtomicInteger(Game.PREPARING_GAME_SECONDS_LEFT);
        this.totalSecondsLeft = new AtomicInteger(Game.GAME_LENGTH_SECONDS);

        this.gameState = GameState.STARTED;
        this.roomModel = GameManager.getInstance().getModel(this.gameType, this.mapId);

        BattleballTileMap tileMap = GameManager.getInstance().getBattleballTileMap(this.mapId);
        this.battleballTiles = new BattleballTile[this.roomModel.getMapSizeX()][this.roomModel.getMapSizeY()];

        for (int y = 0; y < this.roomModel.getMapSizeY(); y++) {
            for (int x = 0; x < this.roomModel.getMapSizeX(); x++) {
                RoomTileState tileState = this.roomModel.getTileState(x, y);
                BattleballTile tile = new BattleballTile(new Position(x, y));

                this.battleballTiles[x][y] = tile;
                tile.setState(BattleballTileState.DEFAULT);

                if (tileState == RoomTileState.CLOSED) {
                    tile.setColour(BattleballTileColour.DISABLED);
                    continue;
                }

                if (!tileMap.isGameTile(x, y)) {
                    tile.setColour(BattleballTileColour.DISABLED);
                    continue;
                }

                tile.setColour(BattleballTileColour.DEFAULT);
            }
        }

        if (this.room == null) {
            this.room = new Room();
            this.room.getData().fill(this.id, "Battleball Arena", "");
            this.room.setRoomModel(roomModel);
        }

        this.assignSpawnPoints();
    }

    /**
     * Method to restart game.
     *
     * @return the list of players who are playing in the restarted game
     */
    public List<GamePlayer> resetGame() {
        List<GamePlayer> newPlayers = new ArrayList<>();

        if (this.preparingTimerRunnable != null) {
            this.preparingTimerRunnable.cancelFuture();
        }

        if (this.gameTimerRunnable != null) {
            this.gameTimerRunnable.cancelFuture();
        }

        for (GameTeam gameTeam : this.teams.values()) {
            for (GamePlayer gamePlayer : gameTeam.getActivePlayers()) {
                gamePlayer.setScore(0);
                gamePlayer.getPlayer().getRoomUser().setWalkingAllowed(false);
                gamePlayer.setClickedRestart(false);

                newPlayers.add(gamePlayer);
            }
        }

        this.initialiseGame();
        this.send(new FULLGAMESTATUS(this, false));  // Show users back at teleporting positions

        // Show coloured tiles at the start of the time, needs to be on delay or won't be seen
        GameScheduler.getInstance().getSchedulerService().schedule(() -> {
            this.send(new FULLGAMESTATUS(this, false));
        }, 200, TimeUnit.MILLISECONDS);

        // Start game after "game is about to begin"
        GameScheduler.getInstance().getSchedulerService().schedule(() -> {
            this.beginGame();
            this.room.getTaskManager().startTasks();
        }, Game.PREPARING_GAME_SECONDS_LEFT, TimeUnit.SECONDS);=

        return newPlayers;
    }

    /**
     * Method to start the game
     */
    public void startGame() {
        this.initialiseGame();

        for (GameTeam team : this.teams.values()) {
            for (GamePlayer p : team.getActivePlayers()) {
                p.setEnteringGame(true);
            }
        }

        this.send(new GAMELOCATION());

        // Preparing game seconds countdown
        this.preparingTimerRunnable = new FutureRunnable() {
            public void run() {
                if (!canGameContinue()) {
                    this.cancelFuture();
                    return;
                }

                if (preparingGameSecondsLeft.getAndDecrement() == 0) {
                    this.cancelFuture();
                    beginGame();
                }
            }
        };

        var future = GameScheduler.getInstance().getSchedulerService().scheduleAtFixedRate(this.preparingTimerRunnable, 0, 1, TimeUnit.SECONDS);
        this.preparingTimerRunnable.setFuture(future);
    }

    /**
     * Method for when the game begins after the initial preparing game seconds timer
     */
    private void beginGame() {
        // Stop all players from walking when game starts if they selected a tile
        for (GameTeam team : teams.values()) {
            for (GamePlayer p : team.getActivePlayers()) {
                p.getPlayer().getRoomUser().setWalkingAllowed(true);
            }
        }

        // Game seconds counter
        this.gameTimerRunnable = new FutureRunnable() {
            public void run() {
                if (!canGameContinue()) {
                    this.cancelFuture();
                    return;
                }

                if (totalSecondsLeft.decrementAndGet() == 0 || !hasFreeTiles()) {
                    this.cancelFuture();
                    finishGame();
                }
            }
        };

        var future = GameScheduler.getInstance().getSchedulerService().scheduleAtFixedRate(this.gameTimerRunnable, 0, 1, TimeUnit.SECONDS);
        this.gameTimerRunnable.setFuture(future);

        // Send game seconds
        this.send(new GAMESTART(Game.GAME_LENGTH_SECONDS));
    }

    /**
     * Finish game
     */
    private void finishGame() {
        // Kill GameTask, stops people interacting, walking, etc
        if (this.room.getTaskManager().hasTask("GameTask")) {
            this.room.getTaskManager().cancelTask("GameTask");
        }

        // Restart countdown
        this.restartCountdown = new AtomicLong(DateUtil.getCurrentTimeSeconds() + Game.RESTART_GAME_SECONDS);

        // Send scores to everybody
        this.send(new GAMEEND(this, this.teams));
    }

    /**
     * Assign spawn points to all team members
     */
    public void assignSpawnPoints() {
        for (GameTeam team : this.teams.values()) {
            GameSpawn gameSpawn = GameManager.getInstance().getGameSpawn(this.gameType, this.mapId, team.getId());

            if (gameSpawn == null) {
                continue;
            }

            AtomicInteger spawnX = new AtomicInteger(gameSpawn.getX());
            AtomicInteger spawnY = new AtomicInteger(gameSpawn.getY());
            AtomicInteger spawnRotation = new AtomicInteger(gameSpawn.getZ());

            boolean flip = false;

            for (GamePlayer p : team.getPlayers()) {
                try {
                    findSpawn(flip, spawnX, spawnY, spawnRotation);
                    flip = (!flip);
                } catch (Exception ex) {
                    flip = (!flip);
                }

                p.getSpawnPosition().setX(spawnX.get());
                p.getSpawnPosition().setY(spawnY.get());
                p.getSpawnPosition().setRotation(spawnRotation.get());
                p.getSpawnPosition().setZ(this.roomModel.getTileHeight(spawnX.get(), spawnY.get()));

                p.getPlayer().getRoomUser().setPosition(p.getSpawnPosition().copy());
                p.getPlayer().getRoomUser().setInstanceId(p.getPlayer().getDetails().getId());
                p.getPlayer().getRoomUser().setWalking(false);
                p.getPlayer().getRoomUser().setNextPosition(null);

                BattleballTile tile = this.getTile(p.getSpawnPosition().getX(), p.getSpawnPosition().getY());

                // Don't allow anyone to spawn on this tile
                tile.setSpawnOccupied(true);

                // Set first interaction on spawn tile, like official Habbo
                tile.setState(BattleballTileState.TOUCHED);
                tile.setColour(BattleballTileColour.getColourById(team.getId()));
             }
        }
    }

    /**
     * Find a spawn with given coordinates.
     *
     * @param flip whether the integers should get incremented or decremented
     * @param spawnX the x coord
     * @param spawnY the y coord
     * @param spawnRotation the spawn rotation
     */
    private void findSpawn(boolean flip, AtomicInteger spawnX, AtomicInteger spawnY, AtomicInteger spawnRotation) {
        while (this.getTile(spawnX.get(), spawnY.get()).isSpawnOccupied()) {
            if (spawnRotation.get() == 0 || spawnRotation.get() == 2) {
                if (flip)
                    spawnX.decrementAndGet();// -= 1;
                else
                    spawnX.incrementAndGet();// += 1;
            } else if (spawnRotation.get() == 4 || spawnRotation.get() == 6) {
                if (flip)
                    spawnY.decrementAndGet();// -= 1;
                else
                    spawnY.incrementAndGet();// += 1;
            }
        }
    }

    /**
     * Make the player leave the game, abort the game if the owner leaves
     *
     * @param gamePlayer the game player to leave
     */
    public void leaveGame(GamePlayer gamePlayer) {
        gamePlayer.getPlayer().getRoomUser().setGamePlayer(null);
        gamePlayer.getPlayer().send(new GAMEDELETED());

        this.movePlayer(gamePlayer, gamePlayer.getTeamId(), -1);

        if (!this.canGameContinue()) {
            GameManager.getInstance().getGames().remove(this);
        }
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

        return this.teams.get(teamId).getActivePlayers().size() < maxPerTeam;
    }

    /**
     * Moves a player from one team to another team.
     *
     * @param gamePlayer the player to move
     * @param fromTeamId the team to move from, -1 if just to add to team
     * @param toTeamId the team to move to, -1 if just removing user from team
     */
    public void movePlayer(GamePlayer gamePlayer, int fromTeamId, int toTeamId) {
        if (fromTeamId != -1) {
            this.teams.get(fromTeamId).getPlayers().remove(gamePlayer);
        }

        if (toTeamId != -1) {
            if (!this.teams.get(toTeamId).getPlayers().contains(gamePlayer)) {
                this.teams.get(toTeamId).getPlayers().add(gamePlayer);
            }

            gamePlayer.setTeamId(toTeamId);
            gamePlayer.setInGame(true);
        } else {
            if (this.gameState == GameState.WAITING) {
                this.teams.get(gamePlayer.getTeamId()).getPlayers().remove(gamePlayer);
            } else {
                gamePlayer.setInGame(false); // Don't remove from team, just show they're no longer in game, for "0" score at the end.
            }

            gamePlayer.getPlayer().getRoomUser().setGamePlayer(null);
        }

        this.send(new GAMEINSTANCE(this));
    }

    /**
     * Send a packet to all members in game
     *
     * @param composer the composer to send
     */
    public void send(MessageComposer composer) {
        for (GameTeam team : this.teams.values()) {
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
            if (this.teams.get(i).getActivePlayers().size() > 0) {
                activeTeamCount++;
            }
        }

        return activeTeamCount > 0;
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
            if (this.teams.get(i).getActivePlayers().size() > 0) {
                activeTeamCount++;
            }
        }

        return activeTeamCount > 0;
    }

    public boolean hasFreeTiles() {
        for (int y = 0; y < this.roomModel.getMapSizeY(); y++) {
            for (int x = 0; x < this.roomModel.getMapSizeX(); x++) {
                BattleballTile tile = this.getTile(x, y);

                if (tile == null || tile.getColour() == BattleballTileColour.DISABLED) {
                    continue;
                }

                if (tile.getState() != BattleballTileState.SEALED) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Get the game player instance by id
     * @param userId the id to get the player by
     * @return the game player instance, else if null
     */
    public GamePlayer getGamePlayer(int userId) {
        for (GameTeam team : this.teams.values()) {
            for (GamePlayer gamePlayer : team.getPlayers()) {
                if (gamePlayer.getUserId() == userId) {
                    return gamePlayer;
                }
            }
        }

        return null;
    }

    public BattleballTile getTile(int x, int y) {
        if (x < 0 || y < 0) {
            return null;
        }

        if (x >= this.roomModel.getMapSizeX() || y >= roomModel.getMapSizeY()) {
            return null;
        }

        return this.battleballTiles[x][y];
    }

    /**
     * Get the list of specators, the people currently watching the game
     *
     * @return the list of spectators
     */
    public List<Player> getSpectators() {
        return spectators;
    }

    /**
     * Get the game id
     *
     * @return the game id
     */
    public int getId() {
        return id;
    }

    public GameType getGameType() {
        return gameType;
    }

    public GameState getGameState() {
        return gameState;
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

    public Map<Integer, GameTeam> getTeams() {
        return teams;
    }

    public Player getGameCreator() {
        return PlayerManager.getInstance().getPlayerById(this.gameCreator);
    }

    public int getMapId() {
        return mapId;
    }

    public AtomicInteger getPreparingGameSecondsLeft() {
        return preparingGameSecondsLeft;
    }

    public AtomicInteger getTotalSecondsLeft() {
        return totalSecondsLeft;
    }

    public RoomModel getRoomModel() {
        return roomModel;
    }

    public Room getRoom() {
        return room;
    }

    public AtomicLong getRestartCountdown() {
        return restartCountdown;
    }
}
