package org.alexdev.kepler.game.games.snowstorm.object;

import org.alexdev.kepler.game.games.enums.GameObjectType;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.games.snowstorm.SnowStormGameObject;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class SnowStormSpawnPlayerEvent extends SnowStormGameObject {
    private final GamePlayer gamePlayer;

    public SnowStormSpawnPlayerEvent(GamePlayer gamePlayer) {
        super(GameObjectType.SNOWSTORM_SPAWN_PLAYER_ONE);
        this.gamePlayer = gamePlayer;
        this.getGameObjectsSyncValues().add(GameObjectType.SNOWSTORM_SPAWN_PLAYER_ONE.getObjectId());
        //this.getGameObjectsSyncValues().add(gamePlayer.getPlayer().getRoomUser().getRoom().getId());
        this.getGameObjectsSyncValues().add(gamePlayer.getPlayer().getRoomUser().getInstanceId());
        this.getGameObjectsSyncValues().add(gamePlayer.getPlayer().getRoomUser().getRoom().getId());
        this.getGameObjectsSyncValues().add(gamePlayer.getPlayer().getDetails().getId());
    }

    @Override
    public void serialiseObject(NettyResponse response) {
        //response.writeInt(GameObjectType.SNOWSTORM_SPAWN_PLAYER_ONE.getObjectId());
        response.writeInt(gamePlayer.getPlayer().getRoomUser().getInstanceId());
        response.writeInt(gamePlayer.getPlayer().getRoomUser().getRoom().getId());
        response.writeInt(gamePlayer.getPlayer().getDetails().getId());
    }
}
