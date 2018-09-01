package org.alexdev.kepler.game.games.battleball.events;

import org.alexdev.kepler.game.games.GameEvent;
import org.alexdev.kepler.game.games.battleball.BattleballGame;
import org.alexdev.kepler.game.games.battleball.BattleballPowerUp;
import org.alexdev.kepler.game.games.enums.GameEventType;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class PowerUpSpawnEvent extends GameEvent {
    private final BattleballGame game;
    private final BattleballPowerUp powerUp;

    public PowerUpSpawnEvent(BattleballGame battleballGame, BattleballPowerUp powerUp) {
        super(GameEventType.BATTLEBALL_POWERUP_SPAWN);
        this.game = battleballGame;
        this.powerUp = powerUp;
    }

    @Override
    public void serialiseEvent(NettyResponse response) {
        response.writeInt(1);
        response.writeInt(this.powerUp.getId());
        response.writeInt(10);
        response.writeInt(-1);
        response.writeInt(this.powerUp.getPowerType().getPowerUpId());
        response.writeInt(this.powerUp.getPosition().getX());
        response.writeInt(this.powerUp.getPosition().getY());
        response.writeInt((int) this.powerUp.getPosition().getZ());
    }
}
