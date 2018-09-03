package org.alexdev.kepler.game.games.snowstorm.events;

import org.alexdev.kepler.game.games.enums.GameObjectType;
import org.alexdev.kepler.game.games.snowstorm.object.SnowStormGameObject;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

import java.util.List;

public class SnowStormObjectEvent extends SnowStormGameObject {
    private final SnowStormGameObject obj;

    public SnowStormObjectEvent(SnowStormGameObject obj) {
        super(GameObjectType.SNOWWAR_OBJECT_EVENT);
        this.obj = obj;
    }

    @Override
    public void serialiseObject(NettyResponse response) {
        response.writeInt(GameObjectType.SNOWWAR_OBJECT_EVENT.getObjectId());
        this.obj.serialiseObject(response);
    }
}
