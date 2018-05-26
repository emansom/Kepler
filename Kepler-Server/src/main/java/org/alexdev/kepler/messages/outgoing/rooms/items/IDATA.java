package org.alexdev.kepler.messages.outgoing.rooms.items;

import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class IDATA extends MessageComposer {
    private final int itemId;
    private final String colour;
    private final String text;

    public IDATA(int itemId, String colour, String text) {
        this.itemId = itemId;
        this.colour = colour;
        this.text = text;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeDelimeter(this.itemId, (char) 9);
        response.writeDelimeter(this.colour, ' ');
        response.write(this.text);
    }

    @Override
    public short getHeader() {
        return 48; // "@p"
    }
}

