package org.alexdev.kepler.messages.outgoing.messenger;

import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class REMOVE_FRIEND extends MessageComposer {
    private final int userId;

    public REMOVE_FRIEND(int userId) {
        this.userId = userId;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(1);
        response.writeInt(this.userId);
    }

    @Override
    public short getHeader() {
        return 138; // "BJ"
    }
}
