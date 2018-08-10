package org.alexdev.kepler.messages.incoming.rooms;

import org.alexdev.kepler.dao.mysql.RoomDao;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.RoomManager;
import org.alexdev.kepler.messages.outgoing.rooms.UPDATE_VOTES;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

public class ROOM_RATING implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        Room room = player.getRoomUser().getRoom();

        if (room == null || room.isPublicRoom()) {
            return;
        }

        int answer = reader.readInt();

        if (answer != 1 && answer != -1) {
            return;
        }

        if (answer == -1 && room.getData().getRating() <= 0) {
            return; // Don't allow votes to go negative, or it will tell user they haven't voted.
        }

        room.getData().setRating(room.getData().getRating() + answer);

        for (Player p : room.getEntityManager().getPlayers()){
            p.send(new UPDATE_VOTES(room.getData(), p.getDetails()));
        }

        RoomDao.vote(player.getDetails().getId(), player.getRoomUser().getRoom().getId(), answer);
    }
}
