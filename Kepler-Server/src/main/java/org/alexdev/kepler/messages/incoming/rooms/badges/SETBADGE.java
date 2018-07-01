package org.alexdev.kepler.messages.incoming.rooms.badges;

import org.alexdev.kepler.dao.mysql.PlayerDao;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.messages.outgoing.rooms.badges.USER_BADGE;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

public class SETBADGE implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        // TODO: find out what first VL64 read is, ignore for now
        var stuff = reader.readInt();

        String newBadge = reader.readString();

        // Return if user does not have badge
        if (!player.getDetails().getBadges().contains(newBadge)) {
            return;
        }

        boolean showBadge = reader.readBoolean();

        // Update user object
        player.getDetails().setCurrentBadge(newBadge);
        player.getDetails().setShowBadge(showBadge);

        // Notify room
        if (player.getRoomUser().getRoom() != null) {
            player.getRoomUser().getRoom().send(new USER_BADGE(player.getRoomUser().getInstanceId(), player.getDetails()));
        }

        // Persist to database
        PlayerDao.saveCurrentBadge(player.getDetails());
    }
}