package org.alexdev.kepler.game.room.mapping;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.models.RoomModel;

import java.util.ArrayList;
import java.util.List;

public class RoomMapping {
    private Room room;
    private RoomModel roomModel;
    private RoomTile roomMap[][];

    public RoomMapping(Room room) {
        this.room = room;
    }

    /**
     * Regenerate the entire collision map used for
     * furniture detection.
     */
    public void regenerateCollisionMap() {
        this.roomModel = this.room.getData().getModel();
        this.roomMap = new RoomTile[this.roomModel.getMapSizeX()][this.roomModel.getMapSizeY()];

        for (int x = 0; x < this.roomModel.getMapSizeX(); x++) {
            for (int y = 0; y < this.roomModel.getMapSizeY(); y++) {
                this.roomMap[x][y] = new RoomTile(new Position(x, y));
                this.roomMap[x][y].setTileHeight(this.roomModel.getTileHeight(x, y));
            }
        }

        synchronized (this.room.getItems()) {
            List<Item> items = new ArrayList<>(this.room.getItems());
            items.sort((item1, item2) -> Double.compare(item1.getPosition().getZ(), item2.getPosition().getZ()));

            for (Item item : items) {
                if (item.getDefinition().getBehaviour().isWallItem()) {
                    continue;
                }

                RoomTile tile = getTile(item.getPosition().getX(), item.getPosition().getY());

                if (tile == null) {
                    continue;
                }

                if (tile.getTileHeight() < item.getTotalHeight()) {
                    tile.setItemBelow(tile.getHighestItem());
                    tile.setTileHeight(item.getTotalHeight());
                    tile.setHighestItem(item);
                }
            }
        }
    }

    /**
     * Method for the pathfinder to check if the tile next to the current tile is a valid step.
     *
     * @param entity the entity walking
     * @param current the current tile
     * @param tmp the temporary tile around the current tile to check
     * @param isFinalMove if the move was final
     * @return true, if a valid step
     */
    public boolean isValidStep(Entity entity, Position current, Position tmp, boolean isFinalMove) {
        if (!this.isValidTile(entity, new Position(current.getX(), current.getY()))) {
            return false;
        }

        if (!this.isValidTile(entity, new Position(tmp.getX(), tmp.getY()))) {
            return false;
        }

        double oldHeight = this.getTile(current.getX(), current.getY()).getTileHeight();
        double newHeight = this.getTile(tmp.getX(), tmp.getY()).getTileHeight();

        if (oldHeight - 4 >= newHeight) {
            return false;
        }

        if (oldHeight + 1.5 <= newHeight) {
            return false;
        }

        return true;
    }

    /**
     * Gets if the tile was valid.
     *
     * @param entity the entity checking
     * @param position the position of the tile
     * @return true, if successful
     */
    public boolean isValidTile(Entity entity, Position position) {
        RoomTile tile = getTile(position.getX(), position.getY());

        if (tile == null) {
            return false;
        }

        if (tile.getHighestItem() != null) {
            return tile.getHighestItem().isWalkable();
        }

        return this.roomModel.getTileState(position.getX(), position.getY()) == RoomTileState.OPEN;
    }

    /**
     * Get the tile by specified coordinates.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the tile, found, else null
     */
    public RoomTile getTile(int x, int y) {
        if (x < 0 || y < 0) {
            return null;
        }

        if (x >= this.roomModel.getMapSizeX() || y >= this.roomModel.getMapSizeY()) {
            return null;
        }

        return this.roomMap[x][y];
    }
}