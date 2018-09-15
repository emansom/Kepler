package org.alexdev.kepler.messages.incoming.messenger;

import org.alexdev.kepler.dao.mysql.MessengerDao;
import org.alexdev.kepler.game.messenger.MessengerUser;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.player.PlayerManager;
import org.alexdev.kepler.messages.outgoing.messenger.NEW_FRIEND;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

import java.sql.SQLException;

public class MESSENGER_ACCEPTBUDDY implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws SQLException {
        int amount = reader.readInt();

        for (int i = 0; i < amount; i++) {
            int userId = reader.readInt();

            if (!player.getMessenger().hasRequest(userId)) {
                continue;
            }


            MessengerDao.newFriend(userId, player.getDetails().getId());
            MessengerDao.removeRequest(userId, player.getDetails().getId());
            MessengerDao.removeRequest(player.getDetails().getId(), userId);

            MessengerUser messengerFriend = new MessengerUser(PlayerManager.getInstance().getPlayerData(userId));

            player.getMessenger().removeRequest(userId);
            player.getMessenger().getFriends().add(messengerFriend);
            player.send(new NEW_FRIEND(messengerFriend));

            Player friend = PlayerManager.getInstance().getPlayerById(userId);

            if (friend != null) {
                messengerFriend = new MessengerUser(player.getDetails());

                friend.getMessenger().removeRequest(player.getDetails().getId());
                friend.getMessenger().getFriends().add(messengerFriend);
            }
        }
    }
}
