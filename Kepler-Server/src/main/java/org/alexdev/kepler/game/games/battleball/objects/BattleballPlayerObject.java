package org.alexdev.kepler.game.games.battleball.objects;

import org.alexdev.kepler.game.games.GameObject;
import org.alexdev.kepler.game.games.enums.GameObjectType;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class BattleballPlayerObject extends GameObject {
    private final GamePlayer gamePlayer;

    public BattleballPlayerObject(GamePlayer gamePlayer) {
        super(GameObjectType.BATTLEBALL_PLAYER_OBJECT);
        this.gamePlayer = gamePlayer;
    }

    @Override
    public void serialiseObject(NettyResponse response) {
        response.writeInt(this.gamePlayer.getPlayer().getRoomUser().getInstanceId());
        response.writeInt(this.gamePlayer.getPlayer().getRoomUser().getPosition().getX());
        response.writeInt(this.gamePlayer.getPlayer().getRoomUser().getPosition().getY());
        response.writeInt((int) this.gamePlayer.getPlayer().getRoomUser().getPosition().getZ());
        response.writeInt(this.gamePlayer.getPlayer().getRoomUser().getPosition().getRotation());
        response.writeInt(0);
        response.writeInt(-1);
    }
}
