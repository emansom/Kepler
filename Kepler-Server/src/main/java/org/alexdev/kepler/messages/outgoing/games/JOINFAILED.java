package org.alexdev.kepler.messages.outgoing.games;

import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class JOINFAILED extends MessageComposer {
    public enum FailedReason {
        TICKETS_NEEDED(2);

        private final int reasonId;

        FailedReason(int reasonId) {
            this.reasonId = reasonId;
        }

        public int getReasonId() {
            return reasonId;
        }
    }

    private final FailedReason reason;

    public JOINFAILED(FailedReason reason) {
        this.reason = reason;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(this.reason.getReasonId());
    }

    @Override
    public short getHeader() {
        return 239;
    }
}
