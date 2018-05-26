package org.alexdev.kepler.messages.incoming.rooms;

import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.messages.outgoing.rooms.ACTIVE_OBJECTS;
import org.alexdev.kepler.messages.outgoing.rooms.OBJECTS_WORLD;
import org.alexdev.kepler.messages.outgoing.rooms.items.SHOWPROGRAM;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

public class G_OBJS implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        if (player.getRoomUser().getRoom() == null) {
            return;
        }

        Room room = player.getRoomUser().getRoom();

        player.sendQueued(new OBJECTS_WORLD(room));
        player.sendQueued(new ACTIVE_OBJECTS(room));

        for (Item item : room.getItems()) {
            if (item.getCurrentProgramValue().length() > 0) {
                player.sendQueued(new SHOWPROGRAM(item.getCurrentProgram(), item.getCurrentProgramValue()));
            }
        }

        player.flushSendQueue();
    }
}
