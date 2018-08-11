package org.alexdev.kepler.game.item.triggers.types;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.triggers.ItemTrigger;
import org.alexdev.kepler.game.room.RoomUser;
import org.alexdev.kepler.game.room.enums.StatusType;
import org.alexdev.kepler.util.StringUtil;

public class BedTrigger implements ItemTrigger {
    @Override
    public void onEntityStep(Entity entity, RoomUser roomUser, Item item, Object... customArgs) {

    }

    @Override
    public void onEntityStop(Entity entity, RoomUser roomUser, Item item, Object... customArgs) {
        roomUser.removeStatus(StatusType.CARRY_ITEM);
        roomUser.removeStatus(StatusType.CARRY_FOOD);
        roomUser.removeStatus(StatusType.CARRY_DRINK);
        roomUser.removeStatus(StatusType.DANCE);

        roomUser.getPosition().setRotation(item.getPosition().getRotation());
        roomUser.setStatus(StatusType.LAY, StringUtil.format(item.getDefinition().getTopHeight()));
        roomUser.setNeedsUpdate(true);
    }

    @Override
    public void onEntityLeave(Entity entity, RoomUser roomUser, Item item, Object... customArgs) {

    }
}
