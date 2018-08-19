package org.alexdev.kepler.messages.outgoing.rooms.items;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;
import org.alexdev.kepler.util.StringUtil;

import java.util.List;

public class SLIDE_OBJECT extends MessageComposer {
    private final Item roller;
    private final List<Item> rollingItems;
    private final Entity rollingEntity;

    public SLIDE_OBJECT(Item roller, List<Item> rollingItems, Entity rollingEntity) {
        this.roller = roller;
        this.rollingItems = rollingItems;
        this.rollingEntity = rollingEntity;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(this.roller.getPosition().getX());
        response.writeInt(this.roller.getPosition().getY());
        response.writeInt(this.roller.getPosition().getSquareInFront().getX());
        response.writeInt(this.roller.getPosition().getSquareInFront().getY());
        response.writeInt(this.rollingItems.size());

        for (Item item : this.rollingItems) {
            response.writeInt(item.getId());
            response.writeString(StringUtil.format(item.getRollingData().getFromPosition().getZ()));
            response.writeString(StringUtil.format(item.getRollingData().getNextPosition().getZ()));
        }

        response.writeInt(this.roller.getId());
        response.writeInt(this.rollingEntity != null ? 2 : 0);

        if (this.rollingEntity != null) {
            response.writeInt(this.rollingEntity.getRoomUser().getInstanceId());
            response.writeString(StringUtil.format(this.rollingEntity.getRoomUser().getRollingData().getFromPosition().getZ()));
            response.writeString(StringUtil.format(this.rollingEntity.getRoomUser().getRollingData().getDisplayHeight()));
        }
    }

    @Override
    public short getHeader() {
        return 230; // "Cf"
    }
}
