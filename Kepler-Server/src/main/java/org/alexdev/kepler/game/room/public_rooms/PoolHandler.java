package org.alexdev.kepler.game.room.public_rooms;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.mapping.RoomTile;
import org.alexdev.kepler.messages.outgoing.rooms.items.SHOWPROGRAM;
import org.alexdev.kepler.messages.outgoing.rooms.pool.OPEN_UIMAKOPPI;
import org.alexdev.kepler.util.StringUtil;

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
            item.showProgram("close");
            player.getRoomUser().setWalkingAllowed(false);
            player.send(new OPEN_UIMAKOPPI());
        }
    }

    public static void exitBooth(Player player) {
        RoomTile tile = player.getRoomUser().getTile();
        Room room = player.getRoomUser().getRoom();

        if (tile == null || tile.getHighestItem() == null || room == null) {
            return;
        }

        if (!tile.getHighestItem().getDefinition().getSprite().equals("poolBooth")) {
            return;
        }

        if (!room.getData().getModel().getModelName().equals("pool_a") &&
            !room.getData().getModel().getModelName().equals("md_a")) {
            return;
        }

        tile.getHighestItem().showProgram("open");
        player.getRoomUser().setWalkingAllowed(true);

        if (room.getData().getModel().getModelName().equals("pool_a")) {
            if (player.getRoomUser().getPosition().getY() == 11) {
                player.getRoomUser().walkTo(19, 11);
            }

            if (player.getRoomUser().getPosition().getY() == 9) {
                player.getRoomUser().walkTo(19, 9);
            }
        }

        if (room.getData().getModel().getModelName().equals("md_a")) {
            if (player.getRoomUser().getPosition().getX() == 8) {
                player.getRoomUser().walkTo(8, 2);
            }

            if (player.getRoomUser().getPosition().getX() == 9) {
                player.getRoomUser().walkTo(9, 9);
            }
        }
    }

    public static void disconnect(Player player) {
        RoomTile tile = player.getRoomUser().getTile();
        Room room = player.getRoomUser().getRoom();

        if (tile == null || tile.getHighestItem() == null || room == null) {
            return;
        }

        Item item = tile.getHighestItem();

        if (item.getDefinition().getSprite().equals("poolBooth") ||
            item.getDefinition().getSprite().equals("poolLift")) {
            item.showProgram("open");
        }
    }
}
