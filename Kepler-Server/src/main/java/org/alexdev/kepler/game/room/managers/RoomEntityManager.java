package org.alexdev.kepler.game.room.managers;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.public_rooms.PoolHandler;
import org.alexdev.kepler.messages.outgoing.rooms.ROOM_URL;
import org.alexdev.kepler.messages.outgoing.rooms.ROOM_READY;
import org.alexdev.kepler.messages.outgoing.rooms.items.SHOWPROGRAM;
import org.alexdev.kepler.messages.outgoing.rooms.user.LOGOUT;
import org.alexdev.kepler.messages.outgoing.user.HOTEL_VIEW;
import org.alexdev.kepler.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class RoomEntityManager {
    private Room room;

    public RoomEntityManager(Room room) {
        this.room = room;
    }

    /**
     * Create a new instance ID for the next user who joins
     * @return the instance id
     */
    private int createInstanceId() {
        int instanceId = 0;

        while (this.getEntityByInstanceId(instanceId) != null) {
            instanceId++;
        }

        return instanceId;
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

    /**
     * Find an entity by the instance id
     * @param instanceId the instance id used to find
     * @return the entity, if successful, else null
     */
    private Entity getEntityByInstanceId(int instanceId) {
        for (Entity entity : this.room.getEntities()) {
            if (entity.getRoomUser().getInstanceId() == instanceId) {
                return entity;
            }
        }

        return null;
    }

    /**
     * Return the list of players currently in this room by its
     * given class.
     *
     * @return List<{@link Player}> list players entities
     */
    public List<Player> getPlayers() {
        return getEntitiesByClass(Player.class);
    }

    /**
     * Adds a generic entity to the room.
     * Will send packets if the entity is a player.
     *
     * @param entity the entity to add
     */
    public void enterRoom(Entity entity) {
        if (entity.getRoomUser().getRoom() != null) {
            entity.getRoomUser().getRoom().getEntityManager().leaveRoom(entity, false);
        }

        if (this.room.getEntityManager().getEntitiesByClass(Player.class).isEmpty()) {
            this.initialiseRoom();
        }

        this.room.getEntities().add(entity);
        this.room.getData().setVisitorsNow(this.room.getEntityManager().getPlayers().size());

        entity.getRoomUser().setRoom(this.room);
        entity.getRoomUser().setInstanceId(this.createInstanceId());
        entity.getRoomUser().setPosition(new Position(
                this.room.getData().getModel().getDoorX(),
                this.room.getData().getModel().getDoorY(),
                this.room.getData().getModel().getDoorZ(),
                this.room.getData().getModel().getDoorRotation(),
                this.room.getData().getModel().getDoorRotation()
        ));

        entity.getRoomUser().setAuthenticateId(-1);

        // From this point onwards we send packets for the user to enter
        if (entity.getType() !=  EntityType.PLAYER) {
            return;
        }

        Player player = (Player) entity;

        player.send(new ROOM_URL());
        player.send(new ROOM_READY(this.room.getId(), this.room.getData().getModel().getModelName()));

        for (Item item : this.room.getItems()) {
            if (item.getCurrentProgramValue().length() > 0) {
                player.send(new SHOWPROGRAM(item.getCurrentProgram(), item.getCurrentProgramValue()));
            }
        }
    }

    /**
     * Setup the room initially for room entry.
     */
    private void initialiseRoom() {
        this.room.getMapping().regenerateCollisionMap();
        this.room.getTaskManager().startTasks();
    }

    /**
     * Setup handler for the entity to leave room.
     *
     * @param entity the entity to leave
     */
    public void leaveRoom(Entity entity, boolean hotelView) {
        if (!this.room.getEntities().contains(entity)) {
            return;
        }

        if (entity.getType() == EntityType.PLAYER) {
            PoolHandler.disconnect((Player) entity);
        }

        this.room.getEntities().remove(entity);
        this.room.getData().setVisitorsNow(this.room.getEntityManager().getPlayers().size());

        this.room.send(new LOGOUT(entity.getRoomUser().getInstanceId()));
        this.room.dispose(false);

        entity.getRoomUser().reset();

        // From this point onwards we send packets for the user to leave
        if (entity.getType() !=  EntityType.PLAYER) {
            return;
        }

        Player player = (Player) entity;

        if (hotelView) {
            player.send(new HOTEL_VIEW());
        }
    }
}
