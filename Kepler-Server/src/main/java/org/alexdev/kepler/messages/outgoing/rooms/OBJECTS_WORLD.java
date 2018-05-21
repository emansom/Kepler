package org.alexdev.kepler.messages.outgoing.rooms;

import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.messages.headers.Outgoing;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

import java.util.List;

public class OBJECTS_WORLD extends MessageComposer {
    private final List<Item> items;

    public OBJECTS_WORLD(Room room) {
        this.items = room.getItemManager().getPublicItems();
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(this.items.size());

        for (Item item : this.items) {
            item.serialise(response);
        }
    }

    @Override
    public short getHeader() {
        return Outgoing.OBJECTS_WORLD;
    }
}
