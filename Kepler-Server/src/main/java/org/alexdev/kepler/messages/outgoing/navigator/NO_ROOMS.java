package org.alexdev.kepler.messages.outgoing.navigator;

import org.alexdev.kepler.messages.headers.Outgoing;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class NO_ROOMS extends MessageComposer {
    private String username;

    public NO_ROOMS(String username) {
        this.username = username;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeString(this.username);
    }

    @Override
    public short getHeader() {
        return Outgoing.NO_ROOMS;
    }
}
