package org.alexdev.kepler.messages.incoming.rooms.items;

import org.alexdev.kepler.dao.mysql.ItemDao;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;
import org.alexdev.kepler.util.StringUtil;

public class SETITEMDATA implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        Room room = player.getRoomUser().getRoom();

        if (room == null) {
            return;
        }

        if (!room.hasRights(player.getDetails().getId())) {
            return;
        }

        String contents = reader.contents();

        int itemId = Integer.parseInt(contents.substring(0, contents.indexOf('/')));
        int itemIdLength = String.valueOf(itemId).length();

        String colour = contents.substring(itemIdLength + 1).substring(0, 6);
        String message = StringUtil.filterInput(contents.substring(itemIdLength + 8), false);

        if (!colour.equals("FFFFFF") &&
                !colour.equals("FFFF33") &&
                !colour.equals("FF9CFF") &&
                !colour.equals("9CFF9C") &&
                !colour.equals("9CCEFF")) {
            return;
        }

        if (message.length() > 684) {
            message = message.substring(0, 684);
        }

        Item item = room.getItemManager().getById(itemId);

        if (item == null) {
            return;
        }

        item.setCustomData(colour + message);
        item.updateStatus();

        ItemDao.updateItem(item);
    }
}
