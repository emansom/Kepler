package org.alexdev.kepler.game.player;

import io.netty.util.AttributeKey;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.room.RoomUser;
import org.alexdev.kepler.messages.MessageHandler;
import org.alexdev.kepler.server.netty.NettyPlayerNetwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Player extends Entity {

    public static final AttributeKey<Player> PLAYER_KEY = AttributeKey.valueOf("Player");

    private final NettyPlayerNetwork network;
    private final Logger log;

    public Player(NettyPlayerNetwork nettyPlayerNetwork) {
        this.network = nettyPlayerNetwork;
        this.log = LoggerFactory.getLogger("Connection " + this.network.getConnectionId());
    }

    @Override
    public boolean hasPermission(String permission) {
        return false;
    }

    @Override
    public PlayerDetails getDetails() {
        return null;
    }

    @Override
    public RoomUser getRoomUser() {
        return null;
    }

    @Override
    public EntityType getType() {
        return null;
    }

    public Logger getLogger() {
        return log;
    }

    public NettyPlayerNetwork getNetwork() {
        return network;
    }

    public MessageHandler getMessageHandler() {
        return new MessageHandler(this);
    }

    @Override
    public void dispose() {

    }

    public void sendMessage(String entry) {

    }
}
