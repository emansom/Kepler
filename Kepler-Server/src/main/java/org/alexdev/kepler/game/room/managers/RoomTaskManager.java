package org.alexdev.kepler.game.room.managers;

import org.alexdev.kepler.game.GameScheduler;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.tasks.EntityTask;
import org.alexdev.kepler.game.room.tasks.RollerTask;
import org.alexdev.kepler.game.room.tasks.StatusTask;
import org.alexdev.kepler.util.config.GameConfiguration;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class RoomTaskManager {
    private Room room;
    private ScheduledExecutorService executorService;
    private Map<String, Pair<ScheduledFuture<?>, Runnable>> processTasks;

    public RoomTaskManager(Room room) {
        this.room = room;
        this.processTasks = new ConcurrentHashMap<>();
        this.executorService = GameScheduler.getInstance().getSchedulerService();
    }

    /**
     * Start all needed room tasks, called when there's at least 1 player in the room.
     */
    public void startTasks() {
        int rollerMillisTask = GameConfiguration.getInstance().getInteger("roller.tick.default");

        this.scheduleTask("EntityTask", new EntityTask(this.room), 500, TimeUnit.MILLISECONDS);
        this.scheduleTask("StatusTask", new StatusTask(this.room), 1, TimeUnit.SECONDS);
        this.scheduleTask("RollerTask", new RollerTask(this.room), 0, rollerMillisTask, TimeUnit.MILLISECONDS);
    }

    /**
     * Stop tasks, called when there's no players in the room.
     */
    public void stopTasks() {
        for (var tasksKvp : this.processTasks.entrySet()) {
            tasksKvp.getValue().getLeft().cancel(false);
        }

        this.processTasks.clear();
    }

    /**
     * Schedule a custom task.
     *
     * @param taskName the task name identifier
     * @param runnableTask the runnable task instance
     * @param interval the interval of the task
     * @param timeUnit the time unit of the interval
     */
    public void scheduleTask(String taskName, Runnable runnableTask, int interval, TimeUnit timeUnit) {
        this.cancelTask(taskName);

        var future = this.executorService.scheduleAtFixedRate(runnableTask, 0, interval, timeUnit);
        this.processTasks.put(taskName, Pair.of(
                future,
                runnableTask
        ));
    }

    /**
     * Schedule a custom task with a delay.
     *
     * @param taskName the task name identifier
     * @param runnableTask the runnable task instance
     * @param interval the interval of the task
     * @param delay the time to wait before the task starts
     * @param timeUnit the time unit of the interval
     */
    public void scheduleTask(String taskName, Runnable runnableTask, int delay, int interval, TimeUnit timeUnit) {
        this.cancelTask(taskName);

        var future = this.executorService.scheduleAtFixedRate(runnableTask, delay, interval, timeUnit);
        this.processTasks.put(taskName, Pair.of(
                future,
                runnableTask
        ));
    }

    /**
     * Cancels a custom task by task name.
     *
     * @param taskName the name of the task to cancel
     */
    public void cancelTask(String taskName) {
        if (this.processTasks.containsKey(taskName)) {
            this.processTasks.get(taskName).getLeft().cancel(false);
            this.processTasks.remove(taskName);
        }
    }

    /**
     * Get if the task is registered.
     *
     * @param taskName the task name to check for
     * @return true, if successful
     */
    public boolean hasTask(String taskName) {
        return this.processTasks.containsKey(taskName);
    }
}
