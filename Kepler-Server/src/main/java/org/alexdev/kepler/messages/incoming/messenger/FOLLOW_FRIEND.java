package org.alexdev.kepler.messages.incoming.messenger;

import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.player.PlayerManager;
import org.alexdev.kepler.messages.outgoing.messenger.FOLLOW_ERROR;
import org.alexdev.kepler.messages.outgoing.messenger.FOLLOW_REQUEST;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

public class FOLLOW_FRIEND implements MessageEvent {
    private enum FollowErrors {
        NOT_FRIEND(0),
        OFFLINE(1),
        ON_HOTELVIEW(2),
        NO_CREEPING_ALLOWED(3);

        private int id;

        FollowErrors(int id) {
            this.id = id;
        }

        public int getID(){
            return id;
        }
    }

    @Override
    public void handle(Player player, NettyRequest reader) {
        int friendId = reader.readInt();

        if (!player.getMessenger().isFriend(friendId)) {
            player.send(new FOLLOW_ERROR(FollowErrors.NOT_FRIEND.getID())); // Not their friend
            return;
        }

        Player friend = PlayerManager.getInstance().getPlayerById(friendId);

        if (friend == null) {
            player.send(new FOLLOW_ERROR(FollowErrors.OFFLINE.getID())); // Friend is not online
            return;
        }

        if (friend.getRoom() == null) {
            player.send(new FOLLOW_ERROR(FollowErrors.ON_HOTELVIEW.getID())); // Friend is on hotelview
            return;
        }

        // TODO: FUSE permission instead of rank check
        if (!friend.getDetails().doesAllowStalking() && player.getDetails().getRank() < 5) {
            player.send(new FOLLOW_ERROR(FollowErrors.NO_CREEPING_ALLOWED.getID())); // Friend does not allow stalking
            return;
        }

        boolean isPublic = friend.getRoom().isPublicRoom();
        int roomId = friend.getRoom().getId();

        player.send(new FOLLOW_REQUEST(isPublic, roomId));
    }
}
