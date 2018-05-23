package org.alexdev.kepler.game.room;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityStatus;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.pathfinder.Pathfinder;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.room.mapping.RoomTile;
import org.alexdev.kepler.game.room.public_rooms.PoolHandler;
import org.alexdev.kepler.util.StringUtil;

import java.util.HashMap;
import java.util.LinkedList;

public class RoomUser {
    private Entity entity;
    private Position position;
    private Position goal;
    private Position nextPosition;
    private Room room;
    private int instanceId;

    private HashMap<EntityStatus, String> statuses;
    private LinkedList<Position> path;

    private boolean isWalkingAllowed;
    private boolean isWalking;
    private boolean beingKicked;
    private boolean needsUpdate;

    public RoomUser(Entity entity) {
        this.entity = entity;
        this.reset();
    }

    public void reset() {
        this.nextPosition = null;
        this.goal = null;
        this.room = null;
        this.isWalkingAllowed = true;
        this.isWalking = false;
        this.beingKicked = false;
        this.instanceId = -1;
        this.statuses = new HashMap<>();
        this.path = new LinkedList<>();
    }

    /**
     * Walk to specified position.
     *
     * @param X the x
     * @param Y the y
     */
    public void walkTo(int X, int Y) {
        if (!this.isWalkingAllowed) {
            return;
        }

        if (this.room == null) {
            return;
        }

        if (this.nextPosition != null) {
            this.position.setX(this.nextPosition.getX());
            this.position.setY(this.nextPosition.getY());
            this.updateNewHeight(this.position);
            this.needsUpdate = true;
        }

        RoomTile tile = this.room.getMapping().getTile(X, Y);

        if (tile == null) {
            return;
        }

        this.goal = new Position(X, Y);
        System.out.println("User requested " + this.goal + " from " + this.position + " with item " + (tile.getHighestItem() != null ? tile.getHighestItem().getDefinition().getSprite() : "NULL"));

        LinkedList<Position> path = Pathfinder.makePath(this.entity);

        if (path.size() > 0) {
            this.path = path;
            this.isWalking = true;
        }
    }

    /**
     * Called to make a user stop walking.
     */
    public void stopWalking() {
        this.path.clear();
        this.isWalking = false;
        this.needsUpdate = true;
        this.nextPosition = null;
        this.removeStatus(EntityStatus.MOVE);

        if (this.beingKicked) {
            this.room.getEntityManager().leaveRoom(this.entity, true);
            return;
        }

        this.invokeItem();
    }

    /**
     * Triggers the current item that the player has walked on top of.
     */
    public void invokeItem() {
        boolean needsUpdate = false;
        double height = this.getTile().getTileHeight();

        if (height != this.position.getZ()) {
            this.position.setZ(height);
            needsUpdate = true;
        }

        RoomTile tile = this.getTile();

        Item item = null;

        if (tile.getHighestItem() != null) {
            item = tile.getHighestItem();
        }

        if (item == null || (!item.getDefinition().getBehaviour().isCanSitOnTop() || !item.getDefinition().getBehaviour().isCanLayOnTop())) {
            if (this.containsStatus(EntityStatus.SIT) || this.containsStatus(EntityStatus.LAY)) {
                this.removeStatus(EntityStatus.SIT);
                this.removeStatus(EntityStatus.LAY);
                needsUpdate = true;
            }
        }

        if (item != null) {
            if (item.getDefinition().getBehaviour().isCanSitOnTop()) {
                this.removeStatus(EntityStatus.DANCE);
                this.position.setRotation(item.getPosition().getRotation());
                this.setStatus(EntityStatus.SIT, " " + StringUtil.format(item.getDefinition().getTopHeight()));
                needsUpdate = true;
            }

            if (item.getDefinition().getBehaviour().isCanLayOnTop()) {
                this.removeStatus(EntityStatus.DANCE);
                this.position.setRotation(item.getPosition().getRotation());
                this.setStatus(EntityStatus.LAY, " " + StringUtil.format(item.getDefinition().getTopHeight()));
                needsUpdate = true;
            }

            if (item.getDefinition().getSprite().equals("poolBooth")) {
                PoolHandler.interact(item, this.entity);
            }
        }

        this.needsUpdate = needsUpdate;
    }

    /**
     * Update new height.
     *
     * @param position the position
     */
    public void updateNewHeight(Position position) {
        double height = this.room.getMapping().getTile(position.getX(), position.getY()).getTileHeight();
        this.position.setZ(height);
    }

    /**
     * Get the current tile the user is on.
     *
     * @return the room tile instance
     */
    public RoomTile getTile() {
        if (this.room == null) {
            return null;
        }

        return this.room.getMapping().getTile(this.position.getX(), this.position.getY());
    }

    /**
     * Contains status.
     *
     * @param status the status
     * @return true, if successful
     */
    public boolean containsStatus(EntityStatus status) {
        return this.statuses.containsKey(status);
    }

    /**
     * Removes the status.
     *
     * @param status the status
     * @return if the user contained the status
     */
    public void removeStatus(EntityStatus status) {
        this.statuses.remove(status);
    }

    /**
     * Sets the status.
     *
     * @param status the status
     * @param value the value
     */
    public void setStatus(EntityStatus status, String value) {
        if (this.containsStatus(status)) {
            this.removeStatus(status);
        }

        this.statuses.put(status, value);
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

    public Position getNextPosition() {
        return nextPosition;
    }

    public void setNextPosition(Position nextPosition) {
        this.nextPosition = nextPosition;
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

    public HashMap<EntityStatus, String> getStatuses() {
        return this.statuses;
    }

    public LinkedList<Position> getPath() {
        return path;
    }

    public void setPath(LinkedList<Position> path) {
        this.path = path;
    }

    public boolean isWalkingAllowed() {
        return isWalkingAllowed;
    }

    public void setWalkingAllowed(boolean walkingAllowed) {
        isWalkingAllowed = walkingAllowed;
    }

    public boolean isWalking() {
        return isWalking;
    }

    public void setWalking(boolean walking) {
        isWalking = walking;
    }

    public boolean isNeedsUpdate() {
        return needsUpdate;
    }

    public void setNeedsUpdate(boolean needsUpdate) {
        this.needsUpdate = needsUpdate;
    }

    public void setBeingKicked(boolean beingKicked) {
        this.beingKicked = beingKicked;
    }

    public boolean isBeingKicked() {
        return beingKicked;
    }
}
