package org.alexdev.kepler.game.games.enums;

public enum GameObjectType {
    BATTLEBALL_PLAYER_OBJECT(0),
    BATTLEBALL_POWER_OBJECT(1);

    private final int objectId;

    GameObjectType(int objectId) {
        this.objectId = objectId;
    }

    public int getObjectId() {
        return objectId;
    }
}