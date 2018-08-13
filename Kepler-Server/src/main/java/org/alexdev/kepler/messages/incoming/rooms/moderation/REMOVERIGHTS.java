package org.alexdev.kepler.messages.incoming.rooms.moderation;

import org.alexdev.kepler.dao.mysql.RoomRightsDao;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.player.PlayerManager;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

public class REMOVERIGHTS implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        if (player.getRoomUser().getRoom() == null) {
            return;
        }

        Room room = player.getRoomUser().getRoom();

        if (!room.isOwner(player.getEntityId()) && !player.hasFuse("fuse_any_room_controller")) {
            return;
        }

        Player target = PlayerManager.getInstance().getPlayerByName(reader.contents());

        if (target == null) {
            return;
        }

        Integer userId = target.getEntityId();

        if (!room.getRights().contains(userId)) {
            return;
        }

        room.getRights().remove(userId);
        room.refreshRights(target);

        target.getRoomUser().setNeedsUpdate(true);
        RoomRightsDao.removeRights(userId, room.getId());
    }
}
