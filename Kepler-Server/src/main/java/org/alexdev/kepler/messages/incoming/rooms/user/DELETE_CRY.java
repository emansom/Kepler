package org.alexdev.kepler.messages.incoming.rooms.user;

import org.alexdev.kepler.game.moderation.CallForHelp;
import org.alexdev.kepler.game.moderation.CallForHelpManager;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.messages.outgoing.user.CFH_ACK;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

public class DELETE_CRY implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        CallForHelp cfh = CallForHelpManager.getInstance().getOpenCallForHelpByPlayerName(
                player.getDetails().getName()
        );
        if(cfh != null) {
            CallForHelpManager.getInstance().deleteCry(cfh.getCallId());
            player.send(new CFH_ACK(player));
        }
    }
}
