package org.alexdev.kepler.messages.incoming.club;

import org.alexdev.kepler.dao.mysql.PlayerDao;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.messages.outgoing.user.CLUB_INFO;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;
import org.alexdev.kepler.util.DateUtil;

public class GET_CLUB implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        long now = DateUtil.getCurrentTimeSeconds();

        int sinceMonths = 0;
        int totalDays = 0;
        int remainingDaysThisMonth = 0;
        int prepaidMonths = 0;

        if (player.getDetails().getClubExpiration() != 0)
            totalDays = (int)((player.getDetails().getClubExpiration() - now) / 60 / 60 / 24);

        if (totalDays < 0)
            totalDays = 0;

        if (totalDays > 0) {
            remainingDaysThisMonth = ((totalDays - 1) % 31) + 1;
            prepaidMonths = (totalDays - remainingDaysThisMonth) / 31;

            if (player.getDetails().getClubSubscribed() > 0) {
                sinceMonths = (int) (now - player.getDetails().getClubSubscribed()) / 60 / 60 / 24 / 31;
            }
        } else {
            // If the database still thinks we have Habbo club, better reset it back to 0.
            if (player.getDetails().getClubExpiration() > 0) {
                player.getDetails().setClubSubscribed(0);
                player.getDetails().setClubExpiration(0);

                player.refreshFuserights();
                PlayerDao.saveSubscription(player.getDetails());
            }
        }

        player.send(new CLUB_INFO(remainingDaysThisMonth, sinceMonths, prepaidMonths));
    }
}
