package org.alexdev.kepler.game.games.battleball.events;

import org.alexdev.kepler.game.games.GameEvent;
import org.alexdev.kepler.game.games.battleball.BattleballPowerUp;
import org.alexdev.kepler.game.games.enums.GameEventType;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class ActivatePowerUpEvent extends GameEvent {
    private final BattleballPowerUp powerUp;
    private final GamePlayer gamePlayer;

    public ActivatePowerUpEvent(GamePlayer gamePlayer, BattleballPowerUp powerUp) {
        super(GameEventType.BATTLEBALL_POWERUP_ACTIVATE);
        this.gamePlayer = gamePlayer;
        this.powerUp = powerUp;
    }

    @Override
    public void serialiseEvent(NettyResponse response) {
        response.writeInt(this.gamePlayer.getPlayer().getRoomUser().getInstanceId());
        response.writeInt(this.powerUp.getId());
        response.writeInt(this.gamePlayer.getPlayer().getRoomUser().getPosition().getRotation());
        response.writeInt(this.powerUp.getPowerType().getPowerUpId());
    }
}
