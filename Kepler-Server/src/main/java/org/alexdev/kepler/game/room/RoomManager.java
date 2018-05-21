package org.alexdev.kepler.game.room;

import org.alexdev.kepler.dao.mysql.RoomDao;
import org.alexdev.kepler.dao.mysql.RoomModelDao;
import org.alexdev.kepler.game.room.models.RoomModel;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class RoomManager {
    private static RoomManager instance = null;

    private ConcurrentHashMap<Integer, Room> roomMap;

    public RoomManager() {
        this.roomMap = new ConcurrentHashMap<>();
        this.addPublicRooms();
    }

    /**
     * Load all public rooms from database, these
     * rooms have an owner id of 0.
     */
    private void addPublicRooms() {
        for (Room publicRoom : RoomDao.getRoomsByUserId(0)) {
            this.addRoom(publicRoom);
        }
    }

    /**
     * Find a room by room id.
     *
     * @param roomId the id of the room to find
     * @return the room, if successful, else null.
     */
    public Room getRoomById(int roomId) {
        if (this.roomMap.containsKey(roomId)) {
            return this.roomMap.get(roomId);
        }

        return null;
    }

    /**
     * Removes a room from the map by room id as key.
     *
     * @param roomId the id of the room to remove
     */
    public void removeRoom(int roomId) {
        this.roomMap.remove(roomId);
    }

    /**
     * Add a room to the map, by querying the database for the
     * specific type of id.
     *
     * @param roomId the id of the room to add
     */
    public void addRoom(int roomId) {
        if (this.roomMap.containsKey(roomId)) {
            return;
        }

        addRoom(RoomDao.getRoomById(roomId));
    }

    /**
     * Add a room instance to the map.
     *
     * @param room the instance of the room
     */
    public void addRoom(Room room) {
        if (room == null) {
            return;
        }

        this.roomMap.put(room.getData().getId(), room);
    }

    /**
     * Get the instance of {@link RoomManager}
     *
     * @return the instance
     */
    public static RoomManager getInstance() {
        if (instance == null) {
            System.out.println("test");
            instance = new RoomManager();
        }

        return instance;
    }

    /**
     * Get the entire list of rooms.
     *
     * @return the collection of rooms
     */
    public Collection<Room> getRooms() {
        return this.roomMap.values();
    }
}
