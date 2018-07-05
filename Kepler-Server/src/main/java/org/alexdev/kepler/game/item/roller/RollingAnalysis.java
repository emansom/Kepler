package org.alexdev.kepler.game.item.roller;

import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.pathfinder.Position;

public interface RollingAnalysis<T> {
    public Position canRoll(T rollingType, Item roller);
    public void doRoll(T rollingType, Position nextPosition);
}
