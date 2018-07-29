package org.alexdev.kepler.messages.incoming.rooms;

import org.alexdev.kepler.dao.Storage;
import org.alexdev.kepler.dao.mysql.RoomDao;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ROOM_RATING implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        boolean upvote = reader.contents().charAt(0) == 'I';

        Room room = player.getRoomUser().getRoom();

        if(room == null || (room != null && room.isPublicRoom()))
            return;

        RoomDao.vote(player.getDetails().getId(), player.getRoomUser().getRoom().getId(), (upvote ? 1 : -1));
    }
}
