package org.alexdev.kepler.game.room.tasks;

import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.room.Room;

public class FortuneTask implements Runnable {
    private final Room room;

    public FortuneTask(Room room) {
        this.room = room;
    }

    public void run() {
        for (Item item : this.room.getItems()) {
            if (!item.getDefinition().getSprite().equals("habbowheel")) {
                continue;
            }

            processFortune(item);
        }
    }

    /**
     * Process fortunes which were interacted with
     *
     * @param fortune the fortune being used
     */
    private void processFortune(Item fortune) {
        if (!fortune.getRequiresUpdate()) {
            return;
        }

        fortune.updateStatus();
        fortune.setRequiresUpdate(false);
    }
}
