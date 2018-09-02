package org.alexdev.kepler.game.games.snowstorm.object;

import org.alexdev.kepler.game.games.GameObject;
import org.alexdev.kepler.game.games.battleball.BattleballPowerUp;
import org.alexdev.kepler.game.games.enums.GameObjectType;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.games.snowstorm.SnowStormGameObject;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

import java.util.ArrayList;
import java.util.List;

public class SnowStormPlayerObject extends SnowStormGameObject {
    private final GamePlayer gamePlayer;

    public SnowStormPlayerObject(GamePlayer gamePlayer) {
        super(GameObjectType.SNOWSTORM_PLAYER_OBJECT);
        this.gamePlayer = gamePlayer;
        this.getGameObjectsSyncValues().add(0); // Hit points
        this.getGameObjectsSyncValues().add(0); // Snowball count
        this.getGameObjectsSyncValues().add(0); // activity timer
        this.getGameObjectsSyncValues().add(0); // activity state
        this.getGameObjectsSyncValues().add(0); // body direction
        this.getGameObjectsSyncValues().add(0); // score
    }

    @Override
    public void serialiseObject(NettyResponse response) {
        response.writeInt(gamePlayer.getPlayer().getDetails().getId());
        response.writeInt(4);//gamePlayer.getPlayer().getRoomUser().getPosition().getX());
        response.writeInt(4);//gamePlayer.getPlayer().getRoomUser().getPosition().getY());
        response.writeInt(gamePlayer.getPlayer().getRoomUser().getPosition().getRotation());
        response.writeInt(0); // hit points
        response.writeInt(0); // snowball count
        response.writeInt(0); // is bot
        response.writeInt(0); // activity timer
        response.writeInt(0); // activity state
        response.writeInt(0); // next tile x
        response.writeInt(0); // next tile y
        response.writeInt(0); // move target x
        response.writeInt(0); // move targett y
        response.writeInt(0); // score
        response.writeInt(gamePlayer.getPlayer().getDetails().getId());
        response.writeInt(gamePlayer.getTeamId());
        response.writeInt(gamePlayer.getPlayer().getDetails().getId());
        response.writeInt(gamePlayer.getPlayer().getRoomUser().getRoom().getId());
        response.writeString(gamePlayer.getPlayer().getDetails().getName());
        response.writeString(gamePlayer.getPlayer().getDetails().getMotto());
        response.writeString(gamePlayer.getPlayer().getDetails().getFigure());
        response.writeString(gamePlayer.getPlayer().getDetails().getSex());// Actually room user id/instance id
    }
}
