package org.alexdev.kepler.messages.incoming.rooms.items;

import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.base.ItemBehaviour;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.messages.outgoing.rooms.items.MOVE_FLOORITEM;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

public class MOVESTUFF implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        Room room = player.getRoomUser().getRoom();

        if (room == null) {
            return;
        }

        if (!room.hasRights(player.getDetails().getId())) {
            return;
        }

        String content = reader.contents();
        String[] data = content.split(" ");

        int itemId = Integer.parseInt(data[0]);
        Item item = room.getItemManager().getById(itemId);

        if (item == null) {
            return;
        }

        if (item.hasBehaviour(ItemBehaviour.WALL_ITEM)) {
            return;
        }

        int x = Integer.parseInt(data[1]);
        int y = Integer.parseInt(data[2]);
        int rotation = Integer.parseInt(data[3]);

        Position oldPosition = item.getPosition().copy();

        if ((oldPosition.getX() == x &&
            oldPosition.getY() == y &&
            oldPosition.getRotation() == rotation) || !item.isValidMove(item, room, x, y, rotation)) {
            // Send item update even though we cancelled, otherwise the client will be confused.
            player.send(new MOVE_FLOORITEM(item));
            return;
        }

        item.getPosition().setX(x);
        item.getPosition().setY(y);
        item.getPosition().setRotation(rotation);

        boolean isRotation = false;

        if (item.getPosition().getRotation() != oldPosition.getRotation()) {
            isRotation = true;
        }

        room.getMapping().moveItem(item, isRotation, oldPosition);
    }
}
