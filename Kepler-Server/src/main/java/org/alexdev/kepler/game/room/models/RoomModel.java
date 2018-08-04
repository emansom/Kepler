package org.alexdev.kepler.game.room.models;

import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.ItemPublicParser;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.room.mapping.RoomTileState;
import org.alexdev.kepler.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class RoomModel {
    private String modelId;
    private String modelName;
    private int doorX;
    private int doorY;
    private double doorZ;
    private int doorRotation;
    private int mapSizeX;
    private int mapSizeY;
    private String heightmap;
    private List<Item> publicItems;

    private RoomTileState[][] tileStates;
    private double[][] tileHeights;

    public RoomModel(String modelId, String modelName, int doorX, int doorY, double doorZ, int doorRotation, String heightmap) {
        this.modelId = modelId;
        this.modelName = modelName;
        this.doorX = doorX;
        this.doorY = doorY;
        this.doorZ = doorZ;
        this.doorRotation = doorRotation;
        this.heightmap = heightmap.replace("|", "\r");
        this.publicItems = ItemPublicParser.getPublicItems(this.modelId);
    }

    /**
     * Parse heightmap, add invalid tiles and the tile heights used
     * for walking, stairs, etc.
     */
    public void parse() {
        String[] lines = this.heightmap.split("\r");

        this.mapSizeX = lines.length;
        this.mapSizeY = lines[0].length();

        this.tileStates = new RoomTileState[this.mapSizeX][this.mapSizeY];
        this.tileHeights = new double[this.mapSizeX][this.mapSizeY];

        StringBuilder temporaryHeightmap = new StringBuilder();

        for (int x = 0; x < this.mapSizeX; x++) {
            String line = lines[x];

            for (int y = 0; y < this.mapSizeY; y++) {
                String tile = Character.toString(line.charAt(y));

                if (StringUtil.isNumber(tile)) {
                    this.tileStates[x][y] = RoomTileState.OPEN;
                    this.tileHeights[x][y] = Double.parseDouble(tile);
                } else {
                    this.tileStates[x][y] = RoomTileState.CLOSED;
                    this.tileHeights[x][y] = 0;
                }

                if (x == this.doorX && y == this.doorY) {
                    this.tileStates[x][y] = RoomTileState.OPEN;
                    this.tileHeights[x][y] = this.doorZ;
                }

                temporaryHeightmap.append(tile);
            }

            temporaryHeightmap.append("\r");
        }

        this.heightmap = temporaryHeightmap.toString();

    }

    /**
     * Get the tile state by given coordinates. This
     * doesn't include room furniture.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the room state
     */
    public RoomTileState getTileState(int x, int y) {
        if (x < 0 || y < 0) {
            return RoomTileState.CLOSED;
        }

        if (x >= this.mapSizeX || y >= this.mapSizeY) {
            return RoomTileState.CLOSED;
        }

        return tileStates[x][y];
    }

    /**
     * Get the tile height, this doesn't include
     * furniture heights.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the room height
     */
    public double getTileHeight(int x, int y) {
        if (x < 0 || y < 0) {
            return 0;
        }

        if (x >= this.mapSizeX || y >= this.mapSizeY) {
            return 0;
        }

        return tileHeights[x][y];
    }


    public String getId() {
        return modelId;
    }

    public String getName() {
        return modelName;
    }

    public Position getDoorLocation() {
        return new Position(this.doorX, this.doorY, this.doorZ, this.doorRotation, this.doorRotation);
    }

    public int getMapSizeX() {
        return mapSizeX;
    }

    public int getMapSizeY() {
        return mapSizeY;
    }

    public String getHeightmap() {
        return heightmap;
    }

    public List<Item> getPublicItems() {
        return publicItems;
    }
}