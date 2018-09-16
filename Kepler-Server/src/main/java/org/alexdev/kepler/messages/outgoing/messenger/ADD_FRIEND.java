package org.alexdev.kepler.messages.outgoing.messenger;

import org.alexdev.kepler.game.messenger.MessengerUser;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.player.PlayerDetails;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class NEW_FRIEND extends MessageComposer {
    private final MessengerUser messengerUser;

    public NEW_FRIEND(MessengerUser messengerUser) {
        this.messengerUser = messengerUser;
    }

    @Override
    public void compose(NettyResponse response) {
        this.messengerUser.serialise(response);
    }

    @Override
    public short getHeader() {
        return 137; // "BI"
    }
}
