package org.alexdev.kepler.game.games.snowstorm.events;

import org.alexdev.kepler.game.games.enums.GameObjectType;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.games.snowstorm.object.SnowStormGameObject;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class SnowStormSpawnPlayer extends SnowStormGameObject {
    private final GamePlayer gamePlayer;

    public SnowStormSpawnPlayer(GamePlayer gamePlayer) {
        super(GameObjectType.SNOWWAR_OBJECT_EVENT);
        this.gamePlayer = gamePlayer;
    }

    @Override
    public void serialiseObject(NettyResponse response) {
        response.writeInt(GameObjectType.SNOWWAR_OBJECT_EVENT.getObjectId());

        response.writeInt(0);
        response.writeInt(gamePlayer.getPlayer().getDetails().getId());
        response.writeInt(gamePlayer.getPlayer().getDetails().getId());// Actually room user id/instance id
    }
}
