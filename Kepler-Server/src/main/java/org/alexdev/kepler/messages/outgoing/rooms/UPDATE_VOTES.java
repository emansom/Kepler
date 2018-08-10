package org.alexdev.kepler.messages.outgoing.rooms;

import org.alexdev.kepler.dao.mysql.RoomDao;
import org.alexdev.kepler.game.player.PlayerDetails;
import org.alexdev.kepler.game.room.RoomData;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;
import org.alexdev.kepler.util.encoding.Base64Encoding;

public class UPDATE_VOTES extends MessageComposer {
    RoomData room;
    PlayerDetails user;

    public UPDATE_VOTES(RoomData room, PlayerDetails user){
        this.room = room;
        this.user = user;
    }

    @Override
    public void compose(NettyResponse response) {
        if(RoomDao.hasVoted(user.getId(), room.getId())) {
            if(this.room.getRating() > 0)
                response.writeInt(this.room.getRating());
            else
                response.writeInt(0);
        }else{
            response.writeInt(-1);
        }
    }

    @Override
    public short getHeader() {
        return 345;
    }
}
