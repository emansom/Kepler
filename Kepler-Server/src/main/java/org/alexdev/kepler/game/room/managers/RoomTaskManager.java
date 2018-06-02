package org.alexdev.kepler.game.room.managers;

import org.alexdev.kepler.game.GameScheduler;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.tasks.*;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class RoomTaskManager {
    private Room room;

    private ScheduledFuture<?> scheduledProcessEntity;
    private ScheduledFuture<?> scheduledProcessStatus;
    private ScheduledFuture<?> scheduledProcessRoller;

    public RoomTaskManager(Room room) {
        this.room = room;
    }

    /**
     * Start all needed room tasks, called when there's at least 1 player
     * in the room.
     */
    public void startTasks() {
        // TODO: create scheduler service per room instance so performance of this room won't affect others
        if (this.scheduledProcessEntity == null) {
            this.scheduledProcessEntity = GameScheduler.getInstance().getSchedulerService().scheduleAtFixedRate(new EntityTask(room), 0, 500, TimeUnit.MILLISECONDS);
        }

        if (this.scheduledProcessStatus == null) {
            this.scheduledProcessStatus = GameScheduler.getInstance().getSchedulerService().scheduleAtFixedRate(new StatusTask(room), 0, 1, TimeUnit.SECONDS);
        }

        if (this.scheduledProcessRoller == null) {
            this.scheduledProcessRoller = GameScheduler.getInstance().getSchedulerService().scheduleAtFixedRate(new RollerTask(room), 0, 3, TimeUnit.SECONDS);
        }
    }

    /**
     * Stop tasks, called when there's no players in the room.
     */
    public void stopTasks() {
        if (this.scheduledProcessEntity != null) {
            this.scheduledProcessEntity.cancel(false);
            this.scheduledProcessEntity = null;
        }

        if (this.scheduledProcessStatus != null) {
            this.scheduledProcessStatus.cancel(false);
            this.scheduledProcessStatus = null;
        }

        if (this.scheduledProcessRoller != null) {
            this.scheduledProcessRoller.cancel(false);
            this.scheduledProcessRoller = null;
        }
    }
}
