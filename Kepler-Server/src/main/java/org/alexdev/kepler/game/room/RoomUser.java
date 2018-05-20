package org.alexdev.kepler.game.room;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.pathfinder.Position;

import java.util.HashMap;

public class RoomUser {
    private Entity entity;
    private Position position;
    private Position goal;
    private Position next;
    private Room room;
    private int instanceId;
    private HashMap<String, String> statuses;

    public RoomUser(Entity entity) {
        this.entity = entity;
        this.reset();
    }

    public void reset() {
        this.next = null;
        this.goal = null;
        this.room = null;
        this.instanceId = -1;
        this.statuses = new HashMap<>();
    }

    public Entity getEntity() {
        return entity;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getGoal() {
        return goal;
    }

    public void setGoal(Position goal) {
        this.goal = goal;
    }

    public Position getNext() {
        return next;
    }

    public void setNext(Position next) {
        this.next = next;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public int getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(int instanceId) {
        this.instanceId = instanceId;
    }

    public HashMap<String, String> getStatuses() {
        return this.statuses;
    }
}
