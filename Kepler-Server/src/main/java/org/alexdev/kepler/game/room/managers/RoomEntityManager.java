package org.alexdev.kepler.game.room.managers;

import org.alexdev.kepler.dao.mysql.ItemDao;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.public_rooms.PoolHandler;
import org.alexdev.kepler.messages.outgoing.rooms.FLATPROPERTY;
import org.alexdev.kepler.messages.outgoing.rooms.ROOM_URL;
import org.alexdev.kepler.messages.outgoing.rooms.ROOM_READY;
import org.alexdev.kepler.messages.outgoing.rooms.items.SHOWPROGRAM;
import org.alexdev.kepler.messages.outgoing.rooms.user.LOGOUT;
import org.alexdev.kepler.messages.outgoing.user.HOTEL_VIEW;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoomEntityManager {
    private Room room;
    private AtomicInteger instanceIdCounter;

    public RoomEntityManager(Room room) {
        this.room = room;
        this.instanceIdCounter = new AtomicInteger(0);
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
        if (entity.getRoom() != null) {
            entity.getRoom().getEntityManager().leaveRoom(entity, false);
        }

        if (this.room.getEntityManager().getEntitiesByClass(Player.class).isEmpty()) {
            this.initialiseRoom();
        }

        this.room.getEntities().add(entity);
        this.room.getData().setVisitorsNow(this.room.getEntityManager().getPlayers().size());

        entity.getRoomUser().setRoom(this.room);
        entity.getRoomUser().setInstanceId(this.instanceIdCounter.getAndIncrement());
        entity.getRoomUser().setPosition(new Position(
                this.room.getModel().getDoorX(),
                this.room.getModel().getDoorY(),
                this.room.getModel().getDoorZ(),
                this.room.getModel().getDoorRotation(),
                this.room.getModel().getDoorRotation()
        ));

        entity.getRoomUser().setAuthenticateId(-1);

        // From this point onwards we send packets for the user to enter
        if (entity.getType() !=  EntityType.PLAYER) {
            return;
        }

        Player player = (Player) entity;

        player.sendQueued(new ROOM_URL());
        player.sendQueued(new ROOM_READY(this.room.getId(), this.room.getModel().getName()));

        if (this.room.getData().getWallpaper() > 0) {
            player.sendQueued(new FLATPROPERTY("wallpaper", this.room.getData().getWallpaper()));
        }

        if (this.room.getData().getFloor() > 0) {
            player.sendQueued(new FLATPROPERTY("floor", this.room.getData().getFloor()));
        }

        for (Item item : this.room.getItems()) {
            if (item.getCurrentProgramValue().length() > 0) {
                player.sendQueued(new SHOWPROGRAM(item.getCurrentProgram(), item.getCurrentProgramValue()));
            }
        }

        player.flushSendQueue();
    }

    /**
     * Setup the room initially for room entry.
     */
    private void initialiseRoom() {
        if (!this.room.isPublicRoom()) {
            this.room.getItems().addAll(ItemDao.getRoomItems(this.room.getId()));
        }

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
        this.room.tryDispose(false);

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

    /**
     * Get the atomic integer counter for instance ids.
     *
     * @return the instance id counter
     */
    public AtomicInteger getInstanceIdCounter() {
        return this.instanceIdCounter;
    }
}
