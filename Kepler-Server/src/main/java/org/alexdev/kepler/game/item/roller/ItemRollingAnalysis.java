package org.alexdev.kepler.game.item.roller;

import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.pathfinder.Position;

public class ItemRollingAnalysis implements RollingAnalysis<Item> {
    @Override
    public Position canRoll(Item item, Item roller) {
        return null;
    }

    @Override
    public boolean doRoll(Item item, Position nextPosition) {
        return false;
    }
}
