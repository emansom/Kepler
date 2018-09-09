package org.alexdev.kepler.game.games.utils;

import org.alexdev.kepler.game.games.battleball.BattleballTile;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.room.mapping.RoomTile;

public class TileUtil {
    public static boolean isValidGameTile(GamePlayer gamePlayer, BattleballTile tile) {
        if (tile == null) {// && tile.getColour() != BattleballColourType.DISABLED;
            return false;
        }

        return RoomTile.isValidTile(gamePlayer.getGame().getRoom(), gamePlayer.getPlayer(), tile.getPosition());
    }
}
