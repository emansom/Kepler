package org.alexdev.kepler.game.room.tasks;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.messages.outgoing.rooms.items.BROADCAST_TELEPORTER;
import org.alexdev.kepler.messages.outgoing.rooms.user.USER_STATUSES;

import java.util.List;

public class TeleporterTask implements Runnable {
    private final Item item;
    private final Entity entity;
    private final Room room;

    public TeleporterTask(Item linkedTeleporter, Entity entity, Room room) {
        this.item = linkedTeleporter;
        this.entity = entity;
        this.room = room;
    }

    @Override
    public void run() {
        this.entity.getRoomUser().warp(this.item.getPosition().copy(), true);
        this.entity.getRoomUser().setAuthenticateTelporterId(-1);
        this.entity.getRoomUser().setWalkingAllowed(true);

        this.room.send(new BROADCAST_TELEPORTER(this.item, this.entity.getDetails().getName(), false));
    }
}
