package org.alexdev.kepler.messages.incoming.messenger;

import org.alexdev.kepler.dao.mysql.MessengerDao;
import org.alexdev.kepler.dao.mysql.PlayerDao;
import org.alexdev.kepler.game.messenger.MessengerUser;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.player.PlayerManager;
import org.alexdev.kepler.messages.outgoing.messenger.FRIEND_REQUEST;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

public class MESSENGER_REQUESTBUDDY implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        int userId = PlayerDao.getId(reader.readString());

        if (userId == -1 || userId == player.getDetails().getId()) {
            return;
        }

        if (player.getMessenger().isFriend(userId)) {
            return;
        }

        if (MessengerDao.requestExists(player.getDetails().getId(), userId) ||
            MessengerDao.requestExists(userId, player.getDetails().getId())) {
            return;
        }

        MessengerDao.newRequest(player.getDetails().getId(), userId);

        Player requested = PlayerManager.getInstance().getPlayerById(userId);

        if (requested != null) {
            requested.send(new FRIEND_REQUEST(player.getDetails().getId(), player.getDetails().getName()));
            requested.getMessenger().getRequests().add(new MessengerUser(player.getDetails().getId()));
        }
    }
}
