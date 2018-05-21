package org.alexdev.kepler.messages.outgoing.messenger;

import org.alexdev.kepler.game.messenger.MessengerUser;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.player.PlayerDetails;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class NEW_FRIEND extends MessageComposer {
    private final PlayerDetails friend;

    public NEW_FRIEND(PlayerDetails friend) {
        this.friend = friend;
    }

    @Override
    public void compose(NettyResponse response) {
        new MessengerUser(this.friend.getId()).serialise(response);
    }

    @Override
    public short getHeader() {
        return 137; // "BI"
    }
}
