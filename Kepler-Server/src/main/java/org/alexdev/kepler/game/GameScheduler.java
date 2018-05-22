package org.alexdev.kepler.game;

import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.player.PlayerManager;
import org.alexdev.kepler.messages.outgoing.user.PING;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.PlatformLoggingMXBean;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class GameScheduler implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(GameScheduler.class);
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
        this.tickRate.incrementAndGet();

        synchronized (PlayerManager.getInstance().getPlayers()) {
            for (Player player : PlayerManager.getInstance().getPlayers()) {
                // Do stuff here like add credits per x minutes, etc.
            }
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
