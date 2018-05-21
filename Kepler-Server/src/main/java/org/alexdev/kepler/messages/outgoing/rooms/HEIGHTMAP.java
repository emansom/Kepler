package org.alexdev.kepler.messages.outgoing.rooms;

import org.alexdev.kepler.game.room.models.RoomModel;
import org.alexdev.kepler.messages.headers.Outgoing;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class HEIGHTMAP extends MessageComposer {
    private final RoomModel roomModel;

    public HEIGHTMAP(RoomModel roomModel) {
        this.roomModel = roomModel;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeString(this.roomModel.getHeightmap());
    }

    @Override
    public short getHeader() {
        return Outgoing.HEIGHTMAP;
    }
}
