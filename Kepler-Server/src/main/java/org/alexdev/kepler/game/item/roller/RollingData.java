package org.alexdev.kepler.game.item.roller;

import org.alexdev.kepler.game.item.Item;

public class RollingData {
    private Item roller;
    private double heightUpdate;

    public RollingData(Item roller) {
        this.roller = roller;
        this.heightUpdate = -1;
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
}
