package org.alexdev.kepler.messages.incoming.games;

import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.messages.outgoing.games.GAMEPARAMETERS;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

public class INITIATECREATEGAME implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        player.send(new GAMEPARAMETERS());
    }
}
