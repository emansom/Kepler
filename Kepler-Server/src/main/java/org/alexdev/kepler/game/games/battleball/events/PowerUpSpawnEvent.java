package org.alexdev.kepler.game.games.battleball.events;

import org.alexdev.kepler.game.games.GameEvent;
import org.alexdev.kepler.game.games.battleball.BattleballGame;
import org.alexdev.kepler.game.games.battleball.enums.BattleballPowerType;
import org.alexdev.kepler.game.games.enums.GameEventType;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

import java.util.concurrent.ThreadLocalRandom;

public class PowerUpSpawnEvent extends GameEvent {
    private final int powerId;
    private final BattleballGame game;
    private final BattleballPowerType powerType;

    public PowerUpSpawnEvent(int powerId, BattleballGame game, BattleballPowerType powerType) {
        super(GameEventType.BATTLEBALL_POWERUP_SPAWN);
        this.powerId = powerId;
        this.game = game;
        this.powerType = powerType;
    }

    @Override
    public void serialiseEvent(NettyResponse response) {
        response.writeInt(1);
        response.writeInt(this.powerId);
        response.writeInt(10);
        response.writeInt(-1);
        response.writeInt(this.powerType.getPowerUpId());
        response.writeInt(ThreadLocalRandom.current().nextInt(0, 20));
        response.writeInt(ThreadLocalRandom.current().nextInt(0, 20));
        response.writeInt(ThreadLocalRandom.current().nextInt(0, 1));
    }
}
