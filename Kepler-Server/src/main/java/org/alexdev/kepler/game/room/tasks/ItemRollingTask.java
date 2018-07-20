package org.alexdev.kepler.game.room.tasks;

import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.messages.outgoing.rooms.items.MOVE_FLOORITEM;

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
            if (item.getRollingData() == null) {
                continue;
            }

            if (item.getRollingData().getHeightUpdate() > 0) {
                item.getPosition().setZ(item.getPosition().getZ() + item.getRollingData().getHeightUpdate());
                this.room.send(new MOVE_FLOORITEM(item));
            }

            item.setRollingData(null);
        }
    }
}
