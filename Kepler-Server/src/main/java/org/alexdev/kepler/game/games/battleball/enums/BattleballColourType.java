package org.alexdev.kepler.game.games.battleball.enums;

public enum BattleballColourType {
    DISABLED(-2),
    DEFAULT(-1),
    RED(0),
    BLUE(1),
    YELLOW(2),
    GREEN(3);

    private final int tileColourId;

    BattleballColourType(int tileColourId) {
        this.tileColourId = tileColourId;
    }

    public int getColourId() {
        return tileColourId;
    }

    public static BattleballColourType getColourById(int id) {
        for (BattleballColourType colour : values()) {
            if (colour.getColourId() == id) {
                return colour;
            }
        }

        return null;
    }
}
