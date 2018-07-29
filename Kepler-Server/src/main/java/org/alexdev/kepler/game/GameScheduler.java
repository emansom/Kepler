package org.alexdev.kepler.game;

import org.alexdev.kepler.dao.mysql.CurrencyDao;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.player.PlayerDetails;
import org.alexdev.kepler.game.player.PlayerManager;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.enums.StatusType;
import org.alexdev.kepler.log.Log;
import org.alexdev.kepler.messages.outgoing.user.CREDIT_BALANCE;
import org.alexdev.kepler.util.DateUtil;
import org.alexdev.kepler.util.config.GameConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class GameScheduler implements Runnable {
    private AtomicLong tickRate = new AtomicLong();

    private ScheduledExecutorService schedulerService;
    private ScheduledFuture<?> gameScheduler;
    private Map<PlayerDetails, Integer> playersToSave;

    private static GameScheduler instance;

    private GameScheduler() {
        this.schedulerService = createNewScheduler();
        this.gameScheduler = this.schedulerService.scheduleAtFixedRate(this, 0, 1, TimeUnit.SECONDS);
        this.playersToSave = new LinkedHashMap<>();
    }
    
    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        this.tickRate.incrementAndGet();

        try {
            for (Player player : PlayerManager.getInstance().getPlayers()) {
                if (player.getRoomUser().getRoom() != null) {

                    // If their sleep timer is now lower than the current time, make them sleep.
                    if (DateUtil.getCurrentTimeSeconds() > player.getRoomUser().getSleepTimer()) {
                        if (!player.getRoomUser().containsStatus(StatusType.SLEEP)) {
                            player.getRoomUser().setStatus(StatusType.SLEEP, "");
                            player.getRoomUser().setNeedsUpdate(true);
                        }
                    }

                    // If their afk timer is up, send them out.
                    if (DateUtil.getCurrentTimeSeconds() > player.getRoomUser().getAfkTimer()) {
                        player.getRoomUser().kick();
                    }

                    // If they're not sleeping (aka, active) and their next handout expired, give them their credits!
                    if (!player.getRoomUser().containsStatus(StatusType.SLEEP)) {
                        if (DateUtil.getCurrentTimeSeconds() > player.getDetails().getNextHandout()) {
                            this.playersToSave.put(player.getDetails(), GameConfiguration.getInstance().getInteger("credits.scheduler.amount"));
                            player.getDetails().resetNextHandout();
                        }
                    }
                }
            }

            if (this.tickRate.get() % 30 == 0) { // Save every 30 seconds
                CurrencyDao.increaseCredits(this.playersToSave);

                for (var kvp : this.playersToSave.entrySet()) {
                    PlayerDetails playerDetails = kvp.getKey();
                    Player player = PlayerManager.getInstance().getPlayerById(playerDetails.getId());

                    if (player != null) {
                        player.send(new CREDIT_BALANCE(playerDetails));
                    }
                }

                this.playersToSave.clear();
            }

        } catch (Exception ex) {
            Log.getErrorLogger().error("GameScheduler crashed: ", ex);
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
