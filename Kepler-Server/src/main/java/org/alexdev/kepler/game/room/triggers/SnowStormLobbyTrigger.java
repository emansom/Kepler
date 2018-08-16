package org.alexdev.kepler.game.room.triggers;

import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.triggers.GenericTrigger;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class SnowStormLobbyTrigger extends GenericTrigger {
    @Override
    public void onRoomEntry(Player player, Room room, Object... customArgs) {
        player.send(new MessageComposer() {
            @Override
            public void compose(NettyResponse response) {
                response.writeInt(0);
            }

            @Override
            public short getHeader() {
                return 231;
            }
        });

        player.send(new MessageComposer() {
            @Override
            public void compose(NettyResponse response) {
                response.writeInt(0);
                response.writeInt(0);
                response.writeInt(0);
            }

            @Override
            public short getHeader() {
                return 232;
            }
        });
    }

    @Override
    public void onRoomLeave(Player player, Room room, Object... customArgs)  {

    }
}
