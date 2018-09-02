package org.alexdev.kepler.game.games.snowstorm;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.games.Game;
import org.alexdev.kepler.game.games.GameEvent;
import org.alexdev.kepler.game.games.GameObject;
import org.alexdev.kepler.game.games.battleball.BattleballGame;
import org.alexdev.kepler.game.games.battleball.BattleballPowerUp;
import org.alexdev.kepler.game.games.battleball.BattleballTile;
import org.alexdev.kepler.game.games.battleball.events.PlayerMoveEvent;
import org.alexdev.kepler.game.games.battleball.objects.PlayerObject;
import org.alexdev.kepler.game.games.battleball.objects.PowerObject;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.games.player.GameTeam;
import org.alexdev.kepler.game.games.snowstorm.object.SnowStormPlayerObject;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.pathfinder.Rotation;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.entities.RoomEntity;
import org.alexdev.kepler.game.room.enums.StatusType;
import org.alexdev.kepler.game.room.mapping.RoomTile;
import org.alexdev.kepler.log.Log;
import org.alexdev.kepler.messages.outgoing.games.GAMESTATUS;
import org.alexdev.kepler.messages.outgoing.games.SNOWSTORM_GAMESTATUS;
import org.alexdev.kepler.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class SnowstormUpdateTask implements Runnable {
    private final Room room;
    private final SnowStormGame game;

    public SnowstormUpdateTask(Room room, SnowStormGame game) {
        this.room = room;
        this.game = game;
    }

    @Override
    public void run() {
        try {
            if (this.game.isGameFinished()) {
                return; // Don't send any packets or do any logic checks during when the game is finished
            }

            List<GameObject> objects = new ArrayList<>();
            List<GameEvent> events = new ArrayList<>();

            this.game.getEventsQueue().drainTo(events);
            this.game.getObjectsQueue().drainTo(objects);

            // for (BattleballPowerUp battleballPowerUp : this.game.getActivePowers()) {
            //     objects.add(new PowerObject(battleballPowerUp));
            // }

            List<BattleballTile> updateTiles = new ArrayList<>();
            List<BattleballTile> fillTiles = new ArrayList<>();

            for (GameTeam gameTeam : this.game.getTeams().values()) {
                for (GamePlayer gamePlayer : gameTeam.getActivePlayers()) {
                    Player player = gamePlayer.getPlayer();

                    if (player != null
                            && player.getRoomUser().getRoom() != null
                            && player.getRoomUser().getRoom() == this.room) {

                        this.processEntity(gamePlayer, objects, events, updateTiles, fillTiles);
                        RoomEntity roomEntity = player.getRoomUser();

                        objects.add(new SnowStormPlayerObject(gamePlayer));

                        if (roomEntity.isNeedsUpdate()) {
                            roomEntity.setNeedsUpdate(false);
                        }
                    }
                }
            }

            this.game.getTurnContainer().calculateChecksum(objects);
            this.game.getTurnContainer().getCurrentTurn().incrementAndGet();

            this.game.send(new SNOWSTORM_GAMESTATUS(this.game, this.game.getTeams().values(), objects, events));
        } catch (Exception ex) {
            Log.getErrorLogger().error("SnowstormUpdateTask crashed: ", ex);
        }
    }

    /**
     * Process entity.
     */
    private void processEntity(GamePlayer gamePlayer, List<GameObject> objects, List<GameEvent> events, List<BattleballTile> updateTiles, List<BattleballTile> fillTiles) {
        Entity entity = (Entity) gamePlayer.getPlayer();
        Game game = gamePlayer.getGame();

        RoomEntity roomEntity = entity.getRoomUser();

        Position position = roomEntity.getPosition();
        Position goal = roomEntity.getGoal();

        if (roomEntity.isWalking()) {
            // Apply next tile from the tile we removed from the list the cycle before
            if (roomEntity.getNextPosition() != null) {
                roomEntity.getPosition().setX(roomEntity.getNextPosition().getX());
                roomEntity.getPosition().setY(roomEntity.getNextPosition().getY());
                roomEntity.updateNewHeight(roomEntity.getPosition());
            }

            // We still have more tiles left, so lets continue moving
            if (roomEntity.getPath().size() > 0) {
                Position next = roomEntity.getPath().pop();

                // Tile was invalid after we started walking, so lets try again!
                if (!RoomTile.isValidTile(this.room, entity, next)) {
                    entity.getRoomUser().getPath().clear();
                    roomEntity.walkTo(goal.getX(), goal.getY());
                    this.processEntity(gamePlayer, objects, events, updateTiles, fillTiles);
                    return;
                }

                RoomTile previousTile = roomEntity.getTile();
                previousTile.removeEntity(entity);

                RoomTile nextTile = roomEntity.getRoom().getMapping().getTile(next);
                nextTile.addEntity(entity);

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
