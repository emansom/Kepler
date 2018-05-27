package org.alexdev.kepler.messages.incoming.navigator;

import org.alexdev.kepler.dao.mysql.RoomDao;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.RoomManager;
import org.alexdev.kepler.messages.outgoing.navigator.SEARCH_NO_RESULTS;
import org.alexdev.kepler.messages.outgoing.navigator.SEARCH_RESULTS;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

import java.util.List;

public class SRCHF implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        String searchQuery = reader.contents();

        List<Room> roomList = RoomManager.getInstance().replaceQueryRooms(
                RoomDao.querySearchRooms(searchQuery));

        if (roomList.size() > 0) {
            RoomManager.getInstance().sortRooms(roomList);
            player.send(new SEARCH_RESULTS(roomList, player));
        } else {
            player.send(new SEARCH_NO_RESULTS());
        }
    }
}
