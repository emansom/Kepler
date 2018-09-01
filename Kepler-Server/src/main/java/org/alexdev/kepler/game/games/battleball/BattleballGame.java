package org.alexdev.kepler.game.games.battleball;

import org.alexdev.kepler.dao.mysql.GameSpawn;
import org.alexdev.kepler.game.games.Game;
import org.alexdev.kepler.game.games.GameManager;
import org.alexdev.kepler.game.games.GameTile;
import org.alexdev.kepler.game.games.enums.GameType;
import org.alexdev.kepler.game.games.battleball.enums.BattleballTileColour;
import org.alexdev.kepler.game.games.battleball.enums.BattleballTileState;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.games.player.GameTeam;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.mapping.RoomTileState;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BattleballGame extends Game {
    private BattleballTile[][] battleballTiles;
    private List<Integer> powerUps;

    public BattleballGame(int id, int mapId, GameType gameType, String name, int teamAmount, Player gameCreator) {
        super(id, mapId, gameType, name, teamAmount, gameCreator);
        this.powerUps = new ArrayList<>();
    }

    @Override
    public void buildMap() {
        BattleballTileMap tileMap = GameManager.getInstance().getBattleballTileMap(this.getMapId());
        this.battleballTiles = new BattleballTile[this.getRoomModel().getMapSizeX()][this.getRoomModel().getMapSizeY()];

        for (int y = 0; y < this.getRoomModel().getMapSizeY(); y++) {
            for (int x = 0; x < this.getRoomModel().getMapSizeX(); x++) {
                RoomTileState tileState = this.getRoomModel().getTileState(x, y);
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

                Position spawnPosition = new Position(spawnX.get(), spawnY.get(), this.getRoomModel().getTileHeight(spawnX.get(), spawnY.get()), spawnRotation.get(), spawnRotation.get());

                p.getSpawnPosition().setX(spawnPosition.getX());
                p.getSpawnPosition().setY(spawnPosition.getY());
                p.getSpawnPosition().setRotation(spawnPosition.getRotation());
                p.getSpawnPosition().setZ(spawnPosition.getZ());

                p.getPlayer().getRoomUser().setPosition(spawnPosition.copy());
                p.getPlayer().getRoomUser().setWalking(false);
                p.getPlayer().getRoomUser().setNextPosition(null);

                // Don't allow anyone to spawn on this tile
                BattleballTile tile = (BattleballTile) this.getTile(spawnPosition.getX(), spawnPosition.getY());
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
     * Get if the game still has free tiles to use.
     *
     * @return true, if successful
     */
    @Override
    public boolean canTimerContinue() {
        for (int y = 0; y < this.getRoomModel().getMapSizeY(); y++) {
            for (int x = 0; x < this.getRoomModel().getMapSizeX(); x++) {
                BattleballTile tile = (BattleballTile) this.getTile(x, y);

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

    @Override
    public GameTile[][] getTileMap() {
        return battleballTiles;
    }

    /**
     * Get the power ups allowed for this match.
     *
     * @return the power ups allowed
     */
    public List<Integer> getPowerUps() {
        return powerUps;
    }
}
