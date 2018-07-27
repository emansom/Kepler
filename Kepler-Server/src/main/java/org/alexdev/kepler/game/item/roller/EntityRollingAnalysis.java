package org.alexdev.kepler.game.item.roller;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.base.ItemBehaviour;
import org.alexdev.kepler.game.pathfinder.Pathfinder;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.enums.StatusType;
import org.alexdev.kepler.game.room.mapping.RoomTile;
import org.alexdev.kepler.messages.outgoing.rooms.items.SLIDE_OBJECT;

public class EntityRollingAnalysis implements RollingAnalysis<Entity> {
    @Override
    public Position canRoll(Entity entity, Item roller, Room room) {
        if (entity.getRoomUser().isWalking()) {
            System.out.println("lol 12354445");
            return null; // Don't roll user if they're walking.
        }

        if (entity.getRoomUser().getPosition().getZ() < roller.getPosition().getZ()) {
            System.out.println("lol 123 1222");
            return null; // Don't roll user if they're below the roller
        }

        if (!entity.getRoomUser().getPosition().equals(roller.getPosition())) {
            System.out.println("lol 123");
            return null; // Don't roll users who aren't on this tile.
        }

        if (!entity.getRoomUser().getTile().hasWalkableFurni()) {
            System.out.println("l34242 123 1222 33");
            return null; // Don't roll user if they are stuck, let them be unstuck...
        }

        Position front = roller.getPosition().getSquareInFront();
        RoomTile frontTile = room.getMapping().getTile(front);

        if (frontTile == null) {
            System.out.println(" cfffff3");
            return null;
        }

        if (!frontTile.hasWalkableFurni()) {
            System.out.println(" cfffff3 3333434");
            return null;
        }

        if (frontTile.getEntities().size() > 0) {
            for (Entity e : frontTile.getEntities()) {
                if (e.getRoomUser().getRoom() == null) {
                    continue;
                }

                if (e.getRoomUser().isWalking()) {
                    continue;
                }

                if (e.getRoomUser().getPosition().equals(front)) {
                    System.out.println("lol 123 444");
                    return null;
                }
            }
        }

        double nextHeight = entity.getRoomUser().getPosition().getZ();
        boolean subtractRollerHeight = true;

        if (frontTile.getHighestItem() != null) {
            Item frontRoller = null;

            for (Item frontItem : frontTile.getItems()) {
                if (!frontItem.hasBehaviour(ItemBehaviour.ROLLER)) {
                    continue;
                }

                frontRoller = frontItem;
            }

            if (frontRoller != null) {
                subtractRollerHeight = false; // Since we know there's a roller, don't subtract the height.

                if (frontRoller.getPosition().getZ() != roller.getPosition().getZ()) {
                    if (Math.abs(frontRoller.getPosition().getZ() - roller.getPosition().getZ()) > 0.1) {
                        System.out.println("hmm ?");
                        return null; // Don't roll if the height of the roller is different by >0.1
                    }
                }

                for (Item frontItem : frontTile.getItems()) {
                    if (frontItem.getPosition().getZ() < frontRoller.getPosition().getZ()) {
                        continue;
                    }

                    // This is because the ItemRollingAnalysis has setHighestItem in nextTile in doRoll which blocks this
                    if (entity.getRoomUser().getCurrentItem() != null
                            && entity.getRoomUser().getCurrentItem().getId() == frontItem.getId()) {
                        continue;
                    }

                    if (frontItem.hasBehaviour(ItemBehaviour.ROLLER)) {
                        Position frontPosition = frontRoller.getPosition().getSquareInFront();

                        // Don't roll an item into the next roller, if the next roller is facing towards the roller
                        // it just rolled from, and the next roller has an item on it.
                        if (frontPosition.equals(entity.getRoomUser().getPosition())) {
                            if (frontTile.getItems().size() > 1 || frontTile.getEntities().size() > 0) {
                                return null;

                            }
                        }
                    } else {
                        //return null;
                    }
                }
            } else {
                if (!RoomTile.isValidTile(room, entity, frontTile.getPosition())) {
                    return null;
                }
            }
        }

        if (subtractRollerHeight) {
            nextHeight -= roller.getDefinition().getTopHeight();
        }

        return new Position(front.getX(), front.getY(), nextHeight);
    }
    @Override
    public void doRoll(Entity entity, Item roller, Room room, Position fromPosition, Position nextPosition) {
        RoomTile previousTile = room.getMapping().getTile(fromPosition);
        RoomTile nextTile = room.getMapping().getTile(nextPosition);

        // Temporary fix if the user walks on an item and their height gets put up.
        if (entity.getRoomUser().getCurrentItem() != null && entity.getRoomUser().getCurrentItem().hasBehaviour(ItemBehaviour.ROLLER)) {
            if (Math.abs(entity.getRoomUser().getPosition().getZ() - roller.getPosition().getZ()) >= 0.1) {
                if (nextTile.getHighestItem() != null && nextTile.getHighestItem().hasBehaviour(ItemBehaviour.ROLLER)) {
                    nextPosition.setZ(roller.getPosition().getZ() + roller.getDefinition().getTopHeight());
                }
            }
        }

        // The next height but what the client sees.
        double displayNextHeight = nextPosition.getZ();

        if (entity.getRoomUser().isSittingOnGround()) {
            displayNextHeight -= 0.5; // Take away sit offset when sitting on ground, because yeah, weird stuff.
        }

        // Fix bounce for sitting on chairs if the chair top height is higher 1.0
        if (entity.getRoomUser().containsStatus(StatusType.SIT)) {
            double sitHeight = Double.parseDouble(entity.getRoomUser().getStatus(StatusType.SIT).getValue());

            if (sitHeight > 1.0) {
                displayNextHeight += (sitHeight - 1.0); // Add new height offset found.
            }
        }

        room.send(new SLIDE_OBJECT(entity, nextPosition, roller.getId(), displayNextHeight));

        if (!entity.getRoomUser().isSittingOnGround()) {
            entity.getRoomUser().invokeItem(); // Invoke the current tile item if they're not sitting on rollers.
        }

        entity.getRoomUser().getPosition().setX(nextPosition.getX());
        entity.getRoomUser().getPosition().setY(nextPosition.getY());
        entity.getRoomUser().getPosition().setZ(nextPosition.getZ());

        //if (nextTile.hasWalkableFurni() && nextTile.getHighestItem().getDefinition().isChairOrBed()) {
        if (!entity.getRoomUser().isSittingOnGround() && !entity.getRoomUser().isSittingOnChair()) {
            entity.getRoomUser().invokeItem();
        }

        entity.getRoomUser().setNeedsUpdate(true);
    }
}
