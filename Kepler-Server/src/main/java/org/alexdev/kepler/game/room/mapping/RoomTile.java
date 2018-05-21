package org.alexdev.kepler.game.room.mapping;

import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.pathfinder.Position;

public class RoomTile {
    private Position position;
    private double tileHeight;

    private Item highestItem;
    private Item itemBelow;

    public RoomTile(Position position) {
        this.position = position;
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
}
