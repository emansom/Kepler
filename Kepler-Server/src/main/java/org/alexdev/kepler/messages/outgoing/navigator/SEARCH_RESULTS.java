package org.alexdev.kepler.messages.outgoing.navigator;

import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

import java.util.List;

public class SEARCH_RESULTS extends MessageComposer {
    private final List<Room> roomList;
    private final Player player;

    public SEARCH_RESULTS(List<Room> roomList, Player player) {
        this.roomList = roomList;
        this.player = player;
    }

    @Override
    public void compose(NettyResponse response) {
        for (Room room : this.roomList) {
            response.writeDelimeter(room.getId(), (char) 9);
            response.writeDelimeter(room.getData().getName(), (char) 9);

            if (room.isOwner(this.player.getEntityId()) || room.getData().showName() || this.player.hasFuse("fuse_see_all_roomowners")) {
                response.writeDelimeter(room.getData().getOwnerName(), (char) 9);
            } else {
                response.writeDelimeter("-", (char) 9);
            }

            response.writeDelimeter(room.getData().getAccessType(), (char) 9);
            response.writeDelimeter("x", (char) 9);
            response.writeDelimeter(room.getData().getVisitorsNow(), (char) 9);
            response.writeDelimeter(room.getData().getVisitorsMax(), (char) 9);
            response.writeDelimeter("null", (char) 9);
            response.writeDelimeter(room.getData().getDescription(), (char) 9);
            response.writeDelimeter(room.getData().getDescription(), (char) 9);
            response.write(Character.toString((char) 13));
        }
    }

    @Override
    public short getHeader() {
        return 55; // "@w"
    }
}
