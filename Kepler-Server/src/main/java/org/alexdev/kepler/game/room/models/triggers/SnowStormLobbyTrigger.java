package org.alexdev.kepler.game.room.models.triggers;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.triggers.GenericTrigger;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class SnowStormLobbyTrigger extends GenericTrigger {
    @Override
    public void onRoomEntry(Entity entity, Room room, Object... customArgs) {

    }

    @Override
    public void onRoomLeave(Entity entity, Room room, Object... customArgs)  {

    }
}
