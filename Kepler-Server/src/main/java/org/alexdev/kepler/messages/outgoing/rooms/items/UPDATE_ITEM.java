package org.alexdev.kepler.messages.outgoing.rooms.items;

import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class UPDATE_ITEM extends MessageComposer {
    private final Item item;

    public UPDATE_ITEM(Item item) {
        this.item = item;
    }

    @Override
    public void compose(NettyResponse response) {
        if (this.item.getBehaviour().isWallItem()) {
            this.item.serialise(response);
        } else {
            response.writeString(this.item.getId());
            response.writeString(this.item.getCustomData());
            response.writeString("");
        }
    }

    @Override
    public short getHeader() {
        if (this.item.getBehaviour().isWallItem()) {
            return 85; // "AU"
        } else {
            return 88; // "AX"
        }
    }
}
