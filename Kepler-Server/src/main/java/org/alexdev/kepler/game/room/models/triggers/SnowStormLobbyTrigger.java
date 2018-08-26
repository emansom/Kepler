package org.alexdev.kepler.game.room.models.triggers;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.games.GameType;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.triggers.GameLobbyTrigger;
import org.alexdev.kepler.game.triggers.GenericTrigger;
import org.alexdev.kepler.messages.outgoing.games.GAMEPLAYERINFO;
import org.alexdev.kepler.messages.outgoing.games.LOUNGEINFO;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;
import org.alexdev.kepler.util.config.GameConfiguration;

import java.util.List;
import java.util.Map;

public class SnowStormLobbyTrigger extends GameLobbyTrigger {
    @Override
    public void onRoomEntry(Entity entity, Room room, Object... customArgs) {
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }

        // Don't show panel and lounge info if create game is disabled
        if (!GameConfiguration.getInstance().getBoolean(this.getGameType().name().toLowerCase() + ".create.game.enabled")) {
            return;
        }

        Player player = (Player) entity;
        player.send(new LOUNGEINFO());

        player.send(new GAMEPLAYERINFO(this.getGameType(), room.getEntityManager().getPlayers()));
        room.send(new GAMEPLAYERINFO(this.getGameType(), List.of(player)));
    }

    @Override
    public void onRoomLeave(Entity entity, Room room, Object... customArgs) {
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player) entity;

    }

    @Override
    public void createGame(Player gameCreator, Map<String, Object> gameParameters) {

    }

    @Override
    public GameType getGameType() {
        return GameType.SNOWSTORM;
    }
}
