package org.alexdev.kepler.game.triggers;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.room.entities.RoomEntity;

public interface GenericTrigger {
    void onEntityStep(Entity entity, RoomEntity roomEntity, Item item, Object... customArgs);
    void onEntityStop(Entity entity, RoomEntity roomEntity, Item item, Object... customArgs);
    void onEntityLeave(Entity entity, RoomEntity roomEntity, Item item, Object... customArgs);
}
