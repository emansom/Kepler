package org.alexdev.kepler.game.games.battleball.enums;

public enum BattleballPowerType {
    LIGHT_BLUB(1),
    SPRING(2),
    FLASHLIGHT(3),
    CANNON(4),
    BOX_OF_TACKS(5),
    HARLEQUIN(6),
    BOMB(7),
    DRILL(8);

    private final int powerUpId;

    BattleballPowerType(int powerUpId) {
        this.powerUpId = powerUpId;
    }

    public int getPowerUpId() {
        return powerUpId;
    }
}
