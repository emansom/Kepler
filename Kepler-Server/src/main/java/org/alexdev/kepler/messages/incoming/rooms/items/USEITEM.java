package org.alexdev.kepler.messages.incoming.rooms.items;

import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.messages.outgoing.user.FILM;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

public class USEITEM implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        player.send(new FILM(player.getDetails()));
    }
}
