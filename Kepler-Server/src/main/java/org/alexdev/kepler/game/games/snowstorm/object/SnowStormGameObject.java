package org.alexdev.kepler.game.games.snowstorm.object;

import org.alexdev.kepler.game.games.GameObject;
import org.alexdev.kepler.game.games.enums.GameObjectType;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

import java.util.ArrayList;
import java.util.List;

public abstract class SnowStormGameObject extends GameObject {
    private final List<Integer> pGameObjectsSyncValues;

    public SnowStormGameObject(GameObjectType gameObjectType) {
        super(gameObjectType);
        this.pGameObjectsSyncValues = new ArrayList<>();
    }

    public List<Integer> getGameObjectsSyncValues() {
        return pGameObjectsSyncValues;
    }
}
