package org.alexdev.kepler.game.games.battleball;

public enum  BattleballTileColour {
    DISABLED(-2),
    DEFAULT(-1),
    RED(0),
    BLUE(1),
    YELLOW(2),
    GREEN(3);

    private final int tileColourId;

    BattleballTileColour(int tileColourId) {
        this.tileColourId = tileColourId;
    }

    public int getTileColourId() {
        return tileColourId;
    }

    public static BattleballTileColour getColourById(int id) {
        for (BattleballTileColour colour : values()) {
            if (colour.getTileColourId() == id) {
                return colour;
            }
        }

        return null;
    }
}
