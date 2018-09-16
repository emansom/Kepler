package org.alexdev.kepler.game.games.battleball;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.games.Game;
import org.alexdev.kepler.game.games.GameEvent;
import org.alexdev.kepler.game.games.GameObject;
import org.alexdev.kepler.game.games.battleball.enums.BattleBallPlayerState;
import org.alexdev.kepler.game.games.battleball.events.PlayerMoveEvent;
import org.alexdev.kepler.game.games.battleball.objects.PlayerUpdateObject;
import org.alexdev.kepler.game.games.battleball.objects.PowerUpUpdateObject;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.pathfinder.Rotation;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.entities.RoomEntity;
import org.alexdev.kepler.game.room.enums.StatusType;
import org.alexdev.kepler.game.room.mapping.RoomTile;
import org.alexdev.kepler.log.Log;
import org.alexdev.kepler.messages.outgoing.games.GAMESTATUS;
import org.alexdev.kepler.server.netty.NettyPlayerNetwork;
import org.alexdev.kepler.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class BattleBallTask implements Runnable {
    private final Room room;
    private final BattleBallGame game;

    public BattleBallTask(Room room, BattleBallGame game) {
        this.room = room;
        this.game = game;
    }

    @Override
    public void run() {
        try {
            if ( this.game.getPlayers().isEmpty()) {
                return; // Don't send any packets or do any logic checks during when the game is finished
            }

            List<GameObject> objects = new ArrayList<>();
            List<GameEvent> events = new ArrayList<>();

            List<BattleBallTile> updateTiles = new ArrayList<>();
            List<BattleBallTile> fillTiles = new ArrayList<>();

            this.game.getEventsQueue().drainTo(events);
            this.game.getObjectsQueue().drainTo(objects);
            this.game.getUpdateTilesQueue().drainTo(updateTiles);
            this.game.getFillTilesQueue().drainTo(fillTiles);

            for (GamePlayer gamePlayer : this.game.getPlayers()) {
                Player player = gamePlayer.getPlayer();

                if (player.getRoomUser().getRoom() != this.room) {
                    continue;
                }

                if (gamePlayer.getPlayerState() == BattleBallPlayerState.CLIMBING_INTO_CANNON ||
                    gamePlayer.getPlayerState() == BattleBallPlayerState.FLYING_THROUGH_AIR) {
                    continue;
                }

                if (this.game.getStoredPowers().containsKey(gamePlayer)) {
                    for (BattleBallPowerUp powerUp : this.game.getStoredPowers().get(gamePlayer)) {
                        objects.add(new PowerUpUpdateObject(powerUp));
                    }
                }

                this.processEntity(gamePlayer, objects, events, updateTiles, fillTiles);

                objects.add(new PlayerUpdateObject(gamePlayer));
            }

            this.game.send(new GAMESTATUS(this.game, this.game.getTeams().values(), objects, events, updateTiles, fillTiles));
        } catch (Exception ex) {
            Log.getErrorLogger().error("GameTask crashed: ", ex);
        }
    }

    /**
     * Process entity.
     */
    private void processEntity(GamePlayer gamePlayer, List<GameObject> objects, List<GameEvent> events, List<BattleBallTile> updateTiles, List<BattleBallTile> fillTiles) {
        Entity entity = (Entity) gamePlayer.getPlayer();
        Game game = gamePlayer.getGame();

        RoomEntity roomEntity = entity.getRoomUser();

        Position position = roomEntity.getPosition();
        Position goal = roomEntity.getGoal();

        if (roomEntity.isWalking()) {
            // Apply next tile from the tile we removed from the list the cycle before
            if (roomEntity.getNextPosition() != null) {
                boolean interact = true;

                if (!RoomTile.isValidTile(this.room, entity, roomEntity.getNextPosition())) {
                    RoomTile nextTile = roomEntity.getRoom().getMapping().getTile(roomEntity.getPosition());
                    nextTile.addEntity(entity);

                    roomEntity.stopWalking();
                    interact = false;
                }

                if (roomEntity.getNextPosition() != null) {
                    RoomTile nextTile = roomEntity.getRoom().getMapping().getTile(roomEntity.getNextPosition());
                    nextTile.addEntity(entity);

                    roomEntity.getPosition().setX(roomEntity.getNextPosition().getX());
                    roomEntity.getPosition().setY(roomEntity.getNextPosition().getY());
                    roomEntity.updateNewHeight(roomEntity.getNextPosition());

                    if (interact) {
                        // Increment tiles...
                        BattleBallTile tile = (BattleBallTile) game.getTile(roomEntity.getNextPosition().getX(), roomEntity.getNextPosition().getY());

                        if (tile != null) {
                            tile.interact(gamePlayer, objects, events, updateTiles, fillTiles);
                        }
                    }
                }
            }

            // We still have more tiles left, so lets continue moving
            if (roomEntity.getPath().size() > 0) {
                Position next = roomEntity.getPath().pop();

                RoomTile previousTile = roomEntity.getTile();
                previousTile.removeEntity(entity);

                roomEntity.removeStatus(StatusType.LAY);
                roomEntity.removeStatus(StatusType.SIT);

                int rotation = Rotation.calculateWalkDirection(position.getX(), position.getY(), next.getX(), next.getY());
                double height = this.room.getMapping().getTile(next).getWalkingHeight();

                roomEntity.getPosition().setRotation(rotation);
                roomEntity.setStatus(StatusType.MOVE, next.getX() + "," + next.getY() + "," + StringUtil.format(height));
                roomEntity.setNextPosition(next);

                // Add next position if moving
                events.add(new PlayerMoveEvent(gamePlayer, roomEntity.getNextPosition().copy()));
            } else {
                roomEntity.stopWalking();
            }

            // If we're walking, make sure to tell the server
            roomEntity.setNeedsUpdate(true);
        }
    }
}
