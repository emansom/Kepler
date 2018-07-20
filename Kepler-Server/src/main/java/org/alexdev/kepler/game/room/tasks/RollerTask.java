package org.alexdev.kepler.game.room.tasks;

import org.alexdev.kepler.dao.mysql.ItemDao;
import org.alexdev.kepler.game.GameScheduler;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.roller.EntityRollingAnalysis;
import org.alexdev.kepler.game.item.roller.ItemRollingAnalysis;
import org.alexdev.kepler.game.item.roller.RollingData;
import org.alexdev.kepler.game.item.base.ItemBehaviour;
import org.alexdev.kepler.game.pathfinder.Pathfinder;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.mapping.RoomTile;
import org.alexdev.kepler.messages.outgoing.rooms.items.SLIDE_OBJECT;
import org.alexdev.kepler.util.config.GameConfiguration;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class RollerTask implements Runnable {
    private final Room room;

    public RollerTask(Room room) {
        this.room = room;
    }

    @Override
    public void run() {
        Map<Item, Pair<Item, Position>> itemsRolling = new LinkedHashMap<>();
        Map<Entity, Pair<Item, Position>> entitiesRolling = new LinkedHashMap<>();

        ItemRollingAnalysis itemRollingAnalysis = new ItemRollingAnalysis();
        EntityRollingAnalysis entityRollingAnalysis = new EntityRollingAnalysis();

        for (Item roller : this.room.getItems()) {
            if (!roller.hasBehaviour(ItemBehaviour.ROLLER)) {
                continue;
            }

            // Process items on rollers
            for (Item item : roller.getTile().getItems()) {
                if (itemsRolling.containsKey(item)) {
                    continue;
                }

                Position nextPosition = itemRollingAnalysis.canRoll(item, roller, this.room);

                if (nextPosition != null) {
                    itemsRolling.put(item, Pair.of(roller, nextPosition));
                }
            }

            // Process entities on rollers
            for (Entity entity : roller.getTile().getEntities()) {
                if (entitiesRolling.containsKey(entity)) {
                    continue;
                }

                Position nextPosition = entityRollingAnalysis.canRoll(entity, roller, this.room);

                if (nextPosition != null) {
                    entitiesRolling.put(entity, Pair.of(roller, nextPosition));
                }
            }
        }

        // Perform roll animation for item
        for (var kvp : itemsRolling.entrySet()) {
            itemRollingAnalysis.doRoll(kvp.getKey(), kvp.getValue().getLeft(), this.room, kvp.getValue().getRight());
        }

        // Perform roll animation for entity
        for (var kvp : entitiesRolling.entrySet()) {
            entityRollingAnalysis.doRoll(kvp.getKey(), kvp.getValue().getLeft(), this.room, kvp.getValue().getRight());
        }

        if (itemsRolling.size() > 0) {
            ItemDao.updateItems(itemsRolling.keySet());
            this.room.getMapping().regenerateCollisionMap();

            GameScheduler.getInstance().getSchedulerService().schedule(
                    new ItemRollingTask(itemsRolling.keySet(), room),
                    1,
                    TimeUnit.SECONDS
            );
        }
    }
}
