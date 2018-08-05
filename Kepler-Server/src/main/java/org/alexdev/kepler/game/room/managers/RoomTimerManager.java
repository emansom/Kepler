package org.alexdev.kepler.game.room.managers;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.room.RoomUser;
import org.alexdev.kepler.game.room.enums.StatusType;
import org.alexdev.kepler.util.DateUtil;
import org.alexdev.kepler.util.config.GameConfiguration;

public class RoomTimerManager {
    private Entity entity;
    private RoomUser roomUser;
    private int lookTimer;
    private long afkTimer;
    private long sleepTimer;
    private long chatBubbleTimer;

    public RoomTimerManager(RoomUser roomUser) {
        this.roomUser = roomUser;
        this.entity = roomUser.getEntity();
    }

    /**
     * Reset all timers, used for first entry into room.
     */
    public void resetTimers() {
        this.resetRoomTimer();
        this.stopChatBubbleTimer();
        this.stopLookTimer();
    }

    /**
     * Set the room timer, make it 10 minutes by default
     */
    public void resetRoomTimer() {
        this.resetRoomTimer(GameConfiguration.getInstance().getInteger("afk.timer.seconds"));
    }

    /**
     * Set the room timer, but with an option to override it.
     *
     * @param afkTimer the timer to override
     */
    public void resetRoomTimer(int afkTimer) {
        this.afkTimer = DateUtil.getCurrentTimeSeconds() + afkTimer;
        this.sleepTimer = DateUtil.getCurrentTimeSeconds() + GameConfiguration.getInstance().getInteger("sleep.timer.seconds");

        // If the user was sleeping, remove the sleep and tell the room cycle to update our character
        if (this.roomUser.containsStatus(StatusType.SLEEP)) {
            this.roomUser.removeStatus(StatusType.SLEEP);
            this.roomUser.setNeedsUpdate(true);
        }
    }

    /**
     * Begin head look timer.
     */
    public void beginLookTimer() {
        this.lookTimer = DateUtil.getCurrentTimeSeconds() + 6;
    }

    /**
     * Stop head look timer.
     */
    public void stopLookTimer() {
        this.lookTimer = -1;
    }

    /**
     * Begin chat time out.
     */
    public void beginChatBubbleTimer() {
        int timeout = GameConfiguration.getInstance().getInteger("chat.bubble.timeout.seconds");

        if (timeout > 0) {
            this.chatBubbleTimer = DateUtil.getCurrentTimeSeconds() + timeout;
        }
    }

    /**
     * Stop chat time out.
     */
    public void stopChatBubbleTimer() {
        this.chatBubbleTimer = -1;
    }

    public Entity getEntity() {
        return entity;
    }

    public long getChatBubbleTimer() {
        return chatBubbleTimer;
    }

    public int getLookTimer() {
        return lookTimer;
    }

    public long getAfkTimer() {
        return afkTimer;
    }

    public long getSleepTimer() {
        return sleepTimer;
    }
}
