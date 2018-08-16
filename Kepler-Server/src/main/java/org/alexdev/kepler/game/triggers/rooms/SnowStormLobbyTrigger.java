package org.alexdev.kepler.game.triggers.rooms;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.entities.RoomEntity;
import org.alexdev.kepler.game.triggers.GenericTrigger;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class SnowStormLobbyTrigger extends GenericTrigger {
    @Override
    public void onRoomEntry(Entity entity, RoomEntity roomEntity, Object... customArgs) {
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player) entity;

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
    public void onRoomLeave(Entity entity, RoomEntity roomEntity, Object... customArgs) {

    }
}
