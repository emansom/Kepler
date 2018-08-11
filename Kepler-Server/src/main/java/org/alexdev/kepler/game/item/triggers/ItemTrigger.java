package org.alexdev.kepler.game.item.triggers;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.room.RoomUser;

public interface ItemTrigger {
    void onEntityStep(Entity entity, RoomUser roomUser, Item item, Object... customArgs);
    void onEntityStop(Entity entity, RoomUser roomUser, Item item, Object... customArgs);
    void onEntityLeave(Entity entity, RoomUser roomUser, Item item, Object... customArgs);
}
