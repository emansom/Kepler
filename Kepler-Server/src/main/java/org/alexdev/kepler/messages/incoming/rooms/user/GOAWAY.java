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

        var currentX = player.getRoomUser().getPosition().getX();
        var currentY = player.getRoomUser().getPosition().getY();
        var doorX = room.getData().getModel().getDoorX();
        var doorY = room.getData().getModel().getDoorY();

        if (currentX == doorX && currentY == doorY) {
            player.getRoomUser().getRoom().getEntityManager().leaveRoom(player, true);
            return;
        }

        player.getRoomUser().walkTo(doorX, doorY);
    }
}
