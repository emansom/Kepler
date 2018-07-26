package org.alexdev.kepler.messages.outgoing.user;

import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;
import org.alexdev.kepler.util.encoding.Base64Encoding;
import org.alexdev.kepler.util.encoding.VL64Encoding;

public class CFH_ACK extends MessageComposer {
    @Override
    public void compose(NettyResponse response) {
        // TODO: Stop multiple requests once the user has a non-picked up CFH
        response.writeString("H");
    }

    @Override
    public short getHeader() {
        return 319; // "D"
    }
}
