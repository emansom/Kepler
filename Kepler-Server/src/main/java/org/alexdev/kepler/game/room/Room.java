package org.alexdev.kepler.game.room;

public class Room {
    private RoomData roomData;

    public Room() {
        this.roomData = new RoomData();
    }

    public boolean isPublicRoom() {
        return this.roomData.getOwnerId() == 0;
    }

    public boolean isPrivateRoom() {
        return this.roomData.getOwnerId() > 0;
    }

    public RoomData getData() {
        return roomData;
    }
}
