package org.alexdev.kepler.messages.outgoing.handshake;

import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class INIT_CRYPTO_RESPONSE extends MessageComposer {

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(7);
        response.writeInt(0);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(3);
        response.writeInt(0);
        response.writeInt(2);
        response.writeInt(1);
        response.writeInt(4);
        response.writeInt(1);
        response.writeInt(5);
        response.writeString("dd-MM-yyyy");

        response.writeInt(9);
        response.writeInt(1);
    }

    @Override
    public short getHeader() {
        return 277;
    }
}
