package org.alexdev.kepler.game.room.tasks;

import org.alexdev.kepler.dao.mysql.ItemDao;
import org.alexdev.kepler.game.item.Item;

public class FortuneTask implements Runnable {
    private final Item fortune;

    public FortuneTask(Item item) {
        this.fortune = item;
    }

    public void run() {
        if (!fortune.getRequiresUpdate()) {
            return;
        }

        fortune.updateStatus();
        fortune.setRequiresUpdate(false);

        ItemDao.updateItem(fortune);
    }
}
