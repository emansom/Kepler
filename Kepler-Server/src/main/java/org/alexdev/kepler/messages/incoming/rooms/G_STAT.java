package org.alexdev.kepler.messages.incoming.rooms;

import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.messages.outgoing.rooms.ACTIVE_OBJECTS;
import org.alexdev.kepler.messages.outgoing.rooms.OBJECTS_WORLD;
import org.alexdev.kepler.messages.outgoing.rooms.user.USER_STATUSES;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

public class G_STAT implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        player.send(new USER_STATUSES(player.getRoomUser().getRoom().getEntities()));
    }
}
