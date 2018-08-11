package org.alexdev.kepler.game.games;

import java.util.List;

public class GameTicTacToe extends GamehallGame {
    public GameTicTacToe(int roomId, List<int[]> chairs) {
        super(roomId, chairs);
    }


    /**
     * Get FUSE game type
     *
     * @return the game type
     */
    public String getGameFuseType() {
        return "TicTacToe";
    }
}
