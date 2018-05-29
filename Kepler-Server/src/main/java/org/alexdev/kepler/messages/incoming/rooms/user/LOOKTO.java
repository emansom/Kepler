package org.alexdev.kepler.messages.incoming.rooms.user;

import org.alexdev.kepler.game.room.enums.StatusType;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.pathfinder.Rotation;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

public class LOOKTO implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        if (player.getRoomUser().getRoom() == null) {
            return;
        }

        //Room room = player.getRoomUser().getRoom();

        String[] data = reader.contents().split(" ");

        Position lookDirection = new Position(
                Integer.parseInt(data[0]),
                Integer.parseInt(data[1]));

        if (player.getRoomUser().containsStatus(StatusType.SIT) || player.getRoomUser().containsStatus(StatusType.LAY)) {
            return;
        }

        if (player.getRoomUser().getPosition().equals(lookDirection)) {
            return;
        }

        int rotation = Rotation.calculateHumanDirection(
                player.getRoomUser().getPosition().getX(),
                player.getRoomUser().getPosition().getY(),
                lookDirection.getX(),
                lookDirection.getY());

        player.getRoomUser().getPosition().setRotation(rotation);
        player.getRoomUser().setNeedsUpdate(true);
        player.getRoomUser().resetRoomTimer();
    }
}
