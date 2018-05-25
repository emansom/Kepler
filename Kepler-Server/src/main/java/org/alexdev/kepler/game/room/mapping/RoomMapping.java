package org.alexdev.kepler.game.room.mapping;

import org.alexdev.kepler.dao.mysql.ItemDao;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityStatus;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.pathfinder.AffectedTile;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.models.RoomModel;
import org.alexdev.kepler.game.room.public_rooms.PoolHandler;
import org.alexdev.kepler.messages.incoming.rooms.items.REMOVE_FLOORITEM;
import org.alexdev.kepler.messages.outgoing.rooms.items.MOVE_FLOORITEM;
import org.alexdev.kepler.messages.outgoing.rooms.items.PLACE_FLOORITEM;
import org.alexdev.kepler.messages.outgoing.rooms.items.PLACE_WALLITEM;
import org.alexdev.kepler.messages.outgoing.rooms.items.REMOVE_WALLITEM;

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
                this.roomMap[x][y] = new RoomTile(this.room, new Position(x, y));
                this.roomMap[x][y].setTileHeight(this.roomModel.getTileHeight(x, y));
            }
        }

        for (Entity entity : this.room.getEntities()) {
            this.getTile(entity.getRoomUser().getPosition().getX(), entity.getRoomUser().getPosition().getY()).addEntity(entity);
        }

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

            if (tile.getTileHeight() < item.getTotalHeight() || item.getDefinition().getBehaviour().isPublicSpaceObject()) {
                tile.setItemBelow(tile.getHighestItem());
                tile.setTileHeight(item.getTotalHeight());
                tile.setHighestItem(item);

                List<Position> affectedTiles = AffectedTile.getAffectedTiles(item);

                for (Position position : affectedTiles) {
                    if (position.getX() == item.getPosition().getX() && position.getY() == item.getPosition().getY()) {
                        continue;
                    }

                    RoomTile affectedTile = this.getTile(position.getX(), position.getY());

                    affectedTile.setTileHeight(item.getTotalHeight());
                    affectedTile.setHighestItem(item);
                }

                if (item.getDefinition().getBehaviour().isPublicSpaceObject()) {
                    PoolHandler.setupRedirections(this.room, item);
                }
            }
        }
    }

    /**
     * Add a specific item to the room map
     *
     * @param item the item to add
     */
    public void addItem(Item item) {
        item.setRoomId(this.room.getId());
        this.room.getItems().add(item);

        if (item.getDefinition().getBehaviour().isWallItem()) {
            this.room.send(new PLACE_WALLITEM(item));
        } else {
            this.handleItemAdjustment(item, false);
            this.regenerateCollisionMap();

            this.room.send(new PLACE_FLOORITEM(item));
        }

        item.updateEntities(null);
        ItemDao.updateItem(item);
    }

    /**
     * Move an item, will regenerate the map if the item is a floor item.
     *
     * @param item the item that is moving
     * @param isRotation whether it's just rotation or not
     *        (don't regenerate map or adjust position if it's just a rotation)
     */
    public void moveItem(Item item, boolean isRotation, Position oldPosition) {
        item.setRoomId(this.room.getId());

        if (!item.getDefinition().getBehaviour().isWallItem()) {
            this.handleItemAdjustment(item, isRotation);
            this.regenerateCollisionMap();

            this.room.send(new MOVE_FLOORITEM(item));
        }

        item.updateEntities(oldPosition);
        ItemDao.updateItem(item);
    }

    /**
     * Remove an item from the room.
     *
     * @param item the item that is being removed
     */
    public void removeItem(Item item) {
        item.setOwnerId(this.room.getData().getOwnerId());

        if (item.getDefinition().getBehaviour().isWallItem()) {
            this.room.send(new REMOVE_WALLITEM(item));
        } else {
            this.regenerateCollisionMap();
            this.room.send(new REMOVE_FLOORITEM(item));
        }

        item.updateEntities(null);

        item.getPosition().setX(0);
        item.getPosition().setY(0);
        item.getPosition().setZ(0);
        item.getPosition().setRotation(0);
        item.setRoomId(0);
        ItemDao.updateItem(item);
    }
    /**
     * Handle item adjustment.
     *
     * @param item the item
     * @param isRotation the rotation only
     */
    private void handleItemAdjustment(Item item, boolean isRotation) {
        RoomTile tile = this.getTile(item.getPosition().getX(), item.getPosition().getY());

        if (tile == null) {
            return;
        }

        if (!isRotation) {
            item.getPosition().setZ(tile.getTileHeight());
        }

        if (item.getPosition().getZ() > 8) {
            item.getPosition().setZ(8);
        }
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

        if (this.roomModel.getTileState(position.getX(), position.getY()) == RoomTileState.CLOSED) {
            return false;
        }

        if (tile.getHighestItem() != null) {
            if (!tile.getHighestItem().isWalkable()) {
                return tile.getHighestItem().getPosition().equals(entity.getRoomUser().getPosition());
            }
        }

        return true;
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