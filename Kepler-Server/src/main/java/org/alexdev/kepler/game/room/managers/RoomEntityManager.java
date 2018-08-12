package org.alexdev.kepler.game.room.managers;

import org.alexdev.kepler.dao.mysql.ItemDao;
import org.alexdev.kepler.dao.mysql.RoomDao;
import org.alexdev.kepler.dao.mysql.RoomRightsDao;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.public_items.PublicItemParser;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.RoomManager;
import org.alexdev.kepler.game.room.mapping.RoomTile;
import org.alexdev.kepler.game.room.tasks.TeleporterTask;
import org.alexdev.kepler.messages.outgoing.rooms.FLATPROPERTY;
import org.alexdev.kepler.messages.outgoing.rooms.ROOM_URL;
import org.alexdev.kepler.messages.outgoing.rooms.ROOM_READY;
import org.alexdev.kepler.messages.outgoing.rooms.UPDATE_VOTES;
import org.alexdev.kepler.messages.outgoing.rooms.user.LOGOUT;
import org.alexdev.kepler.messages.incoming.rooms.user.HOTEL_VIEW;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class RoomEntityManager {
    private Room room;
    private AtomicInteger instanceIdCounter;

    public RoomEntityManager(Room room) {
        this.room = room;
        this.instanceIdCounter = new AtomicInteger(0);
    }

    /**
     * Generates a unique ID for the entities in a room. Will be used for pets
     * and bots in future.
     *
     * @return the unique ID
     */
    public int generateUniqueId() {
        int uniqueId = ThreadLocalRandom.current().nextInt(0, 9999);

        while (getByInstanceId(uniqueId) != null) {
            uniqueId = generateUniqueId();
        }

        return uniqueId;
    }

    /**
     * Return the list of entities currently in this room by its
     * given class.
     *
     * @param entityClass the entity class
     * @return List<{                                                               @                                                               link                                                                                                                               T                                                               }> list of entities
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
     * Return the list of players currently in this room by its
     * given class.
     *
     * @return List<{                                                               @                                                               link                                                                                                                               Player                                                               }> list players entities
     */
    public List<Player> getPlayers() {
        return getEntitiesByClass(Player.class);
    }

    /**
     * Get an entity by instance id.
     *
     * @param instanceId the instance id to get by
     * @return the entity
     */
    public Entity getByInstanceId(int instanceId) {
        for (Entity entity : this.room.getEntities()) {
            if (entity.getRoomUser().getInstanceId() == instanceId) {
                return entity;
            }
        }

        return null;
    }

    /**
     * Adds a generic entity to the room.
     * Will send packets if the entity is a player.
     *
     * @param entity      the entity to add
     * @param destination the (optional) destination to take the user to when they enter
     */
    public void enterRoom(Entity entity, Position destination) {
        if (entity.getRoomUser().getRoom() != null) {
            entity.getRoomUser().getRoom().getEntityManager().leaveRoom(entity, false);
        }

        // If the room is not loaded, add room, as we intend to join it.
        if (!RoomManager.getInstance().hasRoom(this.room.getId())) {
            RoomManager.getInstance().addRoom(this.room);
        }

        entity.getRoomUser().getTimerManager().resetRoomTimer();
        entity.getRoomUser().setRoom(this.room);
        entity.getRoomUser().setInstanceId(this.generateUniqueId());

        if (!this.room.isActive()) {
            this.initialiseRoom();
        }

        this.room.getEntities().add(entity);
        this.room.getData().setVisitorsNow(this.room.getEntityManager().getPlayers().size());

        Position entryPosition = this.room.getModel().getDoorLocation();

        if (destination != null) {
            entryPosition = destination.copy();
        }

        entity.getRoomUser().setPosition(entryPosition);
        entity.getRoomUser().setAuthenticateId(-1);

        if (entity.getRoomUser().getAuthenticateTelporterId() != -1) {
            Item teleporter = ItemDao.getItem(entity.getRoomUser().getAuthenticateTelporterId());

            if (teleporter != null) {
                Item linkedTeleporter = this.room.getItemManager().getById(teleporter.getTeleporterId());

                if (linkedTeleporter != null) {
                    entryPosition = linkedTeleporter.getPosition().copy();

                    new TeleporterTask(
                            linkedTeleporter,
                            entity,
                            this.room).run();

                    entity.getRoomUser().setWalkingAllowed(true);
                    entity.getRoomUser().setPosition(entryPosition);
                }
            }

            entity.getRoomUser().setAuthenticateTelporterId(-1);
        }

        // From this point onwards we send packets for the user to enter
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player) entity;

        player.send(new ROOM_URL());
        player.send(new ROOM_READY(this.room.getId(), this.room.getModel().getName()));

        if (this.room.getData().getWallpaper() > 0) {
            player.send(new FLATPROPERTY("wallpaper", this.room.getData().getWallpaper()));
        }

        if (this.room.getData().getFloor() > 0) {
            player.send(new FLATPROPERTY("floor", this.room.getData().getFloor()));
        }

        // Don't let the room owner vote on it's own room
        boolean voted = this.room.getData().getOwnerId() == player.getDetails().getId()
                || RoomDao.hasVoted(player.getDetails().getId(), room.getData().getId());

        player.send(new UPDATE_VOTES(voted, this.room.getData().getRating()));

        // TODO: send pending CFH messages that haven't been sent before as sending when on hotelview doesn't work

        RoomDao.saveVisitors(this.room);
    }

    /**
     * Setup the room initially for room entry.
     */
    private void initialiseRoom() {
        if (this.room.isActive()) {
            return;
        }

        this.room.setActive(true);

        if (this.room.isPublicRoom()) {
            this.room.getItems().addAll(PublicItemParser.getPublicItems(this.room.getId(), this.room.getModel().getId()));
        } else {
            this.room.getRights().addAll(RoomRightsDao.getRoomRights(this.room.getId()));
        }

        this.room.getItems().addAll(ItemDao.getRoomItems(this.room.getId()));
        this.room.getData().setRating(RoomDao.getRating(this.room.getId()));

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

        this.room.getEntities().remove(entity);

        // Set up trigger for leaving a current item
        if (entity.getRoomUser().getCurrentItem() != null) {
            if (entity.getRoomUser().getCurrentItem().getItemTrigger() != null) {
                entity.getRoomUser().getCurrentItem().getItemTrigger().onEntityLeave(entity, entity.getRoomUser(), entity.getRoomUser().getCurrentItem());
            }
        }

        RoomTile tile = entity.getRoomUser().getTile();

        if (tile != null) {
            tile.removeEntity(entity);
        }

        this.room.getData().setVisitorsNow(this.room.getEntityManager().getPlayers().size());
        this.room.send(new LOGOUT(entity.getRoomUser().getInstanceId()));
        this.room.tryDispose();

        entity.getRoomUser().reset();

        // From this point onwards we send packets for the user to leave
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player) entity;

        if (hotelView) {
            player.send(new HOTEL_VIEW());
        }

        player.getMessenger().sendStatusUpdate();
        RoomDao.saveVisitors(this.room);
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
