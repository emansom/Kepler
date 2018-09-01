package org.alexdev.kepler.game.games.battleball.objects;

import org.alexdev.kepler.game.games.GameObject;
import org.alexdev.kepler.game.games.battleball.BattleballGame;
import org.alexdev.kepler.game.games.battleball.enums.BattleballPowerType;
import org.alexdev.kepler.game.games.enums.GameObjectType;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

import java.util.concurrent.ThreadLocalRandom;

public class BattleballPowerObject extends GameObject {
    private final int powerId;
    private final BattleballGame battleballGame;
    private final BattleballPowerType powerType;

    public BattleballPowerObject(int powerId, BattleballGame battleballGame, BattleballPowerType powerType) {
        super(GameObjectType.BATTLEBALL_POWER_OBJECT);
        this.powerId = powerId;
        this.battleballGame = battleballGame;
        this.powerType = powerType;
    }

    @Override
    public void serialiseObject(NettyResponse response) {
        response.writeInt(this.powerId);
        response.writeInt(10);
        response.writeInt(-1);
        response.writeInt(this.powerType.getPowerUpId());
        response.writeInt(10);//ThreadLocalRandom.current().nextInt(0, 20));
        response.writeInt(11);//ThreadLocalRandom.current().nextInt(0, 20));
        response.writeInt(1);//ThreadLocalRandom.current().nextInt(0, 1));
    }
}
