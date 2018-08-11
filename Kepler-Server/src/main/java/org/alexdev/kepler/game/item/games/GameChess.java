package org.alexdev.kepler.game.item.games;

import java.util.List;

public class GameChess extends GamehallGame {
    public GameChess(int roomId, List<int[]> chairs) {
        super(roomId, chairs);
    }


    /**
     * Get FUSE game type
     *
     * @return the game type
     */
    public String getGameFuseType() {
        return "Chess";
    }
}
