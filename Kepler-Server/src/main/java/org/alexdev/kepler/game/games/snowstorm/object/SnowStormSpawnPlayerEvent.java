package org.alexdev.kepler.game.games.snowstorm.object;

import org.alexdev.kepler.game.games.enums.GameObjectType;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.games.snowstorm.SnowStormGameObject;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class SnowStormSpawnPlayerEvent extends SnowStormGameObject {
    private final GamePlayer gamePlayer;

    public SnowStormSpawnPlayerEvent(GamePlayer gamePlayer) {
        super(GameObjectType.SNOWWAR_OBJECT);
        this.gamePlayer = gamePlayer;
        this.getGameObjectsSyncValues().addAll(new SnowStormPlayerObject(gamePlayer).getGameObjectsSyncValues());
    }

    @Override
    public void serialiseObject(NettyResponse response) {
        //response.writeInt(GameObjectType.SNOWWAR_OBJECT.getObjectId());

        //response.writeInt(GameObjectType.SNOWSTORM_SPAWN_PLAYER_EVENT.getObjectId());
        //response.writeInt(this.gamePlayer.getPlayer().getDetails().getId());
        //response.writeInt(this.gamePlayer.getPlayer().getRoomUser().getRoom().getId());
        //response.writeInt(this.gamePlayer.getPlayer().getRoomUser().getInstanceId());
    }
}
