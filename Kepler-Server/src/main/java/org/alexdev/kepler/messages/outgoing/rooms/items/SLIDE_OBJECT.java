package org.alexdev.kepler.messages.outgoing.rooms.items;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;
import org.alexdev.kepler.util.StringUtil;

public class SLIDE_OBJECT extends MessageComposer {
    private Item item;
    private Position from;
    private Position to;
    private int rollerId;
    private double nextHeight;
    private Entity entity;

    public SLIDE_OBJECT(Item item, Position next, int rollerId, double nextHeight) {
        this.item = item;
        this.from = item.getPosition().copy();
        this.to = next.copy();
        this.rollerId = rollerId;
        this.nextHeight = nextHeight;
    }

    public SLIDE_OBJECT(Entity entity, Position next, int rollerId, double nextHeight) {
        this.entity = entity;
        this.from = entity.getRoomUser().getPosition().copy();
        this.to = next.copy();
        this.rollerId = rollerId;
        this.nextHeight = nextHeight;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(this.from.getX());
        response.writeInt(this.from.getY());
        response.writeInt(this.to.getX());
        response.writeInt(this.to.getY());

        if (this.item != null) {
            response.writeInt(1);
            response.writeInt(this.item.getId());
        } else {
            response.writeInt(0);
            response.writeInt(this.rollerId);
            response.writeInt(2);
            response.writeInt(this.entity.getRoomUser().getInstanceId());
        }

        response.writeString(StringUtil.format(this.from.getZ()));

        if (this.entity.getRoomUser().isSittingOnGround()) {
            response.writeString(StringUtil.format(this.nextHeight) - 0.5); // Take away sit offset because yeah, weird stuff.
        } else {
            response.writeString(StringUtil.format(this.nextHeight));
        }

        if (this.item != null) {
            response.writeInt(this.rollerId);
        }
    }

    @Override
    public short getHeader() {
        return 230; // "Cf"
    }
}
