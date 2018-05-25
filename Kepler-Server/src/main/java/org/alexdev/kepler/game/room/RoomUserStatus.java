package org.alexdev.kepler.game.room;

import org.alexdev.kepler.game.entity.EntityStatus;

public class RoomUserStatus {
    private EntityStatus key;
    private String value;
    private EntityStatus action;

    private int secLifetime;
    private int secActionSwitch;
    private int secSwitchLifetime;
    private int lifetimeCountdown;
    private int actionCountdown;
    private int actionSwitchCountdown;

    public RoomUserStatus(EntityStatus status, String value) {
        this.key = status;
        this.value = value;
        this.secLifetime = -1;
        this.secSwitchLifetime = -1;
    }

    public RoomUserStatus(EntityStatus status, String value, int secLifetime, EntityStatus action, int secActionSwitch, int secSwitchLifetime) {
        this.key = status;
        this.value = value;
        this.action = action;

        this.secLifetime = secLifetime;
        this.secActionSwitch = secActionSwitch;
        this.secSwitchLifetime = secSwitchLifetime;

        this.lifetimeCountdown = secLifetime;
        this.actionCountdown = secActionSwitch;
        this.actionSwitchCountdown = -1;
    }

    /**
     * Swap the key and action for timed statuses, used for drinking, etc.
     */
    public void swapKeyAction() {
        EntityStatus temp = this.key;
        this.key = this.action;
        this.action = temp;
    }

    public EntityStatus getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getSecActionSwitch() {
        return secActionSwitch;
    }


    public int getSecSwitchLifetime() {
        return secSwitchLifetime;
    }


    public int getLifetimeCountdown() {
        return lifetimeCountdown;
    }

    public void setLifetimeCountdown(int lifetimeCountdown) {
        this.lifetimeCountdown = lifetimeCountdown;
    }

    public int getActionCountdown() {
        return actionCountdown;
    }

    public void setActionCountdown(int actionCountdown) {
        this.actionCountdown = actionCountdown;
    }

    public int getActionSwitchCountdown() {
        return actionSwitchCountdown;
    }

    public void setActionSwitchCountdown(int actionSwitchCountdown) {
        this.actionSwitchCountdown = actionSwitchCountdown;
    }
}
