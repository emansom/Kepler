package org.alexdev.kepler.game.games.enums;

public enum GameEventType {
    BATTLEBALL_PLAYER_EVENT(2),
    BATTLEBALL_POWERUP_SPAWN(0);

    private final int eventId;

    GameEventType(int eventId) {
        this.eventId = eventId;
    }

    public int getEventId() {
        return eventId;
    }
}