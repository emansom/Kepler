package org.alexdev.kepler.game.room.public_rooms.walkways;

import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.RoomManager;
import org.alexdev.kepler.game.room.RoomUser;

import java.util.ArrayList;
import java.util.List;

public class WalkwaysManager {
    private static WalkwaysManager instance;
    private List<WalkwaysEntrance> walkways;

    public WalkwaysManager() {
        this.walkways = new ArrayList<>();

        this.addWalkway("rooftop", "rooftop_2", "9,4 10,3 9,3", null, false);
        this.addWalkway("rooftop_2", "rooftop", "3,10 4,10 5,10 3,11 4,11 5,11", "10,5,4,4", true);

        this.addWalkway("old_skool0", "old_skool1", "16,18", null, false);
        this.addWalkway("old_skool1", "old_skool0", "0,7", "15,18,0,6", true);

        this.addWalkway("malja_bar_a", "malja_bar_b", "14,0 15,0", null, false);
        this.addWalkway("malja_bar_b", "malja_bar_a", "5,25 ", "15,1,4,4", true);

        this.addWalkway("bar_a", "bar_b", "9,32 10,32 11,32 9,33 10,33", null, false);
        this.addWalkway("bar_b", "bar_a", "1,10 1,11 1,12", "10,30,5,0", true);

        this.addWalkway("pool_a", "pool_b", "19,3 20,4 21,5 22,6 23,7 24,8 25,9 26,10 27,11 28,12", null, false);
        this.addWalkway("pool_b", "pool_a", "0,13 1,14 2,15 3,16 4,17 5,18 6,19 7,20 8,21 9,22 10,23 11,24 12,25", "23,7,7,5", true);

        this.addWalkway("hallway2", "hallway0", "0,6 0,7 0,8 0,9", "29,3,1,6", false);
        this.addWalkway("hallway2", "hallway3", "6,23 7,23 8,23 9,23", "7,2,1,4", false);
        this.addWalkway("hallway2", "hallway4", "27,6 27,7 27,8 27,9", "2,3,0,2", false);
        this.addWalkway("hallway0", "hallway2", "31,5 31,4 31,3 31,2", "2,7,1,2", true);
        this.addWalkway("hallway0", "hallway1", "14,19 15,19 16,19 17,19", "15,2,0,4", true);
        this.addWalkway("hallway1", "hallway3", "31,9 31,8 31,7 31,6", "2,8,1,2", true);
        this.addWalkway("hallway1", "hallway0", "17,0 16,0 15,0 14,0", "16,17,1,0", true);
        this.addWalkway("hallway3", "hallway2", "9,0 8,0 7,0 6,0", "8,21,1,0", true);
        this.addWalkway("hallway3", "hallway1", "0,9 0,8 0,7 0,6", "29,7,0,6", true);
        this.addWalkway("hallway3", "hallway5", "31,6 31,7 31,8 31,9", "2,15,0,2", true);
        this.addWalkway("hallway5", "hallway3", "0,17 0,16 0,15 0,14", "29,7,0,6", true);
        this.addWalkway("hallway5", "hallway4", "22,0 23,0 24,0 25,0", "24,17,1,0", true);
        this.addWalkway("hallway4", "hallway2", "0,2 0,3 0,4 0,5", "25,7,0,6", true);
        this.addWalkway("hallway4", "hallway5", "22,19 23,19 24,19 25,19", "24,2,1,4", true);
    }

    private void addWalkway(String modelFrom, String modelTo, String fromCoords, String destination, boolean hideRoom) {
        List<Position> coordinates = new ArrayList<>();

        for (String coord : fromCoords.split(" ")) {
            int x = Integer.parseInt(coord.split(",")[0]);
            int y = Integer.parseInt(coord.split(",")[1]);
            coordinates.add(new Position(x, y));
        }

        Position destinationPosition = null;

        if (destination != null) {
            String[] data = destination.split(",");
            int x = Integer.parseInt(data[0]);
            int y = Integer.parseInt(data[1]);
            int z = Integer.parseInt(data[2]);
            int rotation = Integer.parseInt(data[3]);
            destinationPosition = new Position(x, y, z, rotation, rotation);
        }

        WalkwaysEntrance entrance = new WalkwaysEntrance(modelFrom, modelTo, coordinates, hideRoom, destinationPosition);
        this.walkways.add(entrance);
    }

    public WalkwaysEntrance getWalkway(RoomUser roomUser) {
        if (!roomUser.getRoom().isPublicRoom()) {
            return null;
        }

        for (WalkwaysEntrance entrance : this.walkways) {
            for (Position coord : entrance.getFromCoords()) {
                if (coord.equals(roomUser.getPosition())) {
                    return entrance;
                }
            }
        }

        return null;
    }

    public Room getWalkwayRoom(String model) {
        for (Room room : RoomManager.getInstance().getRooms()) {
            if (room.isPublicRoom() && room.getModel().getName().equals(model)) {
                return room;
            }
        }

        return null;
    }

    /**
     * Get the {@link WalkwaysManager} instance
     *
     * @return the item manager instance
     */
    public static WalkwaysManager getInstance() {
        if (instance == null) {
            instance = new WalkwaysManager();
        }

        return instance;
    }
}
