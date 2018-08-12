package org.alexdev.kepler.game.item.triggers;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.base.ItemBehaviour;
import org.alexdev.kepler.game.games.GamehallGame;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.RoomUser;
import org.alexdev.kepler.game.room.mapping.RoomTile;
import org.alexdev.kepler.messages.outgoing.rooms.games.CLOSEGAMEBOARD;
import org.alexdev.kepler.messages.outgoing.rooms.games.OPENGAMEBOARD;

import java.util.ArrayList;
import java.util.List;

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

        if (instance == null || (instance.getPlayers().size() >= instance.getMaximumPeopleRequired())) {
            return; // Game already started
        }

        instance.getPlayers().add(player);

        if (instance.getGameId() != null) {
            player.send(new OPENGAMEBOARD(instance.getGameId(), instance.getGameFuseType())); // Player joined mid-game
        } else {
            if (instance.hasPlayersRequired()) {
                instance.createGameId();
                instance.sendToEveryone(new OPENGAMEBOARD(instance.getGameId(), instance.getGameFuseType()));
            }
        }
    }

    @Override
    public void onEntityLeave(Entity entity, RoomUser roomUser, Item item, Object... customArgs) {
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player) entity;
        GamehallGame instance = this.getGameInstance(item.getPosition());

        if (instance == null || instance.getGameId() == null) {
            return; // Game hasn't started
        }

        instance.getPlayers().remove(player);

        if (instance.hasPlayersRequired()) {
            player.send(new CLOSEGAMEBOARD(instance.getGameId(), instance.getGameFuseType()));
        } else {
            instance.sendToEveryone(new CLOSEGAMEBOARD(instance.getGameId(), instance.getGameFuseType()));
            instance.resetGameId();
            //instance.removePlayers();
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
            for (RoomTile roomTile : instances.getTiles()) {
                if (roomTile.getPosition().equals(position)) {
                    return instances;
                }
            }
        }

        return null;
    }

    /**
     * Gets the list of seats and their pairs as coordinates
     */
    public abstract List<List<int[]>> getChairGroups();
}
