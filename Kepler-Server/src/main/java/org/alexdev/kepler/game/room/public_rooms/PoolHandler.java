package org.alexdev.kepler.game.room.public_rooms;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.messages.outgoing.rooms.pool.OPEN_UIMAKOPPI;

public class PoolHandler {
    public static void setupRedirections(Room room, Item item) {
        if (item.getDefinition().getSprite().equals("poolBooth")) {
            if (item.getPosition().getX() == 17 && item.getPosition().getY() == 11) {
                room.getMapping().getTile(18, 11).setHighestItem(item);
            }

            if (item.getPosition().getX() == 17 && item.getPosition().getY() == 9) {
                room.getMapping().getTile(18, 9).setHighestItem(item);
            }

            if (item.getPosition().getX() == 8 && item.getPosition().getY() == 1) {
                room.getMapping().getTile(8, 0).setHighestItem(item);
            }

            if (item.getPosition().getX() == 9 && item.getPosition().getY() == 1) {
                room.getMapping().getTile(9, 0).setHighestItem(item);
            }
        }
    }

    public static void interact(Item item, Entity entity) {
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player) entity;

        if (item.getDefinition().getSprite().equals("poolBooth")) {
            player.getRoomUser().setWalkingAllowed(false);
            player.send(new OPEN_UIMAKOPPI(player.getDetails().getPoolFigure()));
        }
    }
}
