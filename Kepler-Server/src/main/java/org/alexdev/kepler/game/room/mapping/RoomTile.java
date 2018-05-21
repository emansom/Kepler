package org.alexdev.kepler.game.room.mapping;

import org.alexdev.kepler.game.pathfinder.Position;

public class RoomTile {
    private Position position;
    private double tileHeight;

    public RoomTile(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public double getTileHeight() {
        return tileHeight;
    }

    public void setTileHeight(double tileHeight) {
        this.tileHeight = tileHeight;
    }
}
