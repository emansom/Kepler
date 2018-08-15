package org.alexdev.kepler.messages.incoming.rooms.user;

import org.alexdev.kepler.dao.mysql.RoomDao;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.RoomManager;
import org.alexdev.kepler.messages.outgoing.rooms.UPDATE_VOTES;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

public class RATEFLAT implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        Room room = player.getRoomUser().getRoom();

        if (room == null || room.isPublicRoom()) {
            return;
        }

        // Room owner is not allowed to vote on his own room
        if (room.getData().getOwnerId() == player.getDetails().getId()) {
            return;
        }

        int answer = reader.readInt();

        if (answer != 1 && answer != -1) {
            return;
        }

        if (RoomDao.hasVoted(player.getEntityId(), room.getId())) {
            return;
        }

        RoomDao.vote(player.getDetails().getId(), player.getRoomUser().getRoom().getId(), answer);
        room.getData().setRating(RoomDao.getRating(room.getId()));

        for (Player p : room.getEntityManager().getPlayers()) {
            boolean voted = RoomDao.hasVoted(player.getDetails().getId(), room.getData().getId());
            p.send(new UPDATE_VOTES(voted, room.getData().getRating()));
        }
    }
}
