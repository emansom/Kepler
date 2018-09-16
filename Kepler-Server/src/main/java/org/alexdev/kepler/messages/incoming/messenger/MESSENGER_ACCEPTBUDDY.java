package org.alexdev.kepler.messages.incoming.messenger;

import org.alexdev.kepler.dao.mysql.MessengerDao;
import org.alexdev.kepler.game.messenger.*;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.player.PlayerManager;
import org.alexdev.kepler.messages.outgoing.messenger.ADD_FRIEND;
import org.alexdev.kepler.messages.outgoing.messenger.BUDDY_REQUEST_RESULT;
import org.alexdev.kepler.messages.outgoing.messenger.MESSENGER_ERROR;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MESSENGER_ACCEPTBUDDY implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        List<MessengerError> errors = new ArrayList<>();

        int amount = reader.readInt();

        for (int i = 0; i < amount; i++) {
            int userId = reader.readInt();

            MessengerUser newBuddy = player.getMessenger().getRequest(userId);

            if (newBuddy == null) {
                MessengerError error = new MessengerError(MessengerErrorType.FRIEND_REQUEST_NOT_FOUND);
                error.setCauser(newBuddy);

                errors.add(error);
                continue;
            }

            Messenger newBuddyData = PlayerManager.getInstance().getMessengerData(userId);

            if (newBuddyData == null) {
                // log warning
                continue;
            }

            if (player.getMessenger().isFriendsLimitReached()) {
                MessengerError error = new MessengerError(MessengerErrorType.FRIENDLIST_FULL);
                error.setCauser(newBuddy);

                errors.add(error);
                continue;
            }

            if (newBuddyData.isFriendsLimitReached()) {
                MessengerError error = new MessengerError(MessengerErrorType.TARGET_FRIEND_LIST_FULL);
                error.setCauser(newBuddy);

                errors.add(error);
                continue;
            }

            if (!newBuddyData.isAllowsFriendRequests()) {
                MessengerError error = new MessengerError(MessengerErrorType.TARGET_DOES_NOT_ACCEPT);
                error.setCauser(newBuddy);

                errors.add(error);
                continue;
            }

            player.getMessenger().addFriend(newBuddy);
            player.send(new ADD_FRIEND(newBuddy));

            Player friend = PlayerManager.getInstance().getPlayerById(userId);

            if (friend != null) {
                MessengerUser meAsBuddy = player.getMessenger().getMessengerUser();

                friend.getMessenger().addFriend(meAsBuddy);
                friend.send(new ADD_FRIEND(meAsBuddy));
            }
        }

        player.send(new BUDDY_REQUEST_RESULT(errors));
    }
}
