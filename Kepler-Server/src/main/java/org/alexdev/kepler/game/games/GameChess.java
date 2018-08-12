package org.alexdev.kepler.game.games;

import java.util.List;

public class GameChess extends GamehallGame {
    public GameChess(int roomId, List<int[]> chairs) {
        super(roomId, chairs);
    }

    @Override
    public int getMaximumPeopleRequired() {
        return 2;
    }

    @Override
    public int getMinimumPeopleRequired() {
        return 2;
    }

    @Override
    public String getGameFuseType() {
        return "Chess";
    }
}
