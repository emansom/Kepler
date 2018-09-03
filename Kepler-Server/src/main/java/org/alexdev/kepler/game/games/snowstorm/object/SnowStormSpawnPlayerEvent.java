package org.alexdev.kepler.game.games.snowstorm.object;

import org.alexdev.kepler.game.games.enums.GameObjectType;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.games.snowstorm.SnowStormGameObject;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class SnowStormSpawnPlayerEvent extends SnowStormGameObject {
    private final GamePlayer gamePlayer;
    private final SnowStormPlayerObject obj;// = new SnowStormPlayerObject(gamePlayer);

    public SnowStormSpawnPlayerEvent(GamePlayer gamePlayer) {
        super(GameObjectType.SNOWSTORM_SPAWN_PLAYER_ONE);
        this.gamePlayer = gamePlayer;

        this.obj = new SnowStormPlayerObject(gamePlayer);
        this.getGameObjectsSyncValues().addAll(this.obj.getGameObjectsSyncValues());
    }

    @Override
    public void serialiseObject(NettyResponse response) {
        response.writeInt(0);//GameObjectType.SNOWSTORM_SPAWN_PLAYER_ONE.getObjectId());

        response.writeInt(GameObjectType.SNOWSTORM_SPAWN_PLAYER_ONE.getObjectId());
        obj.serialiseObject(response);

        //response.writeInt(0);
    }
}
