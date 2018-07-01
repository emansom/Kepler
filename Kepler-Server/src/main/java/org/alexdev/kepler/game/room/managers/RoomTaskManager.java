package org.alexdev.kepler.game.room.managers;

import org.alexdev.kepler.game.GameScheduler;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.tasks.EntityTask;
import org.alexdev.kepler.game.room.tasks.RollerTask;
import org.alexdev.kepler.game.room.tasks.StatusTask;
import org.alexdev.kepler.util.config.GameConfiguration;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class RoomTaskManager {
    private Room room;
    private ScheduledExecutorService scheduledExecutorService;

    private ScheduledFuture<?> processEntity;
    private ScheduledFuture<?> processStatus;
    private ScheduledFuture<?> processRoller;
    private HashMap<String, Pair<ScheduledFuture<?>, Runnable>> processCustomTasks;

    public RoomTaskManager(Room room) {
        this.room = room;
        this.processCustomTasks = new HashMap<>();
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

        //if (this.room.getItemManager().containsItemBehaviour(ItemBehaviour.ROLLER)) {
        int rollerMillisTask = GameConfiguration.getInstance().getInteger("roller.tick.default");

        if (this.processRoller == null) {
            this.processRoller = this.scheduledExecutorService.scheduleAtFixedRate(new RollerTask(room), 0, rollerMillisTask, TimeUnit.MILLISECONDS);
        }
        //}
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

        for (var tasksKvp : this.processCustomTasks.entrySet()) {
            tasksKvp.getValue().getLeft().cancel(false);
        }

        this.processCustomTasks.clear();
    }

    /**
     * Schedule a custom task.
     *
     * @param taskName the task name identifier
     * @param runnableTask the runnable task instance
     * @param interval the interval of the task
     * @param timeUnit the time unit of the interval
     */
    public void scheduleCustomTask(String taskName, Runnable runnableTask, int interval, TimeUnit timeUnit) {
        if (processCustomTasks.containsKey(taskName)) {
            this.processCustomTasks.get(taskName).getLeft().cancel(false);
            this.processCustomTasks.remove(taskName);
        }

        var future = this.scheduledExecutorService.scheduleAtFixedRate(runnableTask, 0, interval, timeUnit);

        this.processCustomTasks.put(taskName, Pair.of(
                future,
                runnableTask
        ));
    }

    /**
     * Cancels a custom task by task name.
     *
     * @param taskName the name of the task to cancel
     */
    public void cancelCustomTask(String taskName) {
        if (processCustomTasks.containsKey(taskName)) {
            this.processCustomTasks.get(taskName).getLeft().cancel(false);
            this.processCustomTasks.remove(taskName);
        }
    }
}
