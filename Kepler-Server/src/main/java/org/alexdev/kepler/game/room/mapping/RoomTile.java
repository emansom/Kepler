package org.alexdev.kepler.game.room.mapping;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.room.Room;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RoomTile {
    private Room room;
    private Position position;
    private List<Entity> entities;

    private double tileHeight;

    private Item highestItem;
    private Item itemBelow;

    public RoomTile(Room room, Position position) {
        this.room = room;
        this.position = position;
        this.entities = new CopyOnWriteArrayList<>();
    }

    /**
     * Gets if the tile was valid.
     *
     * @param entity the entity checking
     * @param position the position of the tile
     * @return true, if successful
     */
    public static boolean isValidTile(Entity entity, Position position) {
        Room room = entity.getRoom();

        if (room == null) {
            return false;
        }

        RoomTile tile = room.getMapping().getTile(position);

        if (tile == null) {
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
     * Sets the entity.
     *
     * @param entity the new entity
     */
    public void addEntity(Entity entity) {
        if (new Position(this.position.getX(), this.position.getY()).equals(this.room.getModel().getDoorLocation())) {
            return;
        }

        this.entities.add(entity);
    }

    /**
     * Contains the entity.
     *
     * @param entity the entity
     * @return true, if successful
     */
    public boolean containsEntity(Entity entity) {
        return this.entities.contains(entity);
    }

    /**
     * Removes the entity.
     *
     * @param entity the entity
     * @return true, if successful
     */
    public void removeEntity(Entity entity) {
        this.entities.remove(entity);
    }

    /**
     * Get the current position of the tile.
     *
     * @return the position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Get the current height of the tile.
     *
     * @return the tile height
     */
    public double getTileHeight() {
        return tileHeight;
    }

    /**
     * Set the tile height of this tile.
     *
     * @param tileHeight the new tile height
     */
    public void setTileHeight(double tileHeight) {
        this.tileHeight = tileHeight;
    }

    /**
     * Get the highest item in this tile.
     *
     * @return the highest item
     */
    public Item getHighestItem() {
        return highestItem;
    }

    /**
     * Set the highest item in this tile.
     *
     * @return the highest item
     */
    public void setHighestItem(Item highestItem) {
        this.highestItem = highestItem;
    }

    /**
     * Get the item below the highest item in this tile.
     *
     * @return the item below
     */
    public Item getItemBelow() {
        return itemBelow;
    }

    /**
     * Set the item below the highest item in this tile.
     *
     * @param itemBelow the item below
     */
    public void setItemBelow(Item itemBelow) {
        this.itemBelow = itemBelow;
    }

    public List<Entity> getEntities() {
        return this.entities;
    }
}
