package org.alexdev.kepler.game.games.battleball;

import org.alexdev.kepler.dao.mysql.GameSpawn;
import org.alexdev.kepler.game.games.*;
import org.alexdev.kepler.game.games.battleball.enums.BattleballPlayerState;
import org.alexdev.kepler.game.games.battleball.enums.BattleballPowerType;
import org.alexdev.kepler.game.games.battleball.events.DespawnObjectEvent;
import org.alexdev.kepler.game.games.battleball.events.PowerUpSpawnEvent;
import org.alexdev.kepler.game.games.battleball.objects.PlayerObject;
import org.alexdev.kepler.game.games.enums.GameType;
import org.alexdev.kepler.game.games.battleball.enums.BattleballColourType;
import org.alexdev.kepler.game.games.battleball.enums.BattleballTileType;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.games.player.GameTeam;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.mapping.RoomTileState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BattleballGame extends Game {
    private BattleballTile[][] battleballTiles;

    private List<Integer> allowedPowerUps;
    private List<BattleballPowerUp> activePowers;

    private Map<GamePlayer, List<BattleballPowerUp>> storedPowers;

    private BlockingQueue<BattleballTile> updateTilesQueue;
    private BlockingQueue<BattleballTile> fillTilesQueue;

    private AtomicInteger timeUntilNextPower;

    public BattleballGame(int id, int mapId, GameType gameType, String name, int teamAmount, Player gameCreator, List<Integer> allowedPowerUps) {
        super(id, mapId, gameType, name, teamAmount, gameCreator);

        this.allowedPowerUps = allowedPowerUps;
        this.timeUntilNextPower = new AtomicInteger(0);

        if (this.allowedPowerUps.size() >= 2) {
            this.allowedPowerUps.add(BattleballPowerType.QUESTION_MARK.getPowerUpId());
        }

        this.activePowers = new CopyOnWriteArrayList<>();
        this.storedPowers = new ConcurrentHashMap<>();

        this.updateTilesQueue = new LinkedBlockingQueue<>();
        this.fillTilesQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void gamePrepare() {
        //this.activePowers.clear();
        //this.storedPowers.clear();
        //this.timeUntilNextPower = new AtomicInteger(0);
    }

    @Override
    public void gamePrepareTick() {
        this.activePowers.clear();
        this.storedPowers.clear();
        this.timeUntilNextPower = new AtomicInteger(0);
    }

    @Override
    public void gameTick() {
        this.checkExpirePower();
        this.checkSpawnPower();
        this.checkStoredExpirePower();
    }

    private void checkExpirePower() {
        if (this.allowedPowerUps.isEmpty() || this.activePowers.isEmpty() || (this.getMapId() == 5)) {
            return;
        }

        BattleballPowerUp powerUp = this.activePowers.get(0);

        if (expirePower(powerUp)) {
            this.getObjects().remove(powerUp.getObject());
            this.activePowers.clear();
        }
    }

    private void checkStoredExpirePower() {
        for (var powers : this.storedPowers.values()) {
            List<BattleballPowerUp> expiredPowers = new ArrayList<>();

            for (BattleballPowerUp power : powers) {
                if (expirePower(power)) {
                    expiredPowers.add(power);
                }
            }

            powers.removeAll(expiredPowers);
        }
    }

    private boolean expirePower(BattleballPowerUp powerUp) {
        if (powerUp.getTimeToDespawn().get() > 0) {
            if (powerUp.getTimeToDespawn().decrementAndGet() != 0) {
                return false;
            }
        }

        this.getEventsQueue().add(new DespawnObjectEvent(powerUp.getId()));
        return true;
    }

    private void checkSpawnPower() {
        if (this.allowedPowerUps.isEmpty() || (this.getMapId() == 5)) {
            return;
        }

        if (this.timeUntilNextPower.get() > 0) {
            if (this.timeUntilNextPower.decrementAndGet() != 0) {
                return;
            }
        }

        if (this.activePowers.size() > 0) { // There's already an active power so don't spawn another one
            return;
        }

        BattleballPowerUp powerUp = new BattleballPowerUp(this.createObjectId(), this, this.getRandomTile());
        this.activePowers.add(powerUp);

        this.updateTimeUntilNextPower();

        this.getEventsQueue().add(new PowerUpSpawnEvent(powerUp));
        this.getObjects().add(powerUp.getObject());
    }

    private void updateTimeUntilNextPower() {
        this.timeUntilNextPower = new AtomicInteger(ThreadLocalRandom.current().nextInt(10, 30));
    }

    @Override
    public void buildMap() {
        BattleballTileMap tileMap = GameManager.getInstance().getBattleballTileMap(this.getMapId());
        this.battleballTiles = new BattleballTile[this.getRoomModel().getMapSizeX()][this.getRoomModel().getMapSizeY()];

        for (int y = 0; y < this.getRoomModel().getMapSizeY(); y++) {
            for (int x = 0; x < this.getRoomModel().getMapSizeX(); x++) {
                RoomTileState tileState = this.getRoomModel().getTileState(x, y);
                BattleballTile tile = new BattleballTile(new Position(x, y, this.getRoomModel().getTileHeight(x, y)));

                this.battleballTiles[x][y] = tile;
                tile.setState(BattleballTileType.DEFAULT);

                if (tileState == RoomTileState.CLOSED) {
                    tile.setColour(BattleballColourType.DISABLED);
                    continue;
                }

                if (!tileMap.isGameTile(x, y)) {
                    tile.setColour(BattleballColourType.DISABLED);
                    continue;
                }

                tile.setColour(BattleballColourType.DEFAULT);
            }
        }
    }

    /**
     * Assign spawn points to all team members
     */
    @Override
    public void assignSpawnPoints() {
        for (GameTeam team : this.getTeams().values()) {
            GameSpawn gameSpawn = GameManager.getInstance().getGameSpawn(this.getGameType(), this.getMapId(), team.getId());

            if (gameSpawn == null) {
                continue;
            }

            AtomicInteger spawnX = new AtomicInteger(gameSpawn.getX());
            AtomicInteger spawnY = new AtomicInteger(gameSpawn.getY());
            AtomicInteger spawnRotation = new AtomicInteger(gameSpawn.getZ());

            boolean flip = false;

            for (GamePlayer p : team.getPlayers()) {
                findSpawn(flip, spawnX, spawnY, spawnRotation);
                p.setPlayerState(BattleballPlayerState.NORMAL);
                p.setHarlequinPlayer(null);
                p.setGameObject(new PlayerObject(p));
                p.setObjectId(this.createObjectId());

                this.getObjects().add(p.getGameObject());

                Position spawnPosition = new Position(spawnX.get(), spawnY.get(), this.getRoomModel().getTileHeight(spawnX.get(), spawnY.get()), spawnRotation.get(), spawnRotation.get());
                p.getSpawnPosition().setX(spawnPosition.getX());
                p.getSpawnPosition().setY(spawnPosition.getY());
                p.getSpawnPosition().setRotation(spawnPosition.getRotation());
                p.getSpawnPosition().setZ(spawnPosition.getZ());

                p.getPlayer().getRoomUser().setWalking(false);
                p.getPlayer().getRoomUser().setNextPosition(null);

                // Don't allow anyone to spawn on this tile
                BattleballTile tile = (BattleballTile) this.getTile(spawnPosition.getX(), spawnPosition.getY());
                tile.setSpawnOccupied(true);

                if (tile.getColour() != BattleballColourType.DISABLED) {
                    // Set spawn colour
                    tile.setColour(BattleballColourType.getColourById(team.getId()));

                    if (this.getMapId() == 5) {
                        tile.setState(BattleballTileType.TOUCHED);
                    } else {
                        tile.setState(BattleballTileType.CLICKED);
                    }
                }
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
     * Get if the game still has free tiles to use.
     *
     * @return true, if successful
     */
    @Override
    public boolean canTimerContinue() {
        for (int y = 0; y < this.getRoomModel().getMapSizeY(); y++) {
            for (int x = 0; x < this.getRoomModel().getMapSizeX(); x++) {
                BattleballTile tile = (BattleballTile) this.getTile(x, y);

                if (tile == null || tile.getColour() == BattleballColourType.DISABLED) {
                    continue;
                }

                if (tile.getState() != BattleballTileType.SEALED) {
                    return true;
                }
            }
        }

        return false;
    }

    public BattleballTile getRandomTile() {
        int mapSizeX = this.getRoomModel().getMapSizeX();
        int mapSizeY = this.getRoomModel().getMapSizeY();

        int x = ThreadLocalRandom.current().nextInt(0, mapSizeX);
        int y = ThreadLocalRandom.current().nextInt(0, mapSizeY);

        BattleballTile battleballTile = (BattleballTile) this.getTile(x, y);

        if (battleballTile == null || battleballTile.getColour() == BattleballColourType.DISABLED) {
            return getRandomTile();
        }

        if (this.getRoom().getMapping().getTile(x, y).getEntities().size() > 0) {
            return getRandomTile();
        }

        return battleballTile;
    }

    @Override
    public GameTile[][] getTileMap() {
        return battleballTiles;
    }

    /**
     * Get the power ups allowed for this match.
     *
     * @return the power ups allowed
     */
    public List<Integer> getAllowedPowerUps() {
        return this.allowedPowerUps;
    }

    /**
     * Get the current active powers on the board
     *
     * @return the list of active powers
     */
    public List<BattleballPowerUp> getActivePowers() {
        return activePowers;
    }

    /**
     * Get the list of powers stored by player
     *
     * @return the list of players currently held by player
     */
    public Map<GamePlayer, List<BattleballPowerUp>> getStoredPowers() {
        return storedPowers;
    }

    /**
     * Get the queue for tiles to be updated, used by power ups
     *
     * @return the queue for updating tiles
     */
    public BlockingQueue<BattleballTile> getUpdateTilesQueue() {
        return updateTilesQueue;
    }

    /**
     * Get the fill tiles queue, used by power ups
     *
     * @return the fill tiles queue
     */
    public BlockingQueue<BattleballTile> getFillTilesQueue() {
        return fillTilesQueue;
    }
}
