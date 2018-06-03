package org.alexdev.kepler.messages.incoming.rooms.settings;

import org.alexdev.kepler.dao.mysql.ItemDao;
import org.alexdev.kepler.dao.mysql.RoomDao;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.RoomManager;
import org.alexdev.kepler.log.Log;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

import java.util.ArrayList;
import java.util.List;

public class DELETEFLAT implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        int roomId = Integer.parseInt(reader.contents());
        delete(roomId, player.getDetails().getId());
    }

    public static void delete(int roomId, int userId) {
        Room room = RoomManager.getInstance().getRoomById(roomId);

        if (room == null) {
            return;
        }

        if (!room.isOwner(userId)) {
            return;
        }

        // TODO: ItemDao.deleteItems
        for (Item item : room.getItems()) {
            ItemDao.deleteItem(item.getId());
        }

        List<Entity> entities = new ArrayList<>(room.getEntities());

        for (Entity entity : entities) {
            room.getEntityManager().leaveRoom(entity, true);
        }

        if (!room.tryDispose()) {
            Log.getErrorLogger().error("Room " + roomId + " did not want to get disposed by player id " + userId);
        }

        RoomDao.delete(room);
    }
}
