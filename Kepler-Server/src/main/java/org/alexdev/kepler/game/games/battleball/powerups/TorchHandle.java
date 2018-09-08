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

public class TorchHandle {
    public static void handle(BattleballGame game, GamePlayer gamePlayer, Room room) {
        GameTeam gameTeam = game.getTeams().get(gamePlayer.getTeamId());
        List<BattleballTile> tilesToUpdate = new ArrayList<>();

        Position nextPosition = gamePlayer.getPlayer().getRoomUser().getPosition();

        for (int i = 0; i < 5; i++) {
            nextPosition = nextPosition.getSquareInFront();

            BattleballTile tile = (BattleballTile) game.getTile(nextPosition.getX(), nextPosition.getY());

            if (tile == null || tile.getColour() == BattleballColourType.DISABLED) {
                break;
            }

            tilesToUpdate.add(tile);
        }

        for (BattleballTile tile : tilesToUpdate) {
            if (tile.getState() == BattleballTileType.SEALED) {
                continue;
            }

            tile.setColour(BattleballColourType.getColourById(gamePlayer.getTeamId()));
            tile.setState(BattleballTileType.SEALED);

            gameTeam.setSealedTileScore();
            game.getUpdateTilesQueue().add(tile);
        }

    }
}
