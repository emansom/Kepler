package org.alexdev.kepler.game;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class GameScheduler implements Runnable {
    private AtomicLong tickRate = new AtomicLong();

    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> gameScheduler;

    private static GameScheduler instance;

    private GameScheduler() {
        scheduler = createNewScheduler();
        gameScheduler = scheduler.scheduleAtFixedRate(this, 0, 1, TimeUnit.SECONDS);
    }
    
    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        tickRate.incrementAndGet();

        // If this task has ticked for an entire minute...
        if (tickRate.get() % 60 == 0) {

        }
    }

    /**
     * Gets the scheduler.
     *
     * @return the scheduler
     */
    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    /**
     * Creates the new scheduler.
     *
     * @return the scheduled executor service
     */
    public static ScheduledExecutorService createNewScheduler() {
        return Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
    }

    /**
     * Gets the instance
     *
     * @return the instance
     */
    public static GameScheduler getInstance() {
        if (instance == null) {
            instance = new GameScheduler();
        }

        return instance;
    }
}
