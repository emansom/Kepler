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
    private Map<Player, Integer> playersToSave;

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

                        // Attempt to walk to the door
                        player.getRoomUser().walkTo(doorPos.getX(), doorPos.getY());

                        // If user isn't walking, leave immediately
                        if (!player.getRoomUser().isWalking()) {
                            player.getRoomUser().getRoom().getEntityManager().leaveRoom(player, true);
                        }
                    }

                    if (!player.getRoomUser().containsStatus(StatusType.SLEEP)) {
                        if (DateUtil.getCurrentTimeSeconds() > player.getDetails().getNextHandout()) {
                            //CurrencyDao.increaseCredits(player.getDetails(), GameConfiguration.getInstance().getInteger("credits.scheduler.amount"));
                            //player.send(new CREDIT_BALANCE(player.getDetails()));
                            this.playersToSave.put(player, GameConfiguration.getInstance().getInteger("credits.scheduler.amount"));
                            player.getDetails().resetNextHandout();
                        }
                    }
                }
            }

            if (this.tickRate.get() % 30 == 0) { // Save every 30 seconds
                CurrencyDao.increaseCredits(this.playersToSave);

                for (var kvp : this.playersToSave.entrySet()) {
                    kvp.getKey().send(new CREDIT_BALANCE(kvp.getKey().getDetails()));
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
