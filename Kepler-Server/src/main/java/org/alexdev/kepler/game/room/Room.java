package org.alexdev.kepler.game.room;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.player.PlayerManager;
import org.alexdev.kepler.game.room.managers.RoomEntityManager;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private RoomData roomData;
    private RoomEntityManager roomEntityManager;
    private List<Entity> entities;

    public Room() {
        this.roomData = new RoomData();
        this.roomEntityManager = new RoomEntityManager(this);
        this.entities = new ArrayList<>();
    }

    public void dispose() {
        if (this.roomEntityManager.getEntitiesByClass(Player.class).size() > 0) {
            return;
        }

        // Stop tasks here

        if (this.isPublicRoom()) {
            return;
        }

        // Clear items here

        if (PlayerManager.getInstance().getPlayerById(this.roomData.getOwnerId()) != null) { // Don't remove completely if owner is online
            return;
        }

        RoomManager.getInstance().removeRoom(this.roomData.getId());
    }

    public RoomEntityManager getEntityManager() {
        return this.roomEntityManager;
    }

    public boolean isPublicRoom() {
        return this.roomData.getOwnerId() == 0;
    }

    public RoomData getData() {
        return roomData;
    }

    public List<Entity> getEntities() {
        return entities;
    }
}
