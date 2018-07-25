package org.alexdev.kepler.messages.incoming.moderation;

import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.player.PlayerManager;
import org.alexdev.kepler.messages.outgoing.user.ALERT;
import org.alexdev.kepler.messages.outgoing.user.HOTEL_VIEW;
import org.alexdev.kepler.messages.outgoing.user.MODERATOR_ALERT;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;
import org.alexdev.kepler.util.encoding.FuseMessage;
import org.alexdev.kepler.util.encoding.VL64Encoding;

public class MODERATORACTION implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        String mBody = reader.contents();
        int commandCat = VL64Encoding.decode(mBody.substring(0,1).getBytes());
        int commandId = VL64Encoding.decode(mBody.substring(1,2).getBytes());
        String payload = mBody.substring(2);

        if(commandCat == 0){
            // User Command
            if(commandId == 0){
                // Alert
                String alertMessage = FuseMessage.getArgument(1, payload);
                String alertExtra = FuseMessage.getArgument(2, payload);
                String alertUser = FuseMessage.getArgument(3, payload);

                Player target = PlayerManager.getInstance().getPlayerByName(alertUser);
                if(target != null)
                    target.send(new MODERATOR_ALERT(alertMessage));
                else
                    player.send(new ALERT("Target user is not online."));
            }else if(commandId == 1){
                // Kick
                String alertMessage = FuseMessage.getArgument(1, payload);
                String alertExtra = FuseMessage.getArgument(2, payload);
                String alertUser = FuseMessage.getArgument(3, payload);

                Player target = PlayerManager.getInstance().getPlayerByName(alertUser);
                if(target != null){
                    target.send(new HOTEL_VIEW());
                    target.send(new MODERATOR_ALERT(alertMessage));
                }else
                    player.send(new ALERT("Target user is not online."));
            }else if(commandId == 2){
                //Ban
            }
        }else if(commandCat == 1){
            // Room Command
            if(commandId == 0){
                // Room Alert
            }else if(commandId == 1){
                // Room Kick
            }
        }
    }
}
