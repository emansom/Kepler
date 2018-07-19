package org.alexdev.kepler.game.item.roller;

import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.pathfinder.Position;

public class RollingData {
    private Item roller;
    private Position nextPosition;
    private double heightUpdate;

    public RollingData(Item roller, Position nextPosition) {
        this.roller = roller;
        this.heightUpdate = -1;
        this.nextPosition = nextPosition;
    }

    public Item getRoller() {
        return roller;
    }

    public double getHeightUpdate() {
        return heightUpdate;
    }

    public void setHeightUpdate(double heightUpdate) {
        this.heightUpdate = heightUpdate;
    }

    public Position getNextPosition() {
        return nextPosition;
    }
}
