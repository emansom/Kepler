package org.alexdev.kepler.game.games.snowstorm.object;

import org.alexdev.kepler.game.games.enums.GameObjectType;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.games.snowstorm.SnowStormGameObject;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class SnowStormSpawnPlayerEvent extends SnowStormGameObject {
    private final GamePlayer gamePlayer;

    public SnowStormSpawnPlayerEvent(GamePlayer gamePlayer) {
        super(GameObjectType.SNOWSTORM_SPAWN_PLAYER_EVENT);
        this.gamePlayer = gamePlayer;
        //this.getGameObjectsSyncValues().add(gamePlayer.getPlayer().getRoomUser().getInstanceId());
        //this.getGameObjectsSyncValues().add(gamePlayer.getPlayer().getRoomUser().getPosition().getX());
        //this.getGameObjectsSyncValues().add(gamePlayer.getPlayer().getRoomUser().getPosition().getY());
    }

    @Override
    public void serialiseObject(NettyResponse response) {
        response.writeInt(5);
        //new SnowStormPlayerObject(this.gamePlayer).serialiseObject(response);
    }
}
