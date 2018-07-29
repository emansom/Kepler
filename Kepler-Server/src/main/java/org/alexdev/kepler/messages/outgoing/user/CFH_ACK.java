package org.alexdev.kepler.messages.outgoing.user;

import org.alexdev.kepler.game.moderation.CallForHelp;
import org.alexdev.kepler.game.moderation.CallForHelpManager;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;
import org.alexdev.kepler.util.encoding.Base64Encoding;
import org.alexdev.kepler.util.encoding.VL64Encoding;

public class CFH_ACK extends MessageComposer {
    private Player player;
    public CFH_ACK(Player p){
        this.player = p;
    }

    @Override
    public void compose(NettyResponse response) {
        CallForHelp call = CallForHelpManager.getInstance().getOpenCallForHelpByPlayerName(player.getDetails().getName());
        if(call != null){
            response.writeString("I" + call.getCallId());
            response.writeString(call.getFormattedRequestTime());
            response.writeString(call.getMessage());
        }else{
            response.writeString("H");
        }
    }

    @Override
    public short getHeader() {
        return 319; // "D"
    }
}
