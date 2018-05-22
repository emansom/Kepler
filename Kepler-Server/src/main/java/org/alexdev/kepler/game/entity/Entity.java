package org.alexdev.kepler.game.entity;

import org.alexdev.kepler.game.player.PlayerDetails;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.RoomUser;

public abstract class Entity {
    
    protected boolean disposed;

    /**
     * Checks for permission.
     *
     * @param permission the permission
     * @return true, if successful
     */
    public abstract boolean hasFuse(String permission);
    
    /**
     * Gets the details.
     *
     * @return the details
     */
    public abstract PlayerDetails getDetails();
    
    /**
     * Gets the player id
     * 
     * @return the id
     */
    public int getEntityId() {
        return this.getDetails().getId();
    }
    
    /**
     * Gets the room user.
     *
     * @return the room user
     */
    public abstract RoomUser getRoomUser();
    
    /**
     * Gets the type.
     *
     * @return the type
     */
    public abstract EntityType getType();
    
    /**
     * Dispose.
     */
    public abstract void dispose();

    /**
     * Gets the room.
     *
     * @return the room
     */
    public Room getRoom() {
        return getRoomUser().getRoom();
    }

    /**
     * In room.
     *
     * @return true, if successful
     */
    public boolean inRoom() {
        return getRoomUser().getRoom() != null;
    }
    
    /**
     * Checks if is disposed.
     *
     * @return true, if is disposed
     */
    public boolean isDisposed() {
        return this.disposed;
    }
    
    /**
     * Sets the disposed.
     *
     * @param flag the new disposed
     */
    public void setDisposed(boolean flag) {
        this.disposed = flag;
    }
}
