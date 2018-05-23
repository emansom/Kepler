package org.alexdev.kepler.messages.outgoing.rooms.pool;

import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class OPEN_UIMAKOPPI extends MessageComposer {
    private final String poolFigure;

    public OPEN_UIMAKOPPI(String poolFigure) {
        this.poolFigure = poolFigure;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeString(this.poolFigure);
    }

    @Override
    public short getHeader() {
        return 96; // "A`"
    }
}
