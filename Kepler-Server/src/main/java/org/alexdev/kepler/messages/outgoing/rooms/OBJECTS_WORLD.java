package org.alexdev.kepler.messages.outgoing.rooms;

import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.messages.headers.Outgoing;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class OBJECTS_WORLD extends MessageComposer {
    private final Room room;

    public OBJECTS_WORLD(Room room) {
        this.room = room;
    }

    @Override
    public void compose(NettyResponse response) {
        for (Item item : room.getItems()) {
            item.serialise(response);
        }
    }

    @Override
    public short getHeader() {
        return Outgoing.OBJECTS_WORLD;
    }
}
