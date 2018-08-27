package org.alexdev.kepler.game.games.battleball;

public enum BattleballTileState {
    DEFAULT(0),
    TOUCHED(1),
    CLICKED(2),
    PRESSED(3),
    SEALED(4);// = 1, CLICKED = 2, PRESSED = 3, SEALED = 4

    private final int tileStateId;

    BattleballTileState(int tileStateId) {
        this.tileStateId = tileStateId;
    }

    public int getTileStateId() {
        return tileStateId;
    }

    public static BattleballTileState getStateById(int id) {
        for (BattleballTileState state : values()) {
            if (state.getTileStateId() == id) {
                return state;
            }
        }

        return null;
    }
}
