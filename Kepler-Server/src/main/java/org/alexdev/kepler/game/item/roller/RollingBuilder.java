package org.alexdev.kepler.game.item.roller;

import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.pathfinder.Position;

import java.util.ArrayList;
import java.util.List;

public class RollingBuilder {
    private int rollerId;
    private Position from;
    private Position to;

    private List<RollingData> rollingDataList;

    private RollingBuilder(Item roller) {
        this.rollerId = roller.getId();
        this.from = roller.getPosition().copy();
        this.to = roller.getPosition().getSquareInFront();
        this.rollingDataList = new ArrayList<>();
    }

    public int getRollerId() {
        return rollerId;
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    public List<RollingData> getRollingDataList() {
        return rollingDataList;
    }
}
