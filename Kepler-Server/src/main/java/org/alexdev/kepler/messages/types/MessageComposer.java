package org.alexdev.kepler.messages.types;

import io.netty.buffer.ByteBuf;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public abstract class MessageComposer {

    /**
     * Write the message to send back to the client.
     */
    public abstract void compose(NettyResponse response);

    /**
     * Write to buffer
     */
    public NettyResponse writeToBuffer(ByteBuf buf) {
        return null;
    }

    /**
     * Get the header
     */
    public abstract short getHeader();
}
