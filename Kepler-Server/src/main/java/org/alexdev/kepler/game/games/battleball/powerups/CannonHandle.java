package org.alexdev.kepler.game.games.battleball.powerups;

import javafx.geometry.Pos;
import org.alexdev.kepler.game.GameScheduler;
import org.alexdev.kepler.game.games.battleball.BattleballGame;
import org.alexdev.kepler.game.games.battleball.BattleballTile;
import org.alexdev.kepler.game.games.battleball.enums.BattleballColourType;
import org.alexdev.kepler.game.games.battleball.enums.BattleballPlayerState;
import org.alexdev.kepler.game.games.battleball.enums.BattleballTileType;
import org.alexdev.kepler.game.games.battleball.events.PlayerMoveEvent;
import org.alexdev.kepler.game.games.battleball.objects.PlayerUpdateObject;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.games.utils.PowerUpUtil;
import org.alexdev.kepler.game.games.utils.TileUtil;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.mapping.RoomTile;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CannonHandle {
    public static void handle(BattleballGame game, GamePlayer gamePlayer, Room room) {
        gamePlayer.getPlayer().getRoomUser().stopWalking();
        gamePlayer.getPlayer().getRoomUser().setWalkingAllowed(false);

        Position nextPosition = gamePlayer.getPlayer().getRoomUser().getPosition();
        int rotation = nextPosition.getRotation();

        LinkedList<BattleballTile> tilesToUpdate = new LinkedList<>();
        List<GamePlayer> stunnedPlayers = new ArrayList<>();

        while (TileUtil.isValidGameTile(gamePlayer, (BattleballTile) game.getTile(nextPosition.getX(), nextPosition.getY()), false)) {
            nextPosition = nextPosition.getSquareInFront();

            if (!TileUtil.isValidGameTile(gamePlayer, (BattleballTile) game.getTile(nextPosition.getX(), nextPosition.getY()), false)) {
                break;
            }

            BattleballTile battleballTile = (BattleballTile) game.getTile(nextPosition.getX(), nextPosition.getY());

            tilesToUpdate.add(battleballTile);
            stunnedPlayers.addAll(battleballTile.getPlayers(gamePlayer));

        }

        if (tilesToUpdate.isEmpty()) {
            nextPosition = gamePlayer.getPlayer().getRoomUser().getPosition();
            tilesToUpdate.add((BattleballTile) game.getTile(nextPosition.getX(), nextPosition.getY()));
        }

        // Stun players in direction of cannon
        GameScheduler.getInstance().getSchedulerService().schedule(() -> {
            for (GamePlayer stunnedPlayer : stunnedPlayers) {
                PowerUpUtil.stunPlayer(game, stunnedPlayer, BattleballPlayerState.STUNNED);
            }
        }, 250, TimeUnit.MILLISECONDS);


        //gamePlayer.setPlayerState(BattleballPlayerState.CLIMBING_INTO_CANNON);
        //game.getObjectsQueue().add(new PlayerUpdateObject(gamePlayer));

        for (BattleballTile tile : tilesToUpdate) {
            //if (tile.getState() == BattleballTileType.SEALED) {
            //    continue;
            //}

            if (tile.getColour() == BattleballColourType.DISABLED) {
                continue;
            }

            if (tile.getState() == BattleballTileType.SEALED && tile.getColour().getColourId() == gamePlayer.getTeam().getId()) {
                continue;
            }


            BattleballTileType state = tile.getState();
            BattleballColourType colour = tile.getColour();

            BattleballTileType newState = BattleballTileType.SEALED;
            BattleballColourType newColour = BattleballColourType.getColourById(gamePlayer.getTeam().getId());

            BattleballTile.getNewPoints(gamePlayer, state, colour, newState, newColour);

            tile.setColour(newColour);
            tile.setState(newState);

            BattleballTile.checkFill(gamePlayer, tile, game.getFillTilesQueue());
            game.getUpdateTilesQueue().add(tile);
        }

        BattleballTile lastTile = tilesToUpdate.getLast();

        Position lastPosition = lastTile.getPosition().copy();
        lastPosition.setRotation(rotation);

        gamePlayer.setPlayerState(BattleballPlayerState.FLYING_THROUGH_AIR);
        game.getObjectsQueue().add(new PlayerUpdateObject(gamePlayer));
        game.getEventsQueue().add(new PlayerMoveEvent(gamePlayer, lastPosition));

        GameScheduler.getInstance().getSchedulerService().schedule(() -> {
            gamePlayer.getPlayer().getRoomUser().setPosition(lastPosition);
            PowerUpUtil.stunPlayer(game, gamePlayer, BattleballPlayerState.STUNNED);
        }, 1000, TimeUnit.MILLISECONDS);

        gamePlayer.getPlayer().getRoomUser().warp(lastTile.getPosition(), false);

    }
}
