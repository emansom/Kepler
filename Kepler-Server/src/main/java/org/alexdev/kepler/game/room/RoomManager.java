package org.alexdev.kepler.game.room;

import org.alexdev.kepler.dao.mysql.RoomDao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class RoomManager {
    private static RoomManager instance = null;

    private ConcurrentHashMap<Integer, Room> roomMap;

    public RoomManager() {
        this.roomMap = new ConcurrentHashMap<>();
        this.initialisePublicRooms();
    }

    /**
     * Add walkway settings for various public rooms that have enabled walkways.
     * Such as making sure the total room population is counted and adding the sub rooms.
     */
    public void initialisePublicRooms() {
        for (Room publicRoom : this.roomMap.values()) {
            if (!publicRoom.isPublicRoom()) {
                continue;
            }

            publicRoom.getData().addPublicItems();
            publicRoom.getData().checkWalkwaySettings(this);
        }
    }

    /**
     * Find a room by its model.
     *
     * @param model the model to find the room by
     * @return the room found, else null
     */
    public Room getRoomByModel(String model) {
        for (Room room : this.roomMap.values()) {
            if (room.getModel().getName().equals(model)) {

                return room;
            }
        }

        return null;
    }

    /**
     * Find a room by room id.
     *
     * @param roomId the id of the room to find
     * @return the loaded room instance, if successful, else query the db
     */
    public Room getRoomById(int roomId) {
        if (this.roomMap.containsKey(roomId)) {
            return this.roomMap.get(roomId);
        }

        return RoomDao.getRoomById(roomId);
    }

    /**
     *
     * @param roomId
     * @return
     */
    public boolean hasRoom(int roomId) {
        return this.roomMap.containsKey(roomId);
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

    public void sortRooms(List<Room> roomList) {
        roomList.sort(Comparator.comparingDouble((Room room) -> room.getData().getTotalVisitorsNow()).reversed());
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
