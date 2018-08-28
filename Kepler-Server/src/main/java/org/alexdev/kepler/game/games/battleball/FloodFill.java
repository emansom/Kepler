package org.alexdev.kepler.game.games.battleball;

import org.alexdev.kepler.game.games.Game;
import org.alexdev.kepler.game.games.battleball.enums.BattleballTileColour;
import org.alexdev.kepler.game.games.battleball.enums.BattleballTileState;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.pathfinder.Pathfinder;
import org.alexdev.kepler.game.pathfinder.Position;

import java.util.*;

public class FloodFill {
    public static Collection<BattleballTile> getFill(GamePlayer gamePlayer, int x, int y, final byte teamId) {
        HashSet<BattleballTile> closed = new HashSet<>();

        ArrayDeque<BattleballTile> stack = new ArrayDeque<>();
        stack.add(gamePlayer.getGame().getTile(x, y));

        while (stack.size() > 0) {
            BattleballTile tile = stack.pollLast();

            if (tile != null) {
                for (BattleballTile p : neighbours(gamePlayer.getGame(), tile.getPosition())) {
                    if (p == null) {
                        continue;
                    }

                    if (p.getColour() == BattleballTileColour.DISABLED) {
                        continue;
                    }

                    if ((p.getColour().getColourId() != teamId || p.getState() != BattleballTileState.SEALED) && !closed.contains(p) && !stack.contains(p)) {
                        stack.addFirst(p);
                    }
                }

                closed.add(tile);
            }
        }

        return closed;
    }

    private static HashSet<BattleballTile> neighbours(Game game, Position position) {
        HashSet<BattleballTile> battleballTiles = new HashSet<>();

        for (Position point : Pathfinder.MOVE_POINTS) {
            Position tmp = position.copy().add(point);
            battleballTiles.add(game.getTile(tmp.getX(), tmp.getY()));
        }

        return battleballTiles;
    }
}