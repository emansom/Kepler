package org.alexdev.kepler.game.item.roller;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.pathfinder.Position;

public class EntityRollingAnalysis implements RollingAnalysis<Entity> {
    @Override
    public Position canRoll(Entity entity, Item roller) {
        return null;
    }

    @Override
    public void doRoll(Entity entity, Position nextPosition) {

    }
}
