package org.alexdev.kepler.game.item.triggers.types;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.triggers.ItemTrigger;
import org.alexdev.kepler.game.room.RoomUser;
import org.alexdev.kepler.game.room.enums.StatusType;
import org.alexdev.kepler.util.StringUtil;

public class ChairTrigger implements ItemTrigger {
    @Override
    public void onEntityStep(Entity entity, RoomUser roomUser, Item item, Object... customArgs) {

    }

    @Override
    public void onEntityStop(Entity entity, RoomUser roomUser, Item item, Object... customArgs) {
        boolean isRolling = false;

        if (customArgs.length > 0) {
            isRolling = (boolean)customArgs[0];
        }

        int headRotation = roomUser.getPosition().getHeadRotation();
        roomUser.getPosition().setRotation(item.getPosition().getRotation());

        roomUser.removeStatus(StatusType.DANCE);
        roomUser.setStatus(StatusType.SIT, StringUtil.format(item.getDefinition().getTopHeight()));

        if (isRolling) {
            if (roomUser.getTimerManager().getLookTimer() > -1) {
                roomUser.getPosition().setHeadRotation(headRotation);
            }
        }

        roomUser.setNeedsUpdate(true);
    }
}
