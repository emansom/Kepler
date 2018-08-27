package org.alexdev.kepler.game.games.battleball;

import org.alexdev.kepler.game.games.GameType;
import org.alexdev.kepler.game.room.mapping.RoomTileState;
import org.apache.commons.lang3.StringUtils;

public class BattleballTileMap {
    private final String heightmap;

    private int mapId;
    private GameType gameType;
    private boolean battleballTileMap[][];

    public BattleballTileMap(int mapId, GameType gameType,String tileMap) {
        this.mapId = mapId;
        this.gameType = gameType;
        this.heightmap = tileMap.replace("|", "\r");
        this.parse();
    }

    private void parse() {
        String[] lines = this.heightmap.split("\r");

        int mapSizeY = lines.length;
        int mapSizeX = lines[0].length();

        this.battleballTileMap = new boolean[mapSizeX][mapSizeY];

        for (int y = 0; y < mapSizeY; y++) {
            String line = lines[y];

            for (int x = 0; x < mapSizeX; x++) {
                String tile = Character.toString(line.charAt(x));

                if (StringUtils.isNumeric(tile)) {
                    this.battleballTileMap[x][y] = tile.equals("1");
                } else {
                    this.battleballTileMap[x][y] = false;
                }
            }
        }
    }

    public GameType getGameType() {
        return gameType;
    }

    public int getMapId() {
        return mapId;
    }

    public boolean isGameTile(int x, int y) {
        return battleballTileMap[x][y];
    }
}
