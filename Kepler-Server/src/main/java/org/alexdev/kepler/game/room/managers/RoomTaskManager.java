package org.alexdev.kepler.game.room.managers;

import org.alexdev.kepler.game.GameScheduler;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.tasks.ProcessEntityTask;
import org.alexdev.kepler.game.room.tasks.ProcessRollerTask;
import org.alexdev.kepler.game.room.tasks.ProcessStatusTask;
import org.alexdev.kepler.messages.outgoing.rooms.ROOM_READY;
import org.alexdev.kepler.messages.outgoing.rooms.ROOM_URL;

import java.util.ArrayList;
import java.util.List;
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
            this.scheduledProcessEntity = GameScheduler.getInstance().getScheduler().scheduleAtFixedRate(
                    new ProcessEntityTask(room), 0, 500, TimeUnit.MILLISECONDS);
        }

        if (this.scheduledProcessStatus == null) {
            this.scheduledProcessStatus = GameScheduler.getInstance().getScheduler().scheduleAtFixedRate(
                    new ProcessStatusTask(room), 0, 1, TimeUnit.SECONDS);
        }

        if (this.scheduledProcessRoller == null) {
            this.scheduledProcessRoller = GameScheduler.getInstance().getScheduler().scheduleAtFixedRate(
                    new ProcessRollerTask(room), 0, 3, TimeUnit.SECONDS);
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
