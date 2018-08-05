package org.alexdev.kepler.messages.incoming.rooms;

import org.alexdev.kepler.dao.mysql.ItemDao;
import org.alexdev.kepler.game.GameScheduler;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.base.ItemBehaviour;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.RoomManager;
import org.alexdev.kepler.game.room.tasks.TeleporterTask;
import org.alexdev.kepler.game.room.tasks.TeleporterTask2;
import org.alexdev.kepler.messages.outgoing.rooms.items.BROADCAST_TELEPORTER;
import org.alexdev.kepler.messages.outgoing.rooms.items.TELEPORTER_INIT;
import org.alexdev.kepler.messages.outgoing.rooms.user.USER_STATUSES;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class GETDOORFLAT implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        Room room = player.getRoomUser().getRoom();

        if (room == null) {
            return;
        }

        int itemId = Integer.parseInt(reader.contents());
        Item item = room.getItemManager().getById(itemId);

        if (item == null || !item.hasBehaviour(ItemBehaviour.TELEPORTER)) {
            return;
        }

        Item linkedTeleporter = ItemDao.getItem(item.getTeleporterId());

        if (linkedTeleporter == null) {
            return;
        }

        // If their current item is the teleporters linked them, authenticate them so they can teleport back when double clicking.
        if (player.getRoomUser().getCurrentItem() != null) {
            if (player.getRoomUser().getCurrentItem().getId() == item.getId() ||
                player.getRoomUser().getCurrentItem().getId() == linkedTeleporter.getId()) {
                player.getRoomUser().setAuthenticateTelporterId(item.getId());
            }
        }

        if (player.getRoomUser().getAuthenticateTelporterId() != item.getId()) {
            return;
        }

        room.send(new BROADCAST_TELEPORTER(item, player.getDetails().getName(), true));

        if (linkedTeleporter.getRoomId() != room.getId()) {
            player.send(new TELEPORTER_INIT(item.getTeleporterId(), linkedTeleporter.getRoomId()));
        } else {
            GameScheduler.getInstance().getSchedulerService().schedule(new TeleporterTask(
                    room.getItemManager().getById(item.getTeleporterId()),
                    player,
                    room),1000, TimeUnit.MILLISECONDS);

            GameScheduler.getInstance().getSchedulerService().schedule(new TeleporterTask2(
                    room.getItemManager().getById(item.getTeleporterId()),
                    player,
                    room),1500, TimeUnit.MILLISECONDS);
        }
    }
}