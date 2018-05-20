package org.alexdev.kepler.messages.incoming.handshake;

import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.messages.outgoing.handshake.LOGIN;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

public class SSO implements MessageEvent {

    @Override
    public void handle(Player player, NettyRequest reader) {
        String ticket = reader.readString();

        player.send(new LOGIN());
    }
}