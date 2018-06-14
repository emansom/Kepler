package org.alexdev.kepler.game.room.tasks;

import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.room.Room;

import java.util.Collection;
import java.util.List;

public class ItemRollingTask implements Runnable {
    private final Collection<Item> rollingItems;
    private final Room room;

    public ItemRollingTask(Collection<Item> rollingItems, Room room) {
        this.rollingItems = rollingItems;
        this.room = room;
    }

    @Override
    public void run() {
        for (Item item : this.rollingItems) {
            if (item.isRolling()) {
                item.setRolling(false);
            }
        }
    }
}
