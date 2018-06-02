package org.alexdev.kepler.messages.incoming.rooms.items;

import org.alexdev.kepler.dao.mysql.ItemDao;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.base.ItemBehaviour;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.RoomUser;
import org.alexdev.kepler.messages.outgoing.rooms.items.DICE_VALUE;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;
import org.alexdev.kepler.util.StringUtil;

import java.util.concurrent.ThreadLocalRandom;


public class SPIN_WHEEL_OF_FORTUNE implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        RoomUser roomUser = player.getRoomUser();
        Room room = roomUser.getRoom();

        if (room == null) {
            return;
        }

        if (!room.hasRights(player.getEntityId())) {
            return;
        }

        int itemId = reader.readInt();

        if (itemId < 0) {
            return;
        }

        // Get item by ID
        Item item = room.getItemManager().getById(itemId);

        // Check if item exists and if it is a wheel of fortune
        if (item == null || !item.hasBehaviour(ItemBehaviour.WHEEL_OF_FORTUNE)) {
            return;
        }

        // Spin already being executed, return
        if (item.getRequiresUpdate()) {
            return;
        }

        // Send spinning animation to room
        item.setCustomData("-1");
        item.updateStatus();

        // Set random number that gets picked up by the FortuneTask
        int randomNumber = ThreadLocalRandom.current().nextInt(1, 11); // between 1 and 10

        item.setCustomData(Integer.toString(randomNumber));
        item.setRequiresUpdate(true);

        ItemDao.updateItem(item);
    }
}