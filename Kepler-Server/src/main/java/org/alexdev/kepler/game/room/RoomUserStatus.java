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

    public void swapKeyAction() {
        EntityStatus temp = this.key;
        this.key = this.action;
        this.action = temp;
    }

    public EntityStatus getKey() {
        return key;
    }

    public void setKey(EntityStatus status) {
        this.key = status;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getSecLifetime() {
        return secLifetime;
    }

    public void setSecLifetime(int secLifetime) {
        this.secLifetime = secLifetime;
    }

    public int getSecActionSwitch() {
        return secActionSwitch;
    }

    public void setSecActionSwitch(int secActionSwitch) {
        this.secActionSwitch = secActionSwitch;
    }

    public int getSecSwitchLifetime() {
        return secSwitchLifetime;
    }

    public void setSecSwitchLifetime(int secSwitchLifetime) {
        this.secSwitchLifetime = secSwitchLifetime;
    }

    public EntityStatus getAction() {
        return action;
    }

    public void setAction(EntityStatus action) {
        this.action = action;
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
