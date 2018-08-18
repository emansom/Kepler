package org.alexdev.kepler.messages.outgoing.rooms;

import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class UPDATE_VOTES extends MessageComposer {
    private final boolean hasVoted;
    private final int rating;

    public UPDATE_VOTES(boolean hasVoted, int rating) {
        this.hasVoted = hasVoted;
        this.rating = rating;
    }

    @Override
    public void compose(NettyResponse response) {
        if (hasVoted) {
            response.writeInt(this.rating);
        } else {
            response.writeInt(-1);
        }
    }

    @Override
    public short getHeader() {
        return 345;
    }
}
