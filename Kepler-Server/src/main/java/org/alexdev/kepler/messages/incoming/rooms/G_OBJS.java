package org.alexdev.kepler.messages.incoming.rooms;

import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.messages.outgoing.rooms.ACTIVE_OBJECTS;
import org.alexdev.kepler.messages.outgoing.rooms.OBJECTS_WORLD;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

public class G_OBJS implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        player.send(new OBJECTS_WORLD());
        player.send(new ACTIVE_OBJECTS());
    }
}
