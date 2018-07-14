package org.alexdev.kepler.messages.incoming.rooms.items;

import org.alexdev.kepler.dao.mysql.ItemDao;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.base.ItemBehaviour;
import org.alexdev.kepler.game.item.base.ItemDefinition;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.mapping.RoomTile;
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

        if (item.hasBehaviour(ItemBehaviour.ROOMDIMMER)
                || item.hasBehaviour(ItemBehaviour.DICE)
                || item.hasBehaviour(ItemBehaviour.PRIZE_TROPHY)
                || item.hasBehaviour(ItemBehaviour.POST_IT)
                || item.hasBehaviour(ItemBehaviour.WHEEL_OF_FORTUNE)) {
            return; // Prevent dice rigging, scripting trophies, post-its, etc.
        }

        if (item.getDefinition().hasBehaviour(ItemBehaviour.REQUIRES_RIGHTS_FOR_INTERACTION) &&
                !room.hasRights(player.getEntityId())) {
            return;
        }

        String newData = null;

        if (item.getDefinition().hasBehaviour(ItemBehaviour.DOOR)) {
            if (itemData.equals("O") || itemData.equals("C")) {
                newData = itemData;

                if (itemData.equals("C")) {
                    RoomTile tile = item.getTile();

                    // Make all entities walk out of gate when it's closed
                    if (tile.getEntities().size() > 0) {
                        for (Entity entity : tile.getEntities()) {
                            if (entity.getRoomUser().isWalking()) {
                                continue;
                            }

                            // Can't close gate if there's a user on the tile
                            return;
                            //entity.getRoomUser().walkTo(item.getPosition().getSquareInFront().getX(), item.getPosition().getSquareInFront().getY());
                        }
                    }
                }
            }
        } else {
            if (item.getDefinition().hasBehaviour(ItemBehaviour.CUSTOM_DATA_TRUE_FALSE) && (itemData.equals("TRUE") || itemData.equals("FALSE"))) {
                newData = itemData;
            }

            if (item.getDefinition().hasBehaviour(ItemBehaviour.CUSTOM_DATA_NUMERIC_ON_OFF) && (itemData.equals("2") || itemData.equals("1") || itemData.equals("0"))) {
                newData = itemData;
            }

            if (item.getDefinition().hasBehaviour(ItemBehaviour.CUSTOM_DATA_ON_OFF) && (itemData.equals("ON") || itemData.equals("OFF"))) {
                newData = itemData;
            }

            if (item.getDefinition().hasBehaviour(ItemBehaviour.CUSTOM_DATA_NUMERIC_STATE)) {
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

        if (!item.getDefinition().hasBehaviour(ItemBehaviour.CUSTOM_DATA_TRUE_FALSE)) {
            ItemDao.updateItem(item);
        }
    }
}
