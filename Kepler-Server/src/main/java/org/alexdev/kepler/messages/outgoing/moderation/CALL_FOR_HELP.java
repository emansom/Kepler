package org.alexdev.kepler.messages.outgoing.moderation;

import org.alexdev.kepler.game.moderation.CallForHelp;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class CALL_FOR_HELP extends MessageComposer {
    private CallForHelp cfh;

    public CALL_FOR_HELP(CallForHelp cfh){
        this.cfh = cfh;
    }

    @Override
    public void compose(NettyResponse response) {
        StringBuilder builder = new StringBuilder();
        builder.append(cfh.getCallId());
        builder.append((char)2);
        builder.append(cfh.getPriority());
        // TODO: Figure out what separates the lines here.
        builder.append("     Picked up by: ");
        builder.append(cfh.getPickedUpBy());
        builder.append((char)2);
        builder.append(cfh.getCaller());
        builder.append((char)2);
        builder.append(cfh.getMessage());
        builder.append((char)2);
        builder.append("M");
        builder.append((char)2);
        builder.append("Room: ");
        builder.append(cfh.getRoomName());
        builder.append((char)2);
        builder.append("I");
        builder.append((char)2);
        builder.append(cfh.getRoomId());
        builder.append((char)2);

        response.writeString(builder.toString());
    }

    @Override
    public short getHeader() {
        return 148; // BT
    }
}
