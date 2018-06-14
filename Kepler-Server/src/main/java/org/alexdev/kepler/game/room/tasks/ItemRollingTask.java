package org.alexdev.kepler.game.room.tasks;

import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.room.Room;

import java.util.List;

public class ItemRollingTask implements Runnable {
    private final List<Item> rollingItems;
    private final Room room;

    public ItemRollingTask(List<Item> rollingItems, Room room) {
        this.rollingItems = rollingItems;
        this.room = room;
    }

    @Override
    public void run() {
        for (Item item : this.rollingItems) {
            if (item.isRolling()) {
                System.out.println("Set rolling: " + item.getDefinition().getSprite());
                item.setRolling(true);
            }
        }
    }
}
