package org.alexdev.kepler.messages.outgoing.rooms;

import org.alexdev.kepler.game.room.models.RoomModel;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class HEIGHTMAP_UPDATE extends MessageComposer {
    private final String heightmap;

    public HEIGHTMAP_UPDATE(String heightmap) {
        this.heightmap = heightmap;
    }

    public HEIGHTMAP_UPDATE(RoomModel roomModel) {
        this.heightmap = roomModel.getHeightmap();
    }

    @Override
    public void compose(NettyResponse response) {
        response.write(this.heightmap);
    }

    @Override
    public short getHeader() {
        return 219; // "@_"
    }
}
