package org.alexdev.kepler.game.games.battleball.powerups;

import org.alexdev.kepler.game.games.battleball.BattleballGame;
import org.alexdev.kepler.game.games.battleball.BattleballTile;
import org.alexdev.kepler.game.games.battleball.enums.BattleballColourType;
import org.alexdev.kepler.game.games.battleball.enums.BattleballTileType;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.games.player.GameTeam;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.room.Room;

import java.util.ArrayList;
import java.util.List;

public class LightbulbHandle {
    public static void handle(BattleballGame game, GamePlayer gamePlayer, Room room) {
        GameTeam gameTeam = gamePlayer.getTeam();

        for (Position position : gamePlayer.getPlayer().getRoomUser().getPosition().getCircle(5)) {
            BattleballTile tile = (BattleballTile) game.getTile(position.getX(), position.getY());

            if (tile == null ||
                    tile.getColour() == BattleballColourType.DISABLED ||
                    tile.getState() == BattleballTileType.SEALED) {
                continue;
            }

            BattleballTileType state = tile.getState();
            BattleballColourType colour = tile.getColour();

            if (state == BattleballTileType.DEFAULT) {
                state = BattleballTileType.TOUCHED; // Don't make it 4 hits, make it 3
            }

            BattleballTileType newState = BattleballTileType.getStateById(state.getTileStateId() + 1);
            BattleballColourType newColour = BattleballColourType.getColourById(gameTeam.getId());

            BattleballTile.getNewPoints(gamePlayer, state, colour, newState, newColour);

            tile.setColour(newColour);
            tile.setState(newState);

            if (newState == BattleballTileType.SEALED) {
                BattleballTile.checkFill(gamePlayer, tile, game.getFillTilesQueue());
            }

            gameTeam.setSealedTileScore();
            game.getUpdateTilesQueue().add(tile);
        }

    }
}
