package org.alexdev.kepler.game.room.managers;

import org.alexdev.kepler.game.GameScheduler;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.tasks.ProcessEntityTask;
import org.alexdev.kepler.messages.outgoing.rooms.ROOM_READY;
import org.alexdev.kepler.messages.outgoing.rooms.ROOM_URL;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class RoomTaskManager {
    private Room room;

    private ProcessEntityTask processEntity;
    private ScheduledFuture<?> scheduledProcessEntity;

    public RoomTaskManager(Room room) {
        this.room = room;
        this.processEntity = new ProcessEntityTask(room);
    }

    /**
     * Start all needed room tasks, called when there's at least 1 player
     * in the room.
     */
    public void startTasks() {
        if (this.scheduledProcessEntity == null) {
            this.scheduledProcessEntity = GameScheduler.getInstance().getScheduler().scheduleAtFixedRate(
                    this.processEntity, 0, 500, TimeUnit.MILLISECONDS);
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
    }
}
