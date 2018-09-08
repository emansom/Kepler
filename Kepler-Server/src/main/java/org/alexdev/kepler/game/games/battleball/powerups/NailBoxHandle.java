package org.alexdev.kepler.game.games.battleball.powerups;

import org.alexdev.kepler.game.GameScheduler;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.games.Game;
import org.alexdev.kepler.game.games.GameObject;
import org.alexdev.kepler.game.games.battleball.BattleballGame;
import org.alexdev.kepler.game.games.battleball.BattleballTile;
import org.alexdev.kepler.game.games.battleball.enums.BattleballColourType;
import org.alexdev.kepler.game.games.battleball.enums.BattleballPlayerState;
import org.alexdev.kepler.game.games.battleball.events.DespawnObjectEvent;
import org.alexdev.kepler.game.games.battleball.events.PinSpawnEvent;
import org.alexdev.kepler.game.games.battleball.events.PlayerUpdateEvent;
import org.alexdev.kepler.game.games.battleball.objects.PinObject;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class NailBoxHandle {
    public static void handle(BattleballGame game, GamePlayer gamePlayer, Room room) {
        gamePlayer.getPlayer().getRoomUser().stopWalking();

        List<GamePlayer> dizzyPlayers = new ArrayList<>();
        List<GameObject> pins = new ArrayList<>();

        Position tilePosition = gamePlayer.getPlayer().getRoomUser().getPosition()
                .getSquareInFront()
                .getSquareInFront()
                .getSquareInFront();

        int maxPins = ThreadLocalRandom.current().nextInt(5, 12 + 1);
        List<Position> selectedPositions = new ArrayList<>();
        List<Position> circlePositions = tilePosition.getCircle(3);

        Collections.shuffle(circlePositions);

        for (Position circlePos : circlePositions) {
            if (circlePos.equals(gamePlayer.getPlayer().getRoomUser().getPosition())) {
                continue;
            }

            BattleballTile tile = (BattleballTile) game.getTile(circlePos.getX(), circlePos.getY());

            if (tile == null || tile.getColour() == BattleballColourType.DISABLED) {
                continue;
            }

            circlePos.setZ(tile.getPosition().getZ());

            if (selectedPositions.size() < maxPins) {
                if (ThreadLocalRandom.current().nextBoolean()) {
                    PinObject pin = new PinObject(game.createObjectId(), circlePos);
                    pins.add(pin);

                    game.getEventsQueue().add(new PinSpawnEvent(pin.getId(), pin.getPosition()));
                    selectedPositions.add(circlePos);

                    if (room.getMapping().getTile(circlePos).getEntities().size() > 0) {
                        for (Entity entity : room.getMapping().getTile(circlePos).getEntities()) {
                            if (entity.getType() != EntityType.PLAYER) {
                                continue;
                            }

                            Player player = (Player) entity;
                            GamePlayer gameUser = player.getRoomUser().getGamePlayer();

                            if (gameUser.isSpectator()) {
                                continue;
                            }

                            dizzyPlayers.add(player.getRoomUser().getGamePlayer());
                        }
                    }
                }
            }
        }

        game.getObjects().addAll(pins);

        // Make all affected players dizzy
        for (GamePlayer dizzyPlayer : dizzyPlayers) {
            dizzyPlayer.setPlayerState(BattleballPlayerState.BALL_BROKEN);
            dizzyPlayer.getPlayer().getRoomUser().stopWalking();
            dizzyPlayer.getPlayer().getRoomUser().setWalkingAllowed(false);

            game.getEventsQueue().add(new PlayerUpdateEvent(dizzyPlayer));
        }

        // Restore all players
        GameScheduler.getInstance().getSchedulerService().schedule(()-> {
            for (GamePlayer dizzyPlayer : dizzyPlayers) {
                dizzyPlayer.setPlayerState(BattleballPlayerState.NORMAL);
                dizzyPlayer.getPlayer().getRoomUser().setWalkingAllowed(true);

                game.getEventsQueue().add(new PlayerUpdateEvent(dizzyPlayer));
            }
        }, 5, TimeUnit.SECONDS);

        // Despawn all pins
        GameScheduler.getInstance().getSchedulerService().schedule(()-> {
            for (GameObject pinObject : pins) {
                game.getEventsQueue().add(new DespawnObjectEvent(pinObject.getId()));
                game.getObjects().remove(pinObject);
            }
        }, 10, TimeUnit.SECONDS);
    }
}
