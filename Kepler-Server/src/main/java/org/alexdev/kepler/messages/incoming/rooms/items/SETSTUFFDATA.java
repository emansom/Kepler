package org.alexdev.kepler.messages.incoming.rooms.items;

import org.alexdev.kepler.dao.mysql.ItemDao;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

public class SETSTUFFDATA implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        if (player.getRoomUser().getRoom() == null) {
            return;
        }

        // Do not process public room items
        if (reader.contents().contains("/")) {
            return;
        }

        Room room = player.getRoomUser().getRoom();

        int itemId = Integer.parseInt(reader.readString());
        String itemData = reader.readString();

        Item item = room.getItemManager().getById(itemId);

        if (item == null) {
            return;
        }

        if (item.getDefinition().getBehaviour().isRequiresRightsForInteraction() &&
                !room.hasRights(player.getEntityId())) {
            return;
        }

        String newData = null;

        if (item.getDefinition().getBehaviour().isDoor()) {
            if (itemData.equals("O") || itemData.equals("C")) {
                newData = itemData;
            }
        } else {
            if (item.getDefinition().getBehaviour().isCustomDataTrueFalse() && (itemData.equals("TRUE") || itemData.equals("FALSE"))) {
                newData = itemData;
            }

            if (item.getDefinition().getBehaviour().isCustomDataNumericOnOff() && (itemData.equals("2") || itemData.equals("1"))) {
                newData = itemData;
            }

            if (item.getDefinition().getBehaviour().isCustomDataOnOff() && (itemData.equals("ON") || itemData.equals("OFF"))) {
                newData = itemData;
            }

            if (item.getDefinition().getBehaviour().isCustomDataNumericState()) {
                if (!itemData.equals("x")) {
                    int stateId = Integer.parseInt(itemData);

                    if (stateId >= 0 && stateId <= 99) {
                        newData = itemData;
                    }
                }
            }
        }

        if (newData == null) {
            return;
        }

        item.setCustomData(newData);
        item.updateStatus();

        if (!item.getDefinition().getBehaviour().isCustomDataTrueFalse()) {
            ItemDao.updateItem(item);
        }
    }
}
