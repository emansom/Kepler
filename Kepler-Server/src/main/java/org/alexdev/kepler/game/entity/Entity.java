package org.alexdev.kepler.game.entity;

import org.alexdev.kepler.game.moderation.Fuseright;
import org.alexdev.kepler.game.player.PlayerDetails;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.RoomUser;

public abstract class Entity {

    /**
     * Checks for permission.
     *
     * @param permission the permission
     * @return true, if successful
     */
    public abstract boolean hasFuse(Fuseright permission);
    
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
}
