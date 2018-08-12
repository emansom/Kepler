package org.alexdev.kepler.game.games;

import java.util.List;

public class GamePoker extends GamehallGame {
    public GamePoker(int roomId, List<int[]> kvp) {
        super(roomId, kvp);
    }

    @Override
    public int getMaximumPeopleRequired() {
        return 4;
    }

    @Override
    public int getMinimumPeopleRequired() {
        return 2;
    }

    @Override
    public String getGameFuseType() {
        return "Poker";
    }
}
