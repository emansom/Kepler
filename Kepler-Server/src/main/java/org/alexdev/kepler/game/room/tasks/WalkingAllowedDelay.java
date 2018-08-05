package org.alexdev.kepler.game.room.tasks;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.messages.outgoing.rooms.items.BROADCAST_TELEPORTER;

public class WalkingAllowedDelay implements Runnable {
    private final Entity entity;

    public WalkingAllowedDelay(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void run() {
        this.entity.getRoomUser().setWalkingAllowed(true);
    }
}
