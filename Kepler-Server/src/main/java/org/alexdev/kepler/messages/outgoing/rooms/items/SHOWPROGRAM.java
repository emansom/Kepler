package org.alexdev.kepler.messages.outgoing.rooms.items;

import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;
import org.alexdev.kepler.util.StringUtil;

public class SHOWPROGRAM extends MessageComposer {
    private final String currentProgramValue;
    private final String currentProgram;

    public SHOWPROGRAM(String currentProgram, String currentProgramValue) {
        this.currentProgram = currentProgram;
        this.currentProgramValue = currentProgramValue;
    }

    @Override
    public void compose(NettyResponse response) {
        response.write(this.currentProgram);

        if (!StringUtil.isNullOrEmpty(this.currentProgramValue)) {
            response.write(" ");
            response.write(this.currentProgramValue);
        }
    }

    @Override
    public short getHeader() {
        return 71; // "AG"
    }
}
