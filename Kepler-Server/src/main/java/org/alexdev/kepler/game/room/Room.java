package org.alexdev.kepler.game.room;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.player.PlayerManager;
import org.alexdev.kepler.game.room.managers.RoomEntityManager;
import org.alexdev.kepler.game.room.mapping.RoomMapping;
import org.alexdev.kepler.game.room.tasks.ProcessEntityTask;
import org.alexdev.kepler.messages.types.MessageComposer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

public class Room {
    private RoomData roomData;
    private RoomMapping roomMapping;
    private RoomEntityManager roomEntityManager;
    private ProcessEntityTask processEntity;
    private ScheduledFuture<?> processEntityTask;

    private List<Entity> entities;
    private List<Item> items;

    public Room() {
        this.roomData = new RoomData(this);
        this.roomEntityManager = new RoomEntityManager(this);
        this.processEntity = new ProcessEntityTask(this);
        this.entities = new ArrayList<>();
        this.items = new ArrayList<>();
    }

    public void send(MessageComposer composer) {
        for (Player player : this.roomEntityManager.getPlayers()) {
            player.send(composer);
        }
    }

    public void dispose() {
        if (this.roomEntityManager.getEntitiesByClass(Player.class).size() > 0) {
            return;
        }

        this.roomMapping = null;

        this.processEntityTask.cancel(false);
        this.processEntityTask = null;

        if (this.isPublicRoom()) {
            return;
        }

        // Clear items here

        if (PlayerManager.getInstance().getPlayerById(this.roomData.getOwnerId()) != null) { // Don't remove completely if owner is online
            return;
        }

        RoomManager.getInstance().removeRoom(this.roomData.getId());

        this.roomData = null;
    }

    public RoomEntityManager getEntityManager() {
        return this.roomEntityManager;
    }

    public boolean isPublicRoom() {
        return this.roomData.getOwnerId() == 0;
    }

    public ProcessEntityTask getProcessEntity() {
        return processEntity;
    }

    public RoomMapping getMapping() {
        return roomMapping;
    }

    public void setRoomMapping(RoomMapping roomMapping) {
        this.roomMapping = roomMapping;
    }

    public RoomData getData() {
        return roomData;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setProcessEntityTask(ScheduledFuture<?> processEntityTask) {
        this.processEntityTask = processEntityTask;
    }
}
