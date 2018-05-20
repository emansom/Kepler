package org.alexdev.kepler.messages.outgoing.handshake;

import org.alexdev.kepler.messages.headers.Outgoing;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

import java.util.List;

public class FUSERIGHTS extends MessageComposer {
    private final List<String> avaliableFuserights;

    public FUSERIGHTS(List<String> avaliableFuserights) {
        this.avaliableFuserights = avaliableFuserights;
    }

    @Override
    public void compose(NettyResponse response) {
        for (String fuseright : this.avaliableFuserights) {
            response.writeString(fuseright);
        }
    }

    @Override
    public short getHeader() {
        return Outgoing.FUSERIGHTS;
    }
}
