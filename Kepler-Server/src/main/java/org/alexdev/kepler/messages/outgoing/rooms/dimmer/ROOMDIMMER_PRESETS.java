package org.alexdev.kepler.messages.outgoing.rooms.dimmer;

import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class ROOMDIMMER_PRESETS extends MessageComposer {
    private final int itemId;

    public ROOMDIMMER_PRESETS(int itemId) {
        this.itemId = itemId;
    }

    @Override
    public void compose(NettyResponse response) {
        response.write(this.itemId);
        // TODO: more stuff
    }

    @Override
    public short getHeader() {
        return 365; // "Em"
    }
}
