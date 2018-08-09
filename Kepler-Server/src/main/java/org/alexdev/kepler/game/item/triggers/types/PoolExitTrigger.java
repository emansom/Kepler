package org.alexdev.kepler.game.item.triggers.types;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.triggers.ItemTrigger;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.room.RoomUser;
import org.alexdev.kepler.game.room.public_rooms.PoolHandler;

public class PoolExitTrigger implements ItemTrigger {
    @Override
    public void onEntityStep(Entity entity, RoomUser roomUser, Item item, Object... customArgs) {

    }

    @Override
    public void onEntityStop(Entity entity, RoomUser roomUser, Item item, Object... customArgs) {
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }

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
            PoolHandler.warpSwim(item, entity, warp, goal, true);
        }
    }
}
