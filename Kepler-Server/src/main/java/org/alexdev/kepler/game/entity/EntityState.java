package org.alexdev.kepler.game.entity;

import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.player.PlayerDetails;
import org.alexdev.kepler.game.room.Room;

import java.util.HashMap;

public class EntityState {
    private int entityId;
    private int instanceId;
    private PlayerDetails details;
    private Room room;
    private Position position;
    private HashMap<EntityStatus, String> statuses;

    public EntityState(int entityId, int instanceId, PlayerDetails details, Room room, Position position, HashMap<EntityStatus, String> statuses) {
        this.entityId = entityId;
        this.instanceId = instanceId;
        this.details = details;
        this.room = room;
        this.position = position;
        this.statuses = statuses;
    }

    public int getInstanceId() {
        return instanceId;
    }

    public Position getPosition() {
        return position;
    }

    public HashMap<EntityStatus, String> getStatuses() {
        return statuses;
    }

    public int getEntityId() {
        return entityId;
    }

    public PlayerDetails getDetails() {
        return details;
    }

    public Room getRoom() {
        return room;
    }
}
