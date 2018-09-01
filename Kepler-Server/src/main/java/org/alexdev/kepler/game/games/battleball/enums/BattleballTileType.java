package org.alexdev.kepler.game.games.battleball.enums;

public enum BattleballTileType {
    DEFAULT(0),
    TOUCHED(1),
    CLICKED(2),
    PRESSED(3),
    SEALED(4);// = 1, CLICKED = 2, PRESSED = 3, SEALED = 4

    private final int tileStateId;

    BattleballTileType(int tileStateId) {
        this.tileStateId = tileStateId;
    }

    public int getTileStateId() {
        return tileStateId;
    }

    public static BattleballTileType getStateById(int id) {
        for (BattleballTileType state : values()) {
            if (state.getTileStateId() == id) {
                return state;
            }
        }

        return null;
    }
}
