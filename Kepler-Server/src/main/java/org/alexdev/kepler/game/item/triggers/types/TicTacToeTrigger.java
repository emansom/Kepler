package org.alexdev.kepler.game.item.triggers.types;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.base.ItemBehaviour;
import org.alexdev.kepler.game.item.games.GameTicTacToe;
import org.alexdev.kepler.game.item.triggers.GameTrigger;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.RoomUser;
import org.alexdev.kepler.game.room.mapping.RoomTile;
import org.alexdev.kepler.messages.outgoing.rooms.games.CLOSEGAMEBOARD;
import org.alexdev.kepler.messages.outgoing.rooms.games.OPENGAMEBOARD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicTacToeTrigger extends GameTrigger {
    private static final Map<int[], int[]> gamePairs =  new HashMap<>() {{
        put(new int[] { 15, 4}, new int[] { 15, 5});
        put(new int[] { 15, 9}, new int[] { 15, 10});
        put(new int[] { 15, 14}, new int[] { 15, 15});
        put(new int[] { 10, 4}, new int[] { 10, 5});
        put(new int[] { 10, 9}, new int[] { 10, 10});
        put(new int[] { 10, 14}, new int[] { 10, 15});
        put(new int[] { 5, 4}, new int[] { 5, 5});
        put(new int[] { 5, 9}, new int[] { 5, 10});
        put(new int[] { 5, 14}, new int[] { 5, 15});
    }};

    private List<GameTicTacToe> gameInstances;

    public TicTacToeTrigger(int roomId) {
        this.gameInstances = new ArrayList<>();

        for (var kvp : gamePairs.entrySet()) {
            int[] chairPosition = kvp.getKey();
            int[] opponentPosition = kvp.getValue();

            this.gameInstances.add(new GameTicTacToe(roomId, chairPosition, opponentPosition));
        }
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
        GameTicTacToe instance = this.getGameInstance(item.getPosition());
        RoomTile opponentTile = instance.getOpponentTile(roomUser.getPosition());

        if (instance.getGameId() != null) {
            return; // Game already started
        }

        instance.addPlayer(player);

        // Open gameboard if there's a user in the other tile
        if (opponentTile.getEntities().size() > 0) {
            instance.createGameId();

            player.send(new OPENGAMEBOARD(instance.getGameId(), "TicTacToe"));
            instance.getOpponent(player).send(new OPENGAMEBOARD(instance.getGameId(), "TicTacToe"));
        }
    }

    @Override
    public void onEntityLeave(Entity entity, RoomUser roomUser, Item item, Object... customArgs) {
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player) entity;
        GameTicTacToe instance = this.getGameInstance(item.getPosition());

        if (instance == null) {
            return;
        }

        // Close the game
        if (instance.getGameId() != null) {
            player.send(new CLOSEGAMEBOARD(instance.getGameId(), "TicTacToe"));

            Player opponent = instance.getOpponent(player);

            if (opponent != null) {
                opponent.send(new CLOSEGAMEBOARD(instance.getGameId(), "TicTacToe"));
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
    @Override
    public GameTicTacToe getGameInstance(Position position) {
        for (GameTicTacToe instances : this.gameInstances) {
            if (instances.getChairPosition().equals(position) ||
                instances.getOpponentPosition().equals(position)) {
                return instances;
            }
        }

        return null;
    }
}
