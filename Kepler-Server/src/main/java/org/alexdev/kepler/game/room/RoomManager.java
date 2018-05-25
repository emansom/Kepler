package org.alexdev.kepler.game.room;

import org.alexdev.kepler.dao.mysql.RoomDao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class RoomManager {
    private static RoomManager instance = null;

    private ConcurrentHashMap<Integer, Room> roomMap;

    public RoomManager() {
        this.roomMap = new ConcurrentHashMap<>();
        this.addRoomsByUser(0);
    }

    /**
     * Adds all rooms to the map if they dont already exist, by
     * the owner id
     *
     * @param ownerId the owner of the room
     */
    public void addRoomsByUser(int ownerId) {
        for (Room publicRoom : RoomDao.getRoomsByUserId(ownerId)) {
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
    public Room addRoom(int roomId) {
        Room room = RoomDao.getRoomById(roomId);
        addRoom(room);
        return room;
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

        if (this.roomMap.containsKey(room.getId())) {
            return;
        }

        this.roomMap.put(room.getData().getId(), room);
    }

    /**
     * Will sort a list of rooms returned by MySQL query and
     * replace any with loaded rooms that it finds.
     *
     * @param queryRooms the list of rooms returned by query
     * @return a possible list of actual loaded rooms
     */
    public List<Room> replaceQueryRooms(List<Room> queryRooms) {
        List<Room> roomList = new ArrayList<>();

        for (Room room : queryRooms) {
            Room loadedRoom = this.getRoomById(room.getData().getId());

            if (loadedRoom != null) {
                roomList.add(loadedRoom);
            } else {
                roomList.add(room);
            }
        }

        return roomList;
    }

    /**
     * Get the entire list of rooms.
     *
     * @return the collection of rooms
     */
    public Collection<Room> getRooms() {
        return this.roomMap.values();
    }

    /**
     * Get the instance of {@link RoomManager}
     *
     * @return the instance
     */
    public static RoomManager getInstance() {
        if (instance == null) {
            instance = new RoomManager();
        }

        return instance;
    }
}
