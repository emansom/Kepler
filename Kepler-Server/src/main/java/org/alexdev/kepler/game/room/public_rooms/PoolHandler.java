package org.alexdev.kepler.game.room.public_rooms;

import org.alexdev.kepler.dao.mysql.CurrencyDao;
import org.alexdev.kepler.dao.mysql.PlayerDao;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.player.PlayerDetails;
import org.alexdev.kepler.game.room.enums.StatusType;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.RoomUser;
import org.alexdev.kepler.game.room.mapping.RoomTile;
import org.alexdev.kepler.messages.outgoing.rooms.pool.JUMPINGPLACE_OK;
import org.alexdev.kepler.messages.outgoing.rooms.pool.OPEN_UIMAKOPPI;
import org.alexdev.kepler.messages.outgoing.user.TICKET_BALANCE;

import java.util.Currency;

public class PoolHandler {

    /**
     * Setup booth coordinate registration in multiple areas of the map.
     * Used for both standing in the booth, and the curtain.
     *
     * @param room the room to setup the booth for
     * @param item the item to to set
     */
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

    /**
     * Interact with a pool item.
     *
     * @param item the item to handle
     * @param entity the entity to handles
     */
    public static void interact(Item item, Entity entity) {
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player) entity;

        if (item.getDefinition().getSprite().equals("poolLift")) {
            item.showProgram("close");

            player.getRoomUser().setWalkingAllowed(false);
            player.getRoomUser().resetRoomTimer(60); // Only allow 60 seconds when diving, to stop the queue from piling up if someone goes AFK.
            player.getRoomUser().setDiving(true);

            player.send(new TICKET_BALANCE(player.getDetails().getTickets()));
            player.send(new JUMPINGPLACE_OK());

            CurrencyDao.decreaseTickets(player.getDetails(), 1);
            player.send(new TICKET_BALANCE(player.getDetails().getTickets()));
        }

        if (item.getDefinition().getSprite().equals("poolBooth")) {
            item.showProgram("close");

            player.getRoomUser().setWalkingAllowed(false);
            player.getRoomUser().resetRoomTimer(60); // Only allow 60 seconds when changing clothes, to stop someone from just afking in the booth for 15 minutes.
            player.send(new OPEN_UIMAKOPPI());
        }

        if (item.getDefinition().getSprite().equals("poolEnter")) {
            Position warp = null;
            Position goal = null;

            if (item.getPosition().getX() == 20 && item.getPosition().getY() == 28) {
                warp = new Position(21, 28);
                goal = new Position(22, 28);
            }

            if (item.getPosition().getX() == 17 && item.getPosition().getY() == 21) {
                warp = new Position(16, 22);
                goal = new Position(16, 23);
            }

            if (item.getPosition().getX() == 31 && item.getPosition().getY() == 10) {
                warp = new Position(30, 11);
                goal = new Position(30, 12);
            }

            if (warp != null) {
                warpSwim(item, entity, warp, goal, false);
            }
        }

        if (item.getDefinition().getSprite().equals("poolExit")) {
            Position warp = null;
            Position goal = null;

            if (item.getPosition().getX() == 21 && item.getPosition().getY() == 28) {
                warp = new Position(20, 28);
                goal = new Position(19, 28);
            }

            if (item.getPosition().getX() == 17 && item.getPosition().getY() == 22) {
                warp = new Position(17, 21);
                goal = new Position(17, 20);
            }

            if (item.getPosition().getX() == 31 && item.getPosition().getY() == 11) {
                warp = new Position(31, 10);
                goal = new Position(31, 9);
            }

            if (item.getPosition().getX() == 20 && item.getPosition().getY() == 19) {
                warp = new Position(19, 19);
                goal = new Position(18, 19);
            }

            if (warp != null) {
                warpSwim(item, entity, warp, goal, true);
            }
        }
    }

    /**
     * Warps the player to a location fluidly with splashing.
     *
     * @param item the item, it's either a poolExit or poolEnter
     * @param entity the entity to warp
     * @param warp the warp location
     * @param goal the goal location to swim to
     * @param exit whether it was exiting or entering the ladder, to add or remove swimming
     */
    private static void warpSwim(Item item, Entity entity, Position warp, Position goal, boolean exit) {
        RoomUser roomUser = entity.getRoomUser();
        Room room = entity.getRoom();

        if (exit) {
            roomUser.removeStatus(StatusType.SWIM);
        } else {
            roomUser.setStatus(StatusType.SWIM, "");
        }

        roomUser.setNextPosition(new Position(warp.getX(), warp.getY(), room.getMapping().getTile(warp).getTileHeight()));
        roomUser.getPath().clear();
        roomUser.getPath().add(goal);
        roomUser.setWalking(true);

        item.showProgram(null);
    }

    /**
     * Called when a player exits a changing booth, it will automatically
     * make the player leave the booth.
     *
     * @param player the player to leave
     */
    public static void exitBooth(Player player) {
        RoomTile tile = player.getRoomUser().getTile();
        Room room = player.getRoom();

        if (tile == null || tile.getHighestItem() == null || room == null) {
            return;
        }

        if (!tile.getHighestItem().getDefinition().getSprite().equals("poolBooth")) {
            return;
        }

        if (!room.getModel().getName().equals("pool_a") &&
            !room.getModel().getName().equals("md_a")) {
            return;
        }

        tile.getHighestItem().showProgram("open");
        player.getRoomUser().setWalkingAllowed(true);

        if (room.getData().getModel().equals("pool_a")) {
            if (player.getRoomUser().getPosition().getY() == 11) {
                player.getRoomUser().walkTo(19, 11);
            }

            if (player.getRoomUser().getPosition().getY() == 9) {
                player.getRoomUser().walkTo(19, 9);
            }
        }

        if (room.getData().getModel().equals("md_a")) {
            if (player.getRoomUser().getPosition().getX() == 8) {
                player.getRoomUser().walkTo(8, 2);
            }

            if (player.getRoomUser().getPosition().getX() == 9) {
                player.getRoomUser().walkTo(9, 9);
            }
        }
    }

    /**
     * Handle item program when player disconnects or leaves room.
     * Will re-open up pool lift or the changing booths.
     *
     * @param player the player to handle
     */
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

    public static void checkPoolQueue(Entity entity) {
        if (entity.getRoomUser().isWalking()) {
            return;
        }

        if (entity.getRoomUser().getCurrentItem() != null) {
            if (entity.getRoomUser().getCurrentItem().getDefinition().getSprite().equals("queue_tile2")) {
                Position front =  entity.getRoomUser().getCurrentItem().getPosition().getSquareInFront();
                entity.getRoomUser().walkTo(front.getX(), front.getY());
            }
        }
    }
}
