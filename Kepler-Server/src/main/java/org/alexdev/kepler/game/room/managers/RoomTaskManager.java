package org.alexdev.kepler.game.room.managers;

import org.alexdev.kepler.game.GameScheduler;
import org.alexdev.kepler.game.item.base.ItemBehaviour;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.tasks.*;
import org.alexdev.kepler.util.config.GameConfiguration;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class RoomTaskManager {
    private Room room;
    private ScheduledExecutorService scheduledExecutorService;

    private ScheduledFuture<?> processEntity;
    private ScheduledFuture<?> processStatus;
    private ScheduledFuture<?> processRoller;

    public RoomTaskManager(Room room) {
        this.room = room;
        this.scheduledExecutorService = GameScheduler.getInstance().getSchedulerService();
    }

    /**
     * Start all needed room tasks, called when there's at least 1 player
     * in the room.
     */
    public void startTasks() {
        // TODO: create scheduler service per room instance so performance of this room won't affect others
        if (this.processEntity == null) {
            this.processEntity = this.scheduledExecutorService.scheduleAtFixedRate(new EntityTask(room), 0, 500, TimeUnit.MILLISECONDS);
        }

        if (this.processStatus == null) {
            this.processStatus = this.scheduledExecutorService.scheduleAtFixedRate(new StatusTask(room), 0, 1, TimeUnit.SECONDS);
        }

        if (this.room.getItemManager().containsItemBehaviour(ItemBehaviour.ROLLER)) {
            int rollerMillisTask = GameConfiguration.getInstance().getInteger("roller.tick.default");

            if (this.processRoller == null) {
                this.processRoller = this.scheduledExecutorService.scheduleAtFixedRate(new RollerTask(room), 0, rollerMillisTask, TimeUnit.MILLISECONDS);
            }
        }
    }

    /**
     * Stop tasks, called when there's no players in the room.
     */
    public void stopTasks() {
        if (this.processEntity != null) {
            this.processEntity.cancel(false);
            this.processEntity = null;
        }

        if (this.processStatus != null) {
            this.processStatus.cancel(false);
            this.processStatus = null;
        }

        if (this.processRoller != null) {
            this.processRoller.cancel(false);
            this.processRoller = null;
        }
    }
}
