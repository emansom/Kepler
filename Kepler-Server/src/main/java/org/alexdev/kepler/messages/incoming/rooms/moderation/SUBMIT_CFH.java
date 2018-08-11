package org.alexdev.kepler.messages.incoming.rooms.moderation;

import org.alexdev.kepler.game.moderation.CallForHelpManager;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;
import org.alexdev.kepler.util.encoding.FuseMessage;

public class SUBMIT_CFH implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        // TODO: check if inside room
        String message = reader.readString();

        if (message.length() == 0) {
            return;
        }

        // TODO: ignore messages that only contains spaces

        // Only allow one call for help per user
        if (CallForHelpManager.getInstance().hasPendingCall(player)) {
            return;
        }

        CallForHelpManager.getInstance().submitCall(player, message);
    }
}
