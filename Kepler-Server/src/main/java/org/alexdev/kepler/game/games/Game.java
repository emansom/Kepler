package org.alexdev.kepler.game.games;

import org.alexdev.kepler.dao.mysql.GameSpawn;
import org.alexdev.kepler.game.GameScheduler;
import org.alexdev.kepler.game.games.battleball.BattleballTile;
import org.alexdev.kepler.game.games.battleball.BattleballTileMap;
import org.alexdev.kepler.game.games.battleball.enums.BattleballTileColour;
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
import org.alexdev.kepler.util.config.GameConfiguration;
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

    private FutureRunnable preparingTimerRunnable;
    private FutureRunnable gameTimerRunnable;

    private boolean gameStarted;
    private boolean gameFinished;

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
    private void restartUsers() {
        this.gameStarted = false;
        this.gameFinished = false;

        this.preparingGameSecondsLeft = new AtomicInteger(GameManager.getInstance().getPreparingSeconds(GameType.BATTLEBALL));
        this.totalSecondsLeft = new AtomicInteger(GameManager.getInstance().getLifetimeSeconds(GameType.BATTLEBALL));

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
     * Assign spawn points to all team members
     */
    private void assignSpawnPoints() {
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
                findSpawn(flip, spawnX, spawnY, spawnRotation);

                Position spawnPosition = new Position(spawnX.get(), spawnY.get(), this.roomModel.getTileHeight(spawnX.get(), spawnY.get()), spawnRotation.get(), spawnRotation.get());

                p.getSpawnPosition().setX(spawnPosition.getX());
                p.getSpawnPosition().setY(spawnPosition.getY());
                p.getSpawnPosition().setRotation(spawnPosition.getRotation());
                p.getSpawnPosition().setZ(spawnPosition.getZ());

                p.getPlayer().getRoomUser().setPosition(spawnPosition.copy());
                p.getPlayer().getRoomUser().setWalking(false);
                p.getPlayer().getRoomUser().setNextPosition(null);

                // Don't allow anyone to spawn on this tile
                BattleballTile tile = this.getTile(spawnPosition.getX(), spawnPosition.getY());
                tile.setSpawnOccupied(true);
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
        try {
            while (this.battleballTiles[spawnX.get()][spawnY.get()].isSpawnOccupied()) {
                /*if (spawnRotation.get() == 0 || spawnRotation.get() == 4) {
                    if (flip)
                        spawnX.incrementAndGet();// -= 1;
                    else
                        spawnY.decrementAndGet();// += 1;
                } else if (spawnRotation.get() == 2 || spawnRotation.get() == 6) {
                    if (flip)
                        spawnX.incrementAndGet();// -= 1;
                    else
                        spawnY.decrementAndGet();// += 1;
                }*/
                if (spawnRotation.get() == 0) {
                    if (!flip)
                        spawnX.decrementAndGet();// -= 1;
                    else
                        spawnX.incrementAndGet();// += 1;
                }

                if (spawnRotation.get() == 2) {
                    if (!flip)
                        spawnY.incrementAndGet();// -= 1;
                    else
                        spawnY.decrementAndGet();// += 1;
                }

                if (spawnRotation.get() == 4) {
                    if (!flip)
                        spawnX.incrementAndGet();// -= 1;
                    else
                        spawnX.decrementAndGet();// += 1;
                }

                if (spawnRotation.get() == 6) {
                    if (!flip)
                        spawnY.decrementAndGet();// -= 1;
                    else
                        spawnY.incrementAndGet();// += 1;
                }
            }
            flip = (!flip);
        } catch (Exception ex) {
            flip = (!flip);
            findSpawn(flip, spawnX, spawnY, spawnRotation);
        }
    }

    /**
     * Method to start the game
     */
    public void startGame() {
        this.restartUsers();

        for (GameTeam team : this.teams.values()) {
            for (GamePlayer p : team.getActivePlayers()) {
                p.setEnteringGame(true); // Set to true so when they leave the lobby, the server knows to initialise the user when they join the arena
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
        this.gameStarted = true;

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

                // Game ends either when time runs out or there's no free tiles left to seal
                if (totalSecondsLeft.decrementAndGet() == 0 || !hasFreeTiles()) {
                    this.cancelFuture();
                    finishGame();
                }
            }
        };

        var future = GameScheduler.getInstance().getSchedulerService().scheduleAtFixedRate(this.gameTimerRunnable, 0, 1, TimeUnit.SECONDS);
        this.gameTimerRunnable.setFuture(future);

        // Send game seconds
        this.send(new GAMESTART(GameManager.getInstance().getLifetimeSeconds(GameType.BATTLEBALL)));
    }

    /**
     * Finish game
     */
    private void finishGame() {
        this.gameStarted = false;
        this.gameFinished = true;

        // Stop all players from walking when game starts if they selected a tile
        for (GameTeam team : teams.values()) {
            for (GamePlayer p : team.getActivePlayers()) {
                p.getPlayer().getRoomUser().setWalkingAllowed(false);
            }
        }

        // Send scores to everybody
        this.send(new GAMEEND(this, this.teams));

        // Restart countdown
        this.restartCountdown = new AtomicLong(GameManager.getInstance().getRestartSeconds(GameType.BATTLEBALL));

        var restartRunnable = new FutureRunnable() {
            public void run() {
                if (!canGameContinue()) {
                    this.cancelFuture();
                    return;
                }

                if (restartCountdown.decrementAndGet() == 0) {
                    this.cancelFuture();
                    triggerRestart();
                }
            }
        };

        var future = GameScheduler.getInstance().getSchedulerService().scheduleAtFixedRate(restartRunnable, 0, 1, TimeUnit.SECONDS);
        restartRunnable.setFuture(future);
    }

    /**
     * Restarts all the new players who clicked to play the next game.
     */
    private void triggerRestart() {
        List<GamePlayer> players = new ArrayList<>(); // Players who wanted to restart
        List<GamePlayer> afkPlayers = new ArrayList<>(); // Players who didn't touch any button

        for (GameTeam gameTeam : this.teams.values()) {
            for (GamePlayer p : gameTeam.getActivePlayers()) {
                if (!p.isClickedRestart()) {
                    afkPlayers.add(p);
                } else {
                    players.add(p);
                }
            }
        }

        for (var afkPlayer : afkPlayers) {
            this.leaveGame(afkPlayer);

            // TODO: Redirect to lobby instead
            if (afkPlayer.getPlayer().getRoomUser().getRoom() != null) {
                afkPlayer.getPlayer().getRoomUser().getRoom().getEntityManager().leaveRoom(afkPlayer.getPlayer(), true);
            }
        }

        this.restartUsers(players);
    }

    /**
     * Method to restart game.
     */
    private void restartUsers(List<GamePlayer> players) {
        this.send(new GAMERESET(GameManager.getInstance().getPreparingSeconds(GameType.BATTLEBALL), players));

        if (this.preparingTimerRunnable != null) {
            this.preparingTimerRunnable.cancelFuture();
        }

        if (this.gameTimerRunnable != null) {
            this.gameTimerRunnable.cancelFuture();
        }

        for (GameTeam gameTeam : this.teams.values()) {
            gameTeam.getPlayers().clear();
        }

        for (var gamePlayer : players) {
            this.movePlayer(gamePlayer, -1, gamePlayer.getTeamId());

            gamePlayer.setClickedRestart(false); // Reset whether or not they clicked restart, for next game
            gamePlayer.getPlayer().getRoomUser().setWalkingAllowed(false); // Don't allow them to walk, for next game
        }

        this.restartUsers();
        this.send(new FULLGAMESTATUS(this, false));  // Show users back at spawn positions

        // Start game after "game is about to begin"
        GameScheduler.getInstance().getSchedulerService().schedule(this::beginGame, GameManager.getInstance().getPreparingSeconds(GameType.BATTLEBALL), TimeUnit.SECONDS);
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

        return this.teams.get(teamId).getActivePlayers().size() <= maxPerTeam;
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
            if (this.gameState == GameState.WAITING) {
                this.teams.get(fromTeamId).getPlayers().remove(gamePlayer);
            }

            gamePlayer.setInGame(false); // Leaving team so they're not in game
        }

        if (toTeamId != -1) {
            if (!this.teams.get(toTeamId).getPlayers().contains(gamePlayer)) {
                this.teams.get(toTeamId).getPlayers().add(gamePlayer);
            }

            gamePlayer.setTeamId(toTeamId);
            gamePlayer.setInGame(true); // Entering team so they're in game
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

        return activeTeamCount > GameConfiguration.getInstance().getInteger("battleball.start.minimum.active.teams");
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

    /**
     * Get if the game still has free tiles to use
     * @return true, if successful
     */
    private boolean hasFreeTiles() {
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

    /**
     * Get a tile by specified coordinates
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the battleball tile, if successful
     */
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

    public boolean isGameStarted() {
        return gameStarted;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

}
