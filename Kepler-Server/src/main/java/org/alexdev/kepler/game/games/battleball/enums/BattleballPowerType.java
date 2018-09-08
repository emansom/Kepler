package org.alexdev.kepler.game.games.battleball.enums;

import java.util.concurrent.ThreadLocalRandom;

public enum BattleballPowerType {
    LIGHT_BLUB(1),
    SPRING(2),
    FLASHLIGHT(3),
    CANNON(4),
    BOX_OF_PINS(5),
    HARLEQUIN(6),
    BOMB(7),
    DRILL(8);

    private final int powerUpId;

    BattleballPowerType(int powerUpId) {
        this.powerUpId = powerUpId;
    }

    public static BattleballPowerType random() {
        return BattleballPowerType.values()[ThreadLocalRandom.current().nextInt(0, BattleballPowerType.values().length)];
    }

    public static BattleballPowerType getById(int powerUpId) {
        for (var powerUp : values()) {
            if (powerUp.getPowerUpId() == powerUpId) {
                return powerUp;
            }
        }

        return null;
    }

    public int getPowerUpId() {
        return powerUpId;
    }
}
