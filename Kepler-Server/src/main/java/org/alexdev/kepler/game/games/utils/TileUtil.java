package org.alexdev.kepler.game.games.utils;

import org.alexdev.kepler.game.games.Game;
import org.alexdev.kepler.game.games.battleball.BattleballTile;
import org.alexdev.kepler.game.games.battleball.enums.BattleballColourType;
import org.alexdev.kepler.game.games.battleball.enums.BattleballTileType;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.games.player.GameTeam;
import org.alexdev.kepler.game.room.mapping.RoomTile;

public class TileUtil {
    public static boolean undoTileAttributes(BattleballTile tile, Game game) {
        BattleballTileType state = tile.getState();
        BattleballColourType colour = tile.getColour();

        if (colour == BattleballColourType.DEFAULT || state == BattleballTileType.DEFAULT) {
            return false;
        }

        int pointsToRemove = 0;

        if (state == BattleballTileType.TOUCHED) {
            pointsToRemove = 2;
        }

        if (state == BattleballTileType.CLICKED) {
            pointsToRemove = 6;
        }

        if (state == BattleballTileType.PRESSED) {
            pointsToRemove = 10;
        }

        if (state == BattleballTileType.SEALED) {
            pointsToRemove = 14;
        }
        GameTeam team = game.getTeams().get(colour.getColourId());

        if (pointsToRemove > 0) {
            int eachTeamRemove = team.getActivePlayers().size() / pointsToRemove;

            for (GamePlayer p : team.getActivePlayers()) {
                p.setScore(p.getScore() - eachTeamRemove);
            }

            tile.setColour(BattleballColourType.DEFAULT);
            tile.setState(BattleballTileType.DEFAULT);
        }

        return true;
    }
    public static boolean isValidGameTile(GamePlayer gamePlayer, BattleballTile tile, boolean checkEntities) {
        if (tile == null) {// && tile.getColour() != BattleballColourType.DISABLED;
            return false;
        }

        return RoomTile.isValidTile(gamePlayer.getGame().getRoom(), checkEntities ? gamePlayer.getPlayer() : null, tile.getPosition());
    }
}
