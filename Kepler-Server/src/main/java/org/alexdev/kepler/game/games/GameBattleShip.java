package org.alexdev.kepler.game.games;

import java.util.List;

public class GameBattleShip extends GamehallGame {
    public GameBattleShip(int roomId, List<int[]> kvp) {
        super(roomId, kvp);
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
        return "BattleShip";
    }

}
