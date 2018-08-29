package org.alexdev.kepler.game.games.battleball;

import org.alexdev.kepler.game.games.Game;
import org.alexdev.kepler.game.games.battleball.enums.BattleballTileColour;
import org.alexdev.kepler.game.games.battleball.enums.BattleballTileState;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.pathfinder.Pathfinder;
import org.alexdev.kepler.game.pathfinder.Position;

import java.util.*;

public class FloodFill {
    public static Collection<BattleballTile> getFill(GamePlayer gamePlayer, BattleballTile startTile) {
        HashSet<BattleballTile> closed = new HashSet<>();

        ArrayDeque<BattleballTile> stack = new ArrayDeque<>();
        stack.add(startTile);

        while (stack.size() > 0) {
            BattleballTile tile = stack.pollLast();

            if (tile != null) {
                for (BattleballTile loopTile : neighbours(gamePlayer.getGame(), tile.getPosition())) {
                    if (loopTile == null) {
                        closed.clear();
                        return closed;
                    }

                    if (loopTile.getColour() == BattleballTileColour.DISABLED) {
                        closed.clear();
                        return closed;
                    }

                    if ((loopTile.getColour().getColourId() != gamePlayer.getTeamId() || loopTile.getState() != BattleballTileState.SEALED) && !closed.contains(loopTile) && !stack.contains(loopTile)) {
                        stack.addFirst(loopTile);
                    }
                }

                closed.add(tile);
            }
        }

        return closed;
    }

    public static HashSet<BattleballTile> neighbours(Game game, Position position) {
        HashSet<BattleballTile> battleballTiles = new HashSet<>();

        for (Position point : Pathfinder.MOVE_POINTS) {
            Position tmp = position.copy().add(point);
            battleballTiles.add(game.getTile(tmp.getX(), tmp.getY()));
        }

        return battleballTiles;
    }
}