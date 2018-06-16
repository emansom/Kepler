package org.alexdev.kepler.game;

import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.player.PlayerManager;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.enums.StatusType;
import org.alexdev.kepler.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class GameScheduler implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(GameScheduler.class);
    private AtomicLong tickRate = new AtomicLong();

    private ScheduledExecutorService schedulerService;
    private ScheduledFuture<?> gameScheduler;

    private static GameScheduler instance;

    private GameScheduler() {
        schedulerService = createNewScheduler();
        gameScheduler = schedulerService.scheduleAtFixedRate(this, 0, 1, TimeUnit.SECONDS);
    }
    
    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        this.tickRate.incrementAndGet();

        for (Player player : PlayerManager.getInstance().getPlayers()) {
            if (player.getRoom() != null) {

                if (DateUtil.getCurrentTimeSeconds() > player.getRoomUser().getSleepTimer()) {
                    if (!player.getRoomUser().containsStatus(StatusType.SLEEP)) {
                        player.getRoomUser().setStatus(StatusType.SLEEP, "");
                        player.getRoomUser().setNeedsUpdate(true);
                    }
                }

                if (DateUtil.getCurrentTimeSeconds() > player.getRoomUser().getAfkTimer()) {
                    Room room = player.getRoomUser().getRoom();

                    var curPos = player.getRoomUser().getPosition();
                    var doorPos = room.getModel().getDoorLocation();

                    // If we're standing in the door, immediately leave room
                    if (curPos.equals(doorPos)) {
                        room.getEntityManager().leaveRoom(player, true);
                        return;
                    }

                    // Walk to door
                    player.getRoomUser().walkTo(doorPos.getX(), doorPos.getY());
                }
            }
        }
    }

    /**
     * Gets the scheduler service.
     *
     * @return the scheduler service
     */
    public ScheduledExecutorService getSchedulerService() {
        return schedulerService;
    }

    /**
     * Get the game scheduler loop
     *
     * @return the game scheduler loop
     */
    public ScheduledFuture<?> getGameScheduler() {
        return gameScheduler;
    }

    /**
     * Creates the new schedulerService.
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
