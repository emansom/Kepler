package org.alexdev.kepler.game.games.mapping;

import org.alexdev.kepler.game.games.GameType;
import org.alexdev.kepler.game.room.mapping.RoomTileState;

public class GameMap {
    private GameType gameType;
    private int mapId;
    private String heightmap;
    private String tileMap;

    private RoomTileState[][] tileStates;
    private double[][] tileHeights;

    public GameMap(GameType gameType, int mapId, String heightmap, String tileMap) {
        this.gameType = gameType;
        this.mapId = mapId;
        this.heightmap = heightmap;
        this.tileMap = tileMap;
    }
}
