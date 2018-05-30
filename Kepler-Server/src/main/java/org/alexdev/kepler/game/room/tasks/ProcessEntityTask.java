package org.alexdev.kepler.game.room.tasks;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.room.enums.StatusType;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.pathfinder.Rotation;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.RoomUser;
import org.alexdev.kepler.game.room.mapping.RoomTile;
import org.alexdev.kepler.messages.outgoing.rooms.user.USER_STATUSES;
import org.alexdev.kepler.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class ProcessEntityTask implements Runnable {
    private final Room room;

    public ProcessEntityTask(Room room) {
        this.room = room;
    }

    @Override
    public void run() {
        if (this.room.getEntities().size() == 0) {
            return;
        }

        List<Entity> entitiesToUpdate = new ArrayList<>();

        for (Entity entity : this.room.getEntities()) {
            if (entity != null && entity.getRoomUser().getRoom() != null && entity.getRoomUser().getRoom() == this.room) {
                this.processEntity(entity);
                RoomUser roomEntity = entity.getRoomUser();

                if (roomEntity.isNeedsUpdate()) {
                    roomEntity.setNeedsUpdate(false);
                    entitiesToUpdate.add(entity);
                }
            }
        }

        if (entitiesToUpdate.size() > 0) {
            this.room.send(new USER_STATUSES(entitiesToUpdate));
        }
    }

    /**
     * Process entity.
     *
     * @param entity the entity
     */
    private void processEntity(Entity entity) {
        RoomUser roomUser = entity.getRoomUser();

        Position position = roomUser.getPosition();
        Position goal = roomUser.getGoal();

        if (roomUser.isWalking()) {
            // Apply next tile from the tile we removed from the list the cycle before
            if (roomUser.getNextPosition() != null) {
                roomUser.getPosition().setX(roomUser.getNextPosition().getX());
                roomUser.getPosition().setY(roomUser.getNextPosition().getY());
                roomUser.updateNewHeight(roomUser.getNextPosition());
            }

            // We still have more tiles left, so lets continue moving
            if (roomUser.getPath().size() > 0) {
                Position next = roomUser.getPath().pop();

                // Tile was invalid after we started walking, so lets try again!
                if (!RoomTile.isValidTile(this.room, entity, next)) {
                    roomUser.walkTo(goal.getX(), goal.getY());
                    this.processEntity(entity);
                    return;
                }

                // Leave room if the tile is the door and we are in a flat
                // TODO: disable if in public room with teleporting tiles
                if (next.equals(roomUser.getRoom().getModel().getDoorLocation())) {
                    roomUser.getRoom().getEntityManager().leaveRoom(entity, true);
                    return;
                }

                RoomTile previousTile = roomUser.getTile();
                RoomTile nextTile = roomUser.getRoom().getMapping().getTile(next);

                previousTile.removeEntity(entity);
                nextTile.addEntity(entity);

                roomUser.removeStatus(StatusType.LAY);
                roomUser.removeStatus(StatusType.SIT);
                roomUser.setSitCommand(false);

                int rotation = Rotation.calculateWalkDirection(position.getX(), position.getY(), next.getX(), next.getY());
                double height = this.room.getMapping().getTile(next).getTileHeight();

                roomUser.getPosition().setRotation(rotation);
                roomUser.setStatus(StatusType.MOVE, next.getX() + "," + next.getY() + "," + StringUtil.format(height));
                roomUser.setNextPosition(next);
            } else {
                roomUser.stopWalking();
            }

            // If we're walking, make sure to tell the server
            roomUser.setNeedsUpdate(true);
        }
    }
}
