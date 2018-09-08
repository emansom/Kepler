package org.alexdev.kepler.game.games.battleball.objects;

import org.alexdev.kepler.game.games.GameObject;
import org.alexdev.kepler.game.games.enums.GameObjectType;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class PinObject extends GameObject {
    private final int id;
    private final Position position;

    public PinObject(int id, Position position) {
        super(GameObjectType.BATTLEBALL_PIN_OBJECT);
        this.id = id;
        this.position = position;
    }

    @Override
    public void serialiseObject(NettyResponse response) {
        response.writeInt(this.id);
        response.writeInt(this.position.getX());
        response.writeInt(this.position.getY());
        response.writeInt((int) this.position.getZ());
    }

    public int getId() {
        return id;
    }

    public Position getPosition() {
        return position;
    }
}
