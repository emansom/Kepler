package org.alexdev.kepler.messages.incoming.rooms;

import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.RoomManager;
import org.alexdev.kepler.messages.outgoing.rooms.OPEN_CONNECTION;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

public class ROOM_DIRECTORY implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        char is_public = reader.contents().charAt(0);

        if (is_public != 'A') {
            player.send(new OPEN_CONNECTION());
            return;
        }

        reader.readBytes(1); // strip 'A'
        int roomId = reader.readInt();

        Room room = RoomManager.getInstance().getRoomById(roomId);

        if (room == null) {
            return;
        }

        room.getEntityManager().enterRoom(player, null);
     }
}
