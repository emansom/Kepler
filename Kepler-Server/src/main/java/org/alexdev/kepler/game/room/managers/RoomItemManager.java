package org.alexdev.kepler.game.room.managers;

import org.alexdev.kepler.game.GameScheduler;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.mapping.RoomMapping;
import org.alexdev.kepler.messages.outgoing.rooms.ROOM_READY;
import org.alexdev.kepler.messages.outgoing.rooms.ROOM_URL;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class RoomItemManager {
    private Room room;

    public RoomItemManager(Room room) {
        this.room = room;
    }

    public Item getById(int itemId) {
        for (Item item : this.room.getItems()) {
            if (item.getId() == itemId) {
                return item;
            }
        }

        return null;
    }

    public List<Item> getPublicItems() {
        List<Item> items = new ArrayList<>();

        for (Item item : this.room.getItems()) {
            if (!item.getDefinition().getBehaviour().isPublicSpaceObject()) {
                continue;
            }

            items.add(item);
        }

        return items;
    }

    public List<Item> getFloorItems() {
        List<Item> items = new ArrayList<>();

        for (Item item : this.room.getItems()) {
            if (item.getDefinition().getBehaviour().isPublicSpaceObject()) {
                continue;
            }

            if (item.getDefinition().getBehaviour().isWallItem()) {
                continue;
            }

            items.add(item);
        }

        return items;
    }

    public List<Item> getWallItems() {
        List<Item> items = new ArrayList<>();

        for (Item item : this.room.getItems()) {
            if (item.getDefinition().getBehaviour().isPublicSpaceObject()) {
                continue;
            }

            if (!item.getDefinition().getBehaviour().isWallItem()) {
                continue;
            }

            items.add(item);
        }

        return items;
    }
}