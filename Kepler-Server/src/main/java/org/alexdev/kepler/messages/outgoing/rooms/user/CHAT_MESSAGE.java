package org.alexdev.kepler.messages.outgoing.rooms.user;

import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class CHAT_MESSAGE extends MessageComposer {
    public enum type {
        CHAT (24), // @X
        SHOUT (0),
        WHISPER (0);

        private final short header;

        type(int header) {
            this.header = (short) header;
        }
    }

    private final type chatMessageType;
    private final int instanceId;
    private final String message;

    public CHAT_MESSAGE(type chatMessageType, int instanceId, String message) {
        this.chatMessageType = chatMessageType;
        this.instanceId = instanceId;
        this.message = message;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(this.instanceId);
        response.writeString(this.message);
    }

    @Override
    public short getHeader() {
        return this.chatMessageType.header;
    }
}
