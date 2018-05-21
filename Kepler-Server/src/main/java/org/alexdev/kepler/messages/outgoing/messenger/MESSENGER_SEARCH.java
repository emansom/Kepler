package org.alexdev.kepler.messages.outgoing.messenger;

import org.alexdev.kepler.game.messenger.MessengerUser;
import org.alexdev.kepler.game.player.PlayerDetails;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class MESSENGER_SEARCH extends MessageComposer {
    private final int userId;

    public MESSENGER_SEARCH(int userId) {
        this.userId = userId;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeString("MESSENGER");

        if (this.userId > 0) {
            new MessengerUser(this.userId).serialise(response);
        } else {
            response.writeInt(0);
        }
    }

    @Override
    public short getHeader() {
        return 128; // "B@"
    }
}
