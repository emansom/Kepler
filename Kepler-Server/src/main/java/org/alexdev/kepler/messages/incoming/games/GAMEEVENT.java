package org.alexdev.kepler.messages.incoming.games;

import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

public class GAMEEVENT implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        if (!player.getRoomUser().isWalkingAllowed()) {
            return;
        }

        reader.readInt(); // Instance ID? Useless?

        int X = reader.readInt();
        int Y = reader.readInt();

        player.getRoomUser().walkTo(X, Y);
    }
}
