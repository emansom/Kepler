package org.alexdev.kepler.messages.outgoing.messenger;

import org.alexdev.kepler.game.player.PlayerDetails;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class FRIEND_REQUEST extends MessageComposer {
    private final PlayerDetails details;

    public FRIEND_REQUEST(PlayerDetails details) {
        this.details = details;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(this.details.getId());
        response.writeString(this.details.getName());
    }

    @Override
    public short getHeader() {
        return 132; // "BD"
    }
}
