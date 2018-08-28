package org.alexdev.kepler.game.games.battleball;

import org.alexdev.kepler.game.games.Game;
import org.alexdev.kepler.game.games.GameType;

public class BattleballGame extends Game {
    public BattleballGame(int id, int mapId, GameType gameType, String name, int teamAmount, int gameCreator) {
        super(id, mapId, gameType, name, teamAmount, gameCreator);
    }
}
