package org.alexdev.kepler.game.item;

public class ItemRollingData {
    private Item roller;
    private double heightUpdate;

    public ItemRollingData(Item roller) {
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
