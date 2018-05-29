package org.alexdev.kepler.game.room.managers;

import org.alexdev.kepler.game.GameScheduler;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.tasks.ProcessEntityTask;
import org.alexdev.kepler.game.room.tasks.ProcessRollerTask;
import org.alexdev.kepler.game.room.tasks.ProcessStatusTask;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class RoomTaskManager {
    private Room room;

    private ScheduledFuture<?> scheduledProcessEntity;
    private ScheduledFuture<?> scheduledProcessStatus;;
    private ScheduledFuture<?> scheduledProcessRoller;

    public RoomTaskManager(Room room) {
        this.room = room;
    }

    /**
     * Start all needed room tasks, called when there's at least 1 player
     * in the room.
     */
    public void startTasks() {
        if (this.scheduledProcessEntity == null) {
            this.scheduledProcessEntity = GameScheduler.getInstance().getSchedulerService().scheduleAtFixedRate(new ProcessEntityTask(room), 0, 500, TimeUnit.MILLISECONDS);
        }

        if (this.scheduledProcessStatus == null) {
            this.scheduledProcessStatus = GameScheduler.getInstance().getSchedulerService().scheduleAtFixedRate(new ProcessStatusTask(room), 0, 1, TimeUnit.SECONDS);
        }

        if (this.scheduledProcessRoller == null) {
            this.scheduledProcessRoller = GameScheduler.getInstance().getSchedulerService().scheduleAtFixedRate(new ProcessRollerTask(room), 0, 3, TimeUnit.SECONDS);
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
