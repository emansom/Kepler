package org.alexdev.kepler.messages.incoming.rooms;

import org.alexdev.kepler.dao.mysql.RoomDao;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.RoomManager;
import org.alexdev.kepler.messages.outgoing.rooms.FLAT_ACCESSIBLE;
import org.alexdev.kepler.messages.outgoing.user.LOCALISED_ERROR;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

public class TRYFLAT implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        int roomId = -1;
        String password = null;

        String contents = reader.contents();

        if (contents.contains("/")) {
            roomId = Integer.parseInt(contents.split("/")[0]);
            password = contents.split("/")[1];
        } else {
            roomId = Integer.parseInt(reader.contents());
        }

        Room room = RoomManager.getInstance().getRoomById(roomId);

        if (room == null) {
            room = RoomDao.getRoomById(roomId);
        }

        if (room == null) {
            return;
        }

        if (!player.hasFuse("fuse_enter_locked_rooms")) {
            if (room.getData().getAccessTypeId() == 1 && !room.isOwner(player.getDetails().getId())) {
                // TODO: Knocking
            }

            if (room.getData().getAccessTypeId() == 2 && !room.isOwner(player.getDetails().getId())) {
                if (!password.equals(room.getData().getPassword())) {
                    player.send(new LOCALISED_ERROR("Incorrect flat password"));
                    return;
                }
            }
        }

        /*if (player.getRoomUser().getRoom() != null) {
            player.getRoomUser().getRoom().getEntityManager().leaveRoom(player);
        }*/
        player.getRoomUser().setAuthenticateId(roomId);
        player.send(new FLAT_ACCESSIBLE());
    }
}
