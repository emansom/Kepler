package org.alexdev.kepler.messages.incoming.rooms.moderation;

import org.alexdev.kepler.game.moderation.CallForHelpManager;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

public class CHANGECALLCATEGORY implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        int callId = Integer.parseInt(reader.readString());
        int category = reader.readInt();
        CallForHelpManager.getInstance().changeCategory(callId, category);

        System.out.println(callId+" "+category);
    }
}
