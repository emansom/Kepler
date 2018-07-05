package org.alexdev.kepler.game.item.roller;

import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.pathfinder.Position;

public interface RollingAnalysis<RollingType> {
    public Position canRoll(RollingType rollingType, Item roller);
    public void doRoll(RollingType rollingType, Position nextPosition);
}
