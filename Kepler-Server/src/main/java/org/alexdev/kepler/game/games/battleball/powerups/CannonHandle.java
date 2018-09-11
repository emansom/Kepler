package org.alexdev.kepler.game.games.battleball.powerups;

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
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CannonHandle {
    public static void handle(BattleballGame game, GamePlayer gamePlayer, Room room) {
        gamePlayer.getPlayer().getRoomUser().stopWalking();
        gamePlayer.getPlayer().getRoomUser().setWalkingAllowed(false);

        Position firstPosition = gamePlayer.getPlayer().getRoomUser().getPosition();

        Position nextPosition = firstPosition.copy();
        int rotation = nextPosition.getRotation();

        LinkedList<BattleballTile> tilesToUpdate = new LinkedList<>();
        List<Pair<GamePlayer, Position>> stunnedPlayers = new ArrayList<>();

        while (TileUtil.isValidGameTile(gamePlayer, (BattleballTile) game.getTile(nextPosition.getX(), nextPosition.getY()), false)) {
            nextPosition = nextPosition.getSquareInFront();

            if (!TileUtil.isValidGameTile(gamePlayer, (BattleballTile) game.getTile(nextPosition.getX(), nextPosition.getY()), false)) {
                break;
            }

            BattleballTile battleballTile = (BattleballTile) game.getTile(nextPosition.getX(), nextPosition.getY());

            tilesToUpdate.add(battleballTile);

            for (GamePlayer p : battleballTile.getPlayers(gamePlayer)) {
                stunnedPlayers.add(Pair.of(p, nextPosition));
            }
        }

        if (tilesToUpdate.isEmpty()) {
            nextPosition = gamePlayer.getPlayer().getRoomUser().getPosition();
            tilesToUpdate.add((BattleballTile) game.getTile(nextPosition.getX(), nextPosition.getY()));
        }

        // Stun players in direction of cannon and make them move out of the way
        GameScheduler.getInstance().getSchedulerService().schedule(() -> {
            for (var kvp : stunnedPlayers) {
                try {
                    // TODO: Move player out of the way of user using cannon https://www.youtube.com/watch?v=YX1UZky5pg0&feature=youtu.be&t=98
                    GamePlayer stunnedPlayer = kvp.getKey();
                    Position pushedFrom = kvp.getValue().copy();
                    pushedFrom.setRotation(rotation);

                    List<Position> pushedTo = new ArrayList<>();
                    pushedTo.add(pushedFrom.getSquareRight());
                    pushedTo.add(pushedFrom.getSquareLeft());

                    Position setPosition = null;

                    // Find best position to move player to
                    for (Position position : pushedTo) {
                        if (TileUtil.isValidGameTile(stunnedPlayer, (BattleballTile) game.getTile(position.getX(), position.getY()), true)) {
                            setPosition = position;
                            break;
                        }
                    }

                    if (setPosition != null) {
                        game.getEventsQueue().add(new PlayerMoveEvent(stunnedPlayer, setPosition));
                    }

                    // Stun player
                    PowerUpUtil.stunPlayer(game, stunnedPlayer, BattleballPlayerState.STUNNED);

                    // Set player at teir new spot
                    if (setPosition != null) {
                        setPosition.setRotation(stunnedPlayer.getPlayer().getRoomUser().getPosition().getRotation());
                        stunnedPlayer.getPlayer().getRoomUser().setPosition(setPosition);
                        stunnedPlayer.getPlayer().getRoomUser().warp(setPosition, false);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, 200, TimeUnit.MILLISECONDS);


        for (BattleballTile tile : tilesToUpdate) {
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

        gamePlayer.getPlayer().getRoomUser().setPosition(firstPosition);
        gamePlayer.setPlayerState(BattleballPlayerState.FLYING_THROUGH_AIR);

        game.getObjectsQueue().add(new PlayerUpdateObject(gamePlayer));
        game.getEventsQueue().add(new PlayerMoveEvent(gamePlayer, lastPosition));

        GameScheduler.getInstance().getSchedulerService().schedule(() -> {
            gamePlayer.getPlayer().getRoomUser().setPosition(lastPosition);
            PowerUpUtil.stunPlayer(game, gamePlayer, BattleballPlayerState.STUNNED);
        }, 800, TimeUnit.MILLISECONDS);

        gamePlayer.getPlayer().getRoomUser().warp(lastTile.getPosition(), false);

    }
}
