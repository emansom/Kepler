package org.alexdev.kepler.messages.incoming.rooms;

import org.alexdev.kepler.dao.Storage;
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

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("INSERT INTO users_room_votes (user_id, room_id, vote) VALUES (?, ?, ?)", sqlConnection);
            preparedStatement.setInt(1, player.getDetails().getId());
            preparedStatement.setInt(2, room.getId());
            if(upvote) {
                preparedStatement.setInt(3, room.getData().getVisitorsNow());
            }else{
                preparedStatement.setInt(3, room.getData().getVisitorsNow());
            }
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }
}
