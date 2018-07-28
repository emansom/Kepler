package org.alexdev.kepler.game.room.tasks;

import org.alexdev.kepler.dao.mysql.ItemDao;
import org.alexdev.kepler.game.GameScheduler;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.roller.EntityRollingAnalysis;
import org.alexdev.kepler.game.item.roller.ItemRollingAnalysis;
import org.alexdev.kepler.game.item.base.ItemBehaviour;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.log.Log;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class RollerTask implements Runnable {
    private final Room room;
    private final Map<Item, Pair<Item, Position>> itemsRolling;
    private final Map<Entity, Pair<Item, Position>> entitiesRolling;

    public RollerTask(Room room) {
        this.room = room;
        this.itemsRolling = new LinkedHashMap<>();
        this.entitiesRolling = new LinkedHashMap<>();
    }

    @Override
    public void run() {
        this.itemsRolling.clear();
        this.entitiesRolling.clear();

        try {
            ItemRollingAnalysis itemRollingAnalysis = new ItemRollingAnalysis();
            EntityRollingAnalysis entityRollingAnalysis = new EntityRollingAnalysis();

            for (Item roller : this.room.getItems()) {
                if (!roller.hasBehaviour(ItemBehaviour.ROLLER)) {
                    continue;
                }

                // Process items on rollers
                for (Item item : roller.getTile().getItems()) {
                    if (item.hasBehaviour(ItemBehaviour.ROLLER)) {
                        continue;
                    }

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
                itemRollingAnalysis.doRoll(kvp.getKey(),
                        kvp.getValue().getLeft(), this.room, kvp.getKey().getPosition(), kvp.getValue().getRight());
            }

            // Perform roll animation for entity
            for (var kvp : entitiesRolling.entrySet()) {
                entityRollingAnalysis.doRoll(kvp.getKey(),
                        kvp.getValue().getLeft(), this.room, kvp.getKey().getRoomUser().getPosition(), kvp.getValue().getRight());
            }

            if (itemsRolling.size() > 0) {
                this.room.getMapping().regenerateItemCollision();
            }

            if (entitiesRolling.size() > 0) {
                this.room.getMapping().regenerateEntityCollision();
            }

            if (itemsRolling.size() > 0) {
                ItemDao.updateItems(itemsRolling.keySet());

                GameScheduler.getInstance().getSchedulerService().schedule(
                        new RollerCompleteTask(itemsRolling.keySet(), room),
                        1,
                        TimeUnit.SECONDS
                );
            }
        } catch (Exception ex) {
            Log.getErrorLogger().error("RollerTask crashed: ", ex);
        }
    }

    public Item findPotentialRollingItem(Position position) {
        for (Item item : this.itemsRolling.keySet()) {
            if (item.getRollingData() == null) {
                continue;
            }

            if (item.getRollingData().getNextPosition().equals(position) || item.getRollingData().getFromPosition().equals(position)) {
                return item;
            }
        }

        return null;
    }
}
