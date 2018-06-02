package org.alexdev.kepler.messages.incoming.rooms.items;

import org.alexdev.kepler.dao.mysql.ItemDao;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.RoomUser;
import org.alexdev.kepler.messages.outgoing.rooms.items.DICE_VALUE;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;
import org.alexdev.kepler.util.StringUtil;

import java.util.concurrent.ThreadLocalRandom;


public class THROW_DICE implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        RoomUser roomUser = player.getRoomUser();
        Room room = roomUser.getRoom();

        if (room == null) {
            return;
        }

        String contents = reader.contents();

        // Check if input is numeric
        if (!StringUtil.isNumber(contents)) {
            return;
        }

        // Parse numeric content
        int itemId = Integer.parseInt(contents);

        if (itemId < 0) {
            return;
        }

        // Get item by ID
        Item item = room.getItemManager().getById(itemId);

        // Check if item exists and if it is a dice
        if (item == null || !item.getBehaviour().isDice()) {
            return;
        }

        // Check if user is next to dice
        if (!roomUser.getTile().touches(item.getTile())) {
            return;
        }

        int randomNumber = ThreadLocalRandom.current().nextInt(1, 7); // between 1 and 6

        room.send(new DICE_VALUE(itemId, true, 0));

        item.setCustomData(Integer.toString(randomNumber));
        item.setRequiresUpdate(true);

        ItemDao.updateItem(item);
    }
}