package org.alexdev.kepler.game.triggers.furniture;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.entities.RoomEntity;
import org.alexdev.kepler.game.room.mapping.RoomTile;
import org.alexdev.kepler.game.triggers.GenericTrigger;

public class PoolQueueTrigger extends GenericTrigger {

    @Override
    public void onEntityStop(Entity entity, RoomEntity roomEntity, Item item, Object... customArgs) {
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player)entity;

        if (player.getDetails().getTickets() == 0 ||
            player.getDetails().getPoolFigure().isEmpty()) {

            int rotation = player.getRoomUser().getPosition().getRotation() / 2 * 2;
            Position temp = new Position(player.getRoomUser().getPosition().getX(), player.getRoomUser().getPosition().getY(), rotation);

            Position[] positionsToCheck = new Position[]{
                    temp.getSquareInFront(),
                    temp.getSquareRight(),
                    temp.getSquareLeft(),
                    temp.getSquareBehind()
            };

            for (var nextPosition : positionsToCheck){
                RoomTile nextTile = player.getRoomUser().getRoom().getMapping().getTile(nextPosition);

                if (nextTile == null) {
                    continue;
                }

                if (nextTile.getHighestItem() == null) {
                    player.getRoomUser().walkTo(nextTile.getPosition().getX(), nextTile.getPosition().getY());
                    break;
                }
            }
        }
    }

    @Override
    public void onEntityLeave(Entity entity, RoomEntity roomEntity, Item item, Object... customArgs) {

    }
}
