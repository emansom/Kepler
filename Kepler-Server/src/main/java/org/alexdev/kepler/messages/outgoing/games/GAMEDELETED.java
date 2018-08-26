package org.alexdev.kepler.messages.outgoing.games;

import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class GAMEDELETED extends MessageComposer {
    @Override
    public void compose(NettyResponse response) {
        response.writeInt(0);
    }

    @Override
    public short getHeader() {
        return 237; // "Cm"
    }
}
