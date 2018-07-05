package org.alexdev.kepler.game.item.roller;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.base.ItemBehaviour;
import org.alexdev.kepler.game.pathfinder.Pathfinder;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.mapping.RoomTile;
import org.alexdev.kepler.messages.outgoing.rooms.items.SLIDE_OBJECT;

public class EntityRollingAnalysis implements RollingAnalysis<Entity> {
    @Override
    public Position canRoll(Entity entity, Item roller, Room room) {
        if (entity.getRoomUser().isWalking()) {
            return null; // Don't roll user if they're working.
        }

        if (!entity.getRoomUser().getPosition().equals(roller.getPosition())) {
            return null; // Don't roll users who aren't on this tile.
        }

        if (entity.getRoomUser().getPosition().getZ() < roller.getPosition().getZ()) {
            return null; // Don't roll user if they're below the roller
        }

        Position front = roller.getPosition().getSquareInFront();

        if (!Pathfinder.isValidStep(room, entity, entity.getRoomUser().getPosition(), front, true)) {
            return null;
        }

        RoomTile frontTile = room.getMapping().getTile(front.getX(), front.getY());
        double nextHeight = frontTile.getInteractiveTileHeight();

        if (frontTile.getHighestItem() != null) {
            Item frontRoller = null;

            for (Item frontItem : frontTile.getItems()) {
                if (!frontItem.hasBehaviour(ItemBehaviour.ROLLER)) {
                    continue;
                }

                frontRoller = frontItem;
            }

            if (frontRoller != null) {
                for (Item frontItem : frontTile.getItems()) {
                    if (frontItem.hasBehaviour(ItemBehaviour.ROLLER)) {
                        continue;
                    }

                    if (frontItem.getPosition().getZ() < frontRoller.getPosition().getZ()) {
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
                        return null;
                    }
                }
            }
        }

        return new Position(front.getX(), front.getY(), nextHeight);
    }

    @Override
    public void doRoll(Entity entity, Item roller, Room room, Position nextPosition) {
        RoomTile nextTile = room.getMapping().getTile(nextPosition);
        RoomTile previousTile = room.getMapping().getTile(entity.getRoomUser().getPosition().getX(), entity.getRoomUser().getPosition().getY());

        double displayNextHeight = nextPosition.getZ();

        if (entity.getRoomUser().isSittingOnGround()) {
            displayNextHeight -= 0.5; // Take away sit offset because yeah, weird stuff.
        }

        room.send(new SLIDE_OBJECT(entity, nextPosition, roller.getId(), displayNextHeight));

        if (!entity.getRoomUser().isSittingOnGround()) {
            entity.getRoomUser().invokeItem(); // Invoke the current tile item if they're not sitting on rollers.
        }

        entity.getRoomUser().getPosition().setX(nextPosition.getX());
        entity.getRoomUser().getPosition().setY(nextPosition.getY());
        entity.getRoomUser().getPosition().setZ(nextPosition.getZ());
        entity.getRoomUser().setNeedsUpdate(true);

        previousTile.removeEntity(entity);
        nextTile.addEntity(entity);
    }
}
