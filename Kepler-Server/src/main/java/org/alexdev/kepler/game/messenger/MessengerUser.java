package org.alexdev.kepler.game.messenger;

import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.player.PlayerDetails;
import org.alexdev.kepler.game.player.PlayerManager;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.server.netty.streams.NettyResponse;
import org.alexdev.kepler.util.DateUtil;

public class MessengerUser {
    private final int userId;

    public MessengerUser(int userId) {
        this.userId = userId;
    }

    public void serialise(NettyResponse response) {
        PlayerDetails details = PlayerManager.getInstance().getPlayerData(this.userId);

        if (details == null) {
            return;
        }

        response.writeInt(details.getId());
        response.writeString(details.getName());
        response.writeBool(details.getSex().toLowerCase().equals("m"));
        response.writeString(details.getConsoleMotto());

        Player player = PlayerManager.getInstance().getPlayerById(details.getId());

        boolean isOnline = (player != null);
        response.writeBool(isOnline);

        if (isOnline) {
            if (player.getRoomUser().getRoom() != null) {
                Room room = player.getRoomUser().getRoom();

                if (room.getData().getOwnerId() > 0) {
                    response.writeString("Floor1a");
                } else {
                    response.writeString(room.getData().getName());
                }
            }

            response.writeString("On hotel view");
        } else {
            response.writeString("");
        }

        response.writeString(DateUtil.getDateAsString(details.getLastOnline()));
        response.writeString(details.getFigure());
    }

    public int getUserId() {
        return this.userId;
    }
}
