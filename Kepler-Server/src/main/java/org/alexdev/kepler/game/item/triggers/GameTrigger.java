package org.alexdev.kepler.game.item.triggers;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.base.ItemBehaviour;
import org.alexdev.kepler.game.item.games.GameTicTacToe;
import org.alexdev.kepler.game.item.games.GamehallGame;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.RoomUser;
import org.alexdev.kepler.game.room.mapping.RoomTile;
import org.alexdev.kepler.messages.outgoing.rooms.games.CLOSEGAMEBOARD;
import org.alexdev.kepler.messages.outgoing.rooms.games.OPENGAMEBOARD;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class GameTrigger implements ItemTrigger {
    protected List<GamehallGame> gameInstances;

    public GameTrigger() {
        this.gameInstances = new ArrayList<>();
    }

    @Override
    public void onEntityStep(Entity entity, RoomUser roomUser, Item item, Object... customArgs) {

    }

    @Override
    public void onEntityStop(Entity entity, RoomUser roomUser, Item item, Object... customArgs) {
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player) entity;

        // Call default sitting trigger
        ItemBehaviour.CAN_SIT_ON_TOP.getTrigger().onEntityStop(entity, roomUser, item, customArgs);

        // Handle game logic from here
        GamehallGame instance = this.getGameInstance(item.getPosition());
        RoomTile opponentTile = instance.getOpponentTile(roomUser.getPosition());

        if (instance.getGameId() != null) {
            return; // Game already started
        }

        instance.addPlayer(player);

        // Open gameboard if there's a user in the other tile
        if (opponentTile.getEntities().size() > 0) {
            instance.createGameId();
            instance.addPlayer((Player) opponentTile.getEntities().get(0));

            player.send(new OPENGAMEBOARD(instance.getGameId(), this.getGameFuseType()));
            instance.getOpponent(player).send(new OPENGAMEBOARD(instance.getGameId(), this.getGameFuseType()));
        }
    }

    @Override
    public void onEntityLeave(Entity entity, RoomUser roomUser, Item item, Object... customArgs) {
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player) entity;
        GamehallGame instance = this.getGameInstance(item.getPosition());

        if (instance == null) {
            return;
        }

        // Close the game
        if (instance.getGameId() != null) {
            player.send(new CLOSEGAMEBOARD(instance.getGameId(), this.getGameFuseType()));

            Player opponent = instance.getOpponent(player);

            if (opponent != null) {
                opponent.send(new CLOSEGAMEBOARD(instance.getGameId(), this.getGameFuseType()));
            }

            instance.resetGameId();
            instance.removePlayers();
        }
    }

    /**
     * Gets the game instance on this specified position.
     *
     * @param position the position to look for the game instance
     * @return the game instance, if successful
     */
    public GamehallGame getGameInstance(Position position) {
        for (GamehallGame instances : this.gameInstances) {
            if (instances.getChairPosition().equals(position) ||
                    instances.getOpponentPosition().equals(position)) {
                return instances;
            }
        }

        return null;
    }

    /**
     * Gets the list of seats and their pairs as coordinates
     */
    public abstract Map<int[], int[]> getChairPairs();

    /**
     * Get FUSE game type
     */
    public abstract String getGameFuseType();
}
