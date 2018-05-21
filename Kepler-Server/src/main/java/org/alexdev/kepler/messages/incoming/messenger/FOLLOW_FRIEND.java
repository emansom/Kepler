package org.alexdev.kepler.messages.incoming.messenger;

import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.player.PlayerManager;
import org.alexdev.kepler.messages.outgoing.messenger.FOLLOW_ERROR;
import org.alexdev.kepler.messages.outgoing.messenger.FOLLOW_REQUEST;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

public class FOLLOW_FRIEND implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        int friendId = reader.readInt();

        int errorId = -1;

        if (player.getMessenger().isFriend(friendId)) {
            Player friend = PlayerManager.getInstance().getPlayerById(friendId);

            if (friend != null) {
                if (friend.getRoomUser().getRoom() != null) {
                    boolean isPublic = friend.getRoomUser().getRoom().isPublicRoom();
                    int roomId = friend.getRoomUser().getRoom().getData().getId();

                    player.send(new FOLLOW_REQUEST(isPublic, roomId));
                } else {
                    errorId = 2;
                }
            } else {
                errorId = 1; // Friend is not offline
            }
        } else {
            errorId = 0; // Not their friend
        }

        if (errorId != -1) {
            player.send(new FOLLOW_ERROR(errorId));
        }
    }
}
