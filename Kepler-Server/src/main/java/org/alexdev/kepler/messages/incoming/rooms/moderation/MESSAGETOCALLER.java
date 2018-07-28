package org.alexdev.kepler.messages.incoming.rooms.moderation;

import org.alexdev.kepler.game.moderation.CallForHelpManager;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.player.PlayerManager;
import org.alexdev.kepler.messages.outgoing.moderation.CRY_REPLY;
import org.alexdev.kepler.messages.outgoing.user.MODERATOR_ALERT;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

public class MESSAGETOCALLER implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        // BUG: Calling reader.readInt() and then reader.readString() acts as if the int hasn't been read, thus
        //      returns the callId. To counteract this read both a strings but parse callId to Integer.
        int callId = Integer.parseInt(reader.readString());
        String message = reader.readString();
        String callerName = CallForHelpManager.getInstance().getCallForHelpById(callId).getCaller();
        Player caller = PlayerManager.getInstance().getPlayerByName(callerName);
        if(caller != null){
            caller.send(new CRY_REPLY(message));
        }
    }
}
