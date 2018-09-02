package org.alexdev.kepler.game.games.battleball.objects;

import org.alexdev.kepler.game.games.GameObject;
import org.alexdev.kepler.game.games.battleball.BattleballPowerUp;
import org.alexdev.kepler.game.games.enums.GameObjectType;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class PowerObject extends GameObject {
    private final GamePlayer gamePlayer;
    private final BattleballPowerUp powerUp;

    public PowerObject(GamePlayer gamePlayer, BattleballPowerUp powerUp) {
        super(GameObjectType.BATTLEBALL_POWER_OBJECT);
        this.gamePlayer = gamePlayer;
        this.powerUp = powerUp;
    }

    @Override
    public void serialiseObject(NettyResponse response) {
        response.writeInt(this.powerUp.getId());
        response.writeInt(this.powerUp.getTimeToDespawn().get());
        response.writeInt(this.gamePlayer.getPlayer().getRoomUser().getInstanceId());
    }
}
