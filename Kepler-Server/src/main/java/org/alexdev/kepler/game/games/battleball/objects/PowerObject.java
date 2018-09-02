package org.alexdev.kepler.game.games.battleball.objects;

import org.alexdev.kepler.game.games.GameObject;
import org.alexdev.kepler.game.games.battleball.BattleballGame;
import org.alexdev.kepler.game.games.battleball.BattleballPowerUp;
import org.alexdev.kepler.game.games.battleball.enums.BattleballPowerType;
import org.alexdev.kepler.game.games.enums.GameObjectType;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

import java.util.concurrent.ThreadLocalRandom;

public class PowerObject extends GameObject {
    private final BattleballPowerUp powerUp;

    public PowerObject(BattleballPowerUp powerUp) {
        super(GameObjectType.BATTLEBALL_POWER_OBJECT);
        this.powerUp = powerUp;
    }

    @Override
    public void serialiseObject(NettyResponse response) {
        response.writeInt(this.powerUp.getId());
        response.writeInt(this.powerUp.getTimeToDespawn().get());
        response.writeInt(this.powerUp.getPlayerHolding());
        response.writeInt(this.powerUp.getPowerType().getPowerUpId());
        response.writeInt(this.powerUp.getPosition().getX());
        response.writeInt(this.powerUp.getPosition().getX());
        response.writeInt((int) this.powerUp.getPosition().getZ());
    }
}
