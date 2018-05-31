package org.alexdev.kepler.game.room.public_rooms;

import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.RoomUser;

public class SunTerraceHandler {
    public static boolean isRedirected(RoomUser roomUser, int targetX, int targetY) {
        Room room = roomUser.getRoom();

        if (!room.getModel().getName().equals("sun_terrace")) {
            return false;
        }

        double currentZ = roomUser.getPosition().getZ();
        double goalZ  = room.getMapping().getTile(targetX, targetY).getTileHeight();

        if (!(currentZ >= 8) && goalZ >= 8 && roomUser.getPosition().getX() != 4 && roomUser.getPosition().getY() != 18) {
            return true;
        }

        return targetX == 4 && targetY == 18 && roomUser.getPosition().getX() != 6 && roomUser.getPosition().getY() != 21;
    }
}
