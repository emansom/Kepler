package org.alexdev.kepler.game.room.mapping;

import javafx.geometry.Pos;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.util.StringUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RoomMapping {
    private Room room;
    private RoomModel roomModel;
    private RoomTile roomMap[][];

    public RoomMapping(Room room) {
        this.room = room;
        this.roomModel = this.room.getData().getModel();
        this.roomMap = new RoomTile[this.roomModel.getMapSizeX()][this.roomModel.getMapSizeY()];
    }

    public void regenerateCollisionMap() {
        for (int x = 0; x < this.roomModel.getMapSizeX(); x++) {
            for (int y = 0; y < this.roomModel.getMapSizeY(); y++) {
                this.roomMap[x][y] = new RoomTile(new Position(x, y));
                this.roomMap[x][y].setTileHeight(this.roomModel.getTileHeight(x, y));
            }
        }
    }

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

    public boolean isValidTile(Entity entity, Position position) {
        return this.roomModel.getTileState(position.getX(), position.getY()) == RoomTileState.OPEN;
    }

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