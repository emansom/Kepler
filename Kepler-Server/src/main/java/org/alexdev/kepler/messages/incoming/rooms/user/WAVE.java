package org.alexdev.kepler.messages.incoming.rooms.user;

import org.alexdev.kepler.game.GameScheduler;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.enums.StatusType;
import org.alexdev.kepler.messages.outgoing.rooms.user.USER_STATUSES;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class WAVE implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        player.getRoomUser().wave();
    }

}
