package org.alexdev.kepler.game.room.managers;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.messages.outgoing.rooms.ROOM_URL;
import org.alexdev.kepler.messages.outgoing.rooms.ROOM_READY;

import java.util.ArrayList;
import java.util.List;

public class RoomEntityManager {
    private Room room;

    public RoomEntityManager(Room room) {
        this.room = room;
    }

    /**
     * Return the list of entities currently in this room by its
     * given class.
     *
     * @param entityClass the entity class
     * @return List<{@link T}> list of entities
     */
    public <T extends Entity> List<T> getEntitiesByClass(Class<T> entityClass) {
        List<T> entities = new ArrayList<>();

        for (Entity entity : this.room.getEntities()) {
            if (entity.getClass().isAssignableFrom(entityClass)) {
                entities.add(entityClass.cast(entity));
            }
        }

        return entities;
    }

    public void enterRoom(Entity entity) {
        if (entity.getRoomUser().getRoom() != null) {
            entity.getRoomUser().getRoom().getEntityManager().leaveRoom(entity);
        }

        this.room.getEntities().remove(entity);
        this.room.getData().setVisitorsNow(this.room.getEntities().size());

        // From this point onwards we send packets for the user to enter
        if (entity.getType() !=  EntityType.PLAYER) {
            return;
        }

        Player player = (Player) entity;

        player.send(new ROOM_URL());
        player.send(new ROOM_READY(this.room.getData().getId(), this.room.getData().getModel()));
    }

    public void leaveRoom(Entity entity) {
        if (!this.room.getEntities().contains(entity)) {
            return;
        }

        this.room.getEntities().remove(entity);
        this.room.getData().setVisitorsNow(this.room.getEntities().size());

        entity.getRoomUser().reset();
        this.room.dispose();
    }
}
