package org.alexdev.kepler.messages.outgoing.messenger;

import org.alexdev.kepler.game.messenger.MessengerUser;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.player.PlayerDetails;
import org.alexdev.kepler.game.player.PlayerManager;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;
import org.alexdev.kepler.util.DateUtil;

import java.util.List;

public class FRIENDS_UPDATE extends MessageComposer {
    private final List<MessengerUser> friends;

    public FRIENDS_UPDATE(List<MessengerUser> friends) {
        this.friends = friends;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(this.friends.size());

        for (MessengerUser friend : this.friends) {
            PlayerDetails details = PlayerManager.getInstance().getPlayerData(friend.getUserId());

            response.writeInt(details.getId());
            response.writeString(details.getConsoleMotto());
            friend.serialiseStatus(response, details);
            response.writeString("");
            response.writeString(DateUtil.getDateAsString(details.getLastOnline()));
        }
    }

    @Override
    public short getHeader() {
        return 13; // "@M"
    }
}
