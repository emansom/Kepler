package org.alexdev.kepler.messages.incoming.rooms;

import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.messages.outgoing.rooms.user.USER_OBJECTS;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

public class G_USRS implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        player.send(new USER_OBJECTS(player.getRoomUser().getRoom().getEntities()));
    }
}
