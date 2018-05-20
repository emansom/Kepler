package org.alexdev.kepler.messages.outgoing.rooms;

import org.alexdev.kepler.messages.headers.Outgoing;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class ACTIVE_OBJECTS extends MessageComposer {
    @Override
    public void compose(NettyResponse response) {
        response.writeInt(0);
    }

    @Override
    public short getHeader() {
        return Outgoing.ACTIVE_OBJECTS;
    }
}
