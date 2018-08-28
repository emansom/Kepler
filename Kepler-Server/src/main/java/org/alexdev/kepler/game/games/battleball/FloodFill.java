package org.alexdev.kepler.game.games.battleball;

import org.alexdev.kepler.game.games.Game;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.pathfinder.Pathfinder;
import org.alexdev.kepler.game.pathfinder.Position;

import java.util.HashSet;

public class FloodFill {
    public static HashSet<BattleballTile> getFill(GamePlayer gamePlayer, int tileX, int tileY) {
        HashSet<BattleballTile> tiles = new HashSet<>();
        return tiles;
    }


    private static HashSet<BattleballTile> neighbours(Game game, Position position) {
        HashSet<BattleballTile> battleballTiles = new HashSet<>();

        for (Position point : Pathfinder.DIAGONAL_MOVE_POINTS) {
            Position tmp = position.copy().add(point);

            if (tmp == null) {
                return battleballTiles;
            }

            battleballTiles.add(game.getTile(tmp.getX(), tmp.getY()));
        }

        return battleballTiles;
    }
}