package org.alexdev.kepler.game.triggers.furniture;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.room.entities.RoomEntity;
import org.alexdev.kepler.game.triggers.GenericTrigger;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.room.enums.StatusType;
import org.alexdev.kepler.game.room.mapping.RoomTile;
import org.alexdev.kepler.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class BedTrigger implements GenericTrigger {
    @Override
    public void onEntityStep(Entity entity, RoomEntity roomEntity, Item item, Object... customArgs) {

    }

    @Override
    public void onEntityStop(Entity entity, RoomEntity roomEntity, Item item, Object... customArgs) {
        if (!this.isValidPillowTile(item, roomEntity.getPosition())) {
            for (Position tile : this.getValidPillowTiles(item)) {
                if (!RoomTile.isValidTile(roomEntity.getRoom(), entity, tile)) {
                    continue;
                }

                if (item.getPosition().getRotation() == 0) {
                    entity.getRoomUser().getPosition().setY(tile.getY());

                } else {
                    entity.getRoomUser().getPosition().setX(tile.getX());
                }

                break;
            }
        }

        if (this.isValidPillowTile(item, roomEntity.getPosition())) {
            roomEntity.removeStatus(StatusType.CARRY_ITEM);
            roomEntity.removeStatus(StatusType.CARRY_FOOD);
            roomEntity.removeStatus(StatusType.CARRY_DRINK);
            roomEntity.removeStatus(StatusType.DANCE);

            roomEntity.getPosition().setRotation(item.getPosition().getRotation());
            roomEntity.setStatus(StatusType.LAY, StringUtil.format(item.getDefinition().getTopHeight()));
        }

        roomEntity.setNeedsUpdate(true);
    }

    @Override
    public void onEntityLeave(Entity entity, RoomEntity roomEntity, Item item, Object... customArgs) {

    }

    /**
     * Validates if the users tile is a valid pillow tile on a bed.
     *
     * @param item the bed to check for
     * @param entityPosition the entity position to check against
     * @return true, if successful
     */
    public boolean isValidPillowTile(Item item, Position entityPosition) {
        if (entityPosition.equals(item.getPosition())) {
            return true;
        } else {
            for (Position validTile : getValidPillowTiles(item)) {
                if (validTile.equals(entityPosition)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Gets the valid pillow tile list for a bed.
     *
     * @param item the item to check for
     * @return the list of valid coordinates
     */
    public List<Position> getValidPillowTiles(Item item) {
        List<Position> tiles = new ArrayList<>();
        tiles.add(new Position(item.getPosition().getX(), item.getPosition().getY()));

        int validPillowX = -1;
        int validPillowY = -1;

        if (item.getPosition().getRotation() == 0) {
            validPillowX = item.getPosition().getX() + 1;
            validPillowY = item.getPosition().getY();
        }

        if (item.getPosition().getRotation() == 2) {
            validPillowX = item.getPosition().getX();
            validPillowY = item.getPosition().getY() + 1;
        }

        tiles.add(new Position(validPillowX, validPillowY));
        return tiles;
    }
}
