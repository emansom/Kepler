package org.alexdev.kepler.messages.incoming.rooms.user;

import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

public class GOAWAY implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        if (player.getRoomUser().getRoom() == null) {
            return;
        }

        Room room = player.getRoomUser().getRoom();

        var doorX = room.getModel().getDoorX();
        var doorY = room.getModel().getDoorY();
        var curPos = player.getRoomUser().getPosition();

        // If we're standing in the door, immediately leave room
        if (curPos.getX() == doorX && curPos.getY() == doorY) {
            room.getEntityManager().leaveRoom(player, true);
            return;
        }

        // Walk to door
        player.getRoomUser().walkTo(doorX, doorY);
    }
}
