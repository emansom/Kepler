package org.alexdev.kepler.game.games.battleball.powerups;

import org.alexdev.kepler.game.GameScheduler;
import org.alexdev.kepler.game.games.Game;
import org.alexdev.kepler.game.games.battleball.BattleballGame;
import org.alexdev.kepler.game.games.battleball.BattleballTile;
import org.alexdev.kepler.game.games.battleball.enums.BattleballColourType;
import org.alexdev.kepler.game.games.battleball.enums.BattleballPlayerState;
import org.alexdev.kepler.game.games.battleball.enums.BattleballTileType;
import org.alexdev.kepler.game.games.battleball.events.PlayerMoveEvent;
import org.alexdev.kepler.game.games.battleball.objects.PlayerUpdateObject;
import org.alexdev.kepler.game.games.gamehalls.GameBattleShip;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.mapping.RoomTile;
import org.alexdev.kepler.messages.outgoing.games.GAMESTATUS;
import org.alexdev.kepler.util.schedule.FutureRunnable;

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

        while (isValidGameTile(gamePlayer, (BattleballTile) game.getTile(nextPosition.getX(), nextPosition.getY()))) {
            nextPosition = nextPosition.getSquareInFront();

            if (!isValidGameTile(gamePlayer, (BattleballTile) game.getTile(nextPosition.getX(), nextPosition.getY()))) {
                break;
            }

            tilesToUpdate.add((BattleballTile) game.getTile(nextPosition.getX(), nextPosition.getY()));

        }

        if (tilesToUpdate.isEmpty()) {
            nextPosition = gamePlayer.getPlayer().getRoomUser().getPosition();
            tilesToUpdate.add((BattleballTile) game.getTile(nextPosition.getX(), nextPosition.getY()));
        }

        //gamePlayer.setPlayerState(BattleballPlayerState.CLIMBING_INTO_CANNON);
        //game.getObjectsQueue().add(new PlayerUpdateObject(gamePlayer));

        for (BattleballTile tile : tilesToUpdate) {
            if (tile.getState() == BattleballTileType.SEALED) {
                continue;
            }

            if (tile.getColour() == BattleballColourType.DISABLED) {
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
        }

        BattleballTile lastTile = tilesToUpdate.getLast();

        Position lastPosition = lastTile.getPosition().copy();
        lastPosition.setRotation(rotation);

        gamePlayer.setPlayerState(BattleballPlayerState.FLYING_THROUGH_AIR);
        game.getObjectsQueue().add(new PlayerUpdateObject(gamePlayer));
        game.getEventsQueue().add(new PlayerMoveEvent(gamePlayer, lastPosition));

        GameScheduler.getInstance().getSchedulerService().schedule(() -> {
            finishedCannon(game, gamePlayer, lastPosition);
        }, 1000, TimeUnit.MILLISECONDS);

        gamePlayer.getPlayer().getRoomUser().warp(lastTile.getPosition(), false);

    }

    private static void finishedCannon(Game game, GamePlayer gamePlayer, Position lastPosition) {
        gamePlayer.setPlayerState(BattleballPlayerState.STUNNED);
        gamePlayer.getPlayer().getRoomUser().setPosition(lastPosition);
        game.getObjectsQueue().add(new PlayerUpdateObject(gamePlayer));

        // Restore player 5 seconds later
        GameScheduler.getInstance().getSchedulerService().schedule(()-> {
            gamePlayer.getPlayer().getRoomUser().setWalkingAllowed(true);

            gamePlayer.setPlayerState(BattleballPlayerState.NORMAL);
            game.getObjectsQueue().add(new PlayerUpdateObject(gamePlayer));
        }, 5, TimeUnit.SECONDS);
    }

    public static boolean isValidGameTile(GamePlayer gamePlayer, BattleballTile tile) {
        if (tile == null) {// && tile.getColour() != BattleballColourType.DISABLED;
            return false;
        }

        return RoomTile.isValidTile(gamePlayer.getGame().getRoom(), gamePlayer.getPlayer(), tile.getPosition());
    }
}
