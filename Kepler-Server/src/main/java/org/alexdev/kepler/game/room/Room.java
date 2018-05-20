package org.alexdev.kepler.game.room;

public class Room {
    private RoomData roomData;

    public Room() {
        this.roomData = new RoomData();
    }

    public RoomData getData() {
        return roomData;
    }
}
