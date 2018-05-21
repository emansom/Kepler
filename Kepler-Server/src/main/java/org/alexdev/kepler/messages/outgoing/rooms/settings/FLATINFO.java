package org.alexdev.kepler.messages.outgoing.rooms.settings;

import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class FLATINFO extends MessageComposer {
    private Player player;
    private Room room;

    public FLATINFO(Player player, Room room) {
        this.player = player;
        this.room = room;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeBool(this.room.getData().allowSuperUsers());
        response.writeInt(this.room.getData().getAccessTypeId());
        response.writeInt(this.room.getData().getId());

        if (this.room.getData().getOwnerId() == player.getDetails().getId() || this.room.getData().showName() ||
                this.player.hasPermission("fuse_see_all_roomowners")) {
            response.writeString(this.room.getData().getOwnerName());
        } else {
            response.writeString("-");
        }

        response.writeString(this.room.getData().getModelName());
        response.writeString(this.room.getData().getName());
        response.writeString(this.room.getData().getDescription());
        response.writeBool(this.room.getData().showName());
        response.writeBool(true); // Allow trading
        response.writeInt(this.room.getData().getVisitorsNow());
        response.writeInt(this.room.getData().getVisitorsMax());
    }

    @Override
    public short getHeader() {
        return 54; // "@v"
    }
}

