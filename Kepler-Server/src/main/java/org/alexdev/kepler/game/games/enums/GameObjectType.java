package org.alexdev.kepler.game.games.enums;

public enum GameObjectType {
    BATTLEBALL_PLAYER_OBJECT(0),
    BATTLEBALL_POWER_OBJECT(1),
    SNOWSTORM_PLAYER_OBJECT(5),

    SNOWSTORM_SPAWN_PLAYER_EVENT(0),
    SNOWSTORM_SPAWN_PLAYER_ONE(0);

    private final int objectId;

    GameObjectType(int objectId) {
        this.objectId = objectId;
    }

    public int getObjectId() {
        return objectId;
    }
}
