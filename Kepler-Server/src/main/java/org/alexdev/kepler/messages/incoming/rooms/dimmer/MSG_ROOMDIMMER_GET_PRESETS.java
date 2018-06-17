package org.alexdev.kepler.messages.incoming.rooms.dimmer;

import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

public class MSG_ROOMDIMMER_GET_PRESETS implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
    }
}