package org.alexdev.kepler.messages.incoming.club;

import org.alexdev.kepler.dao.mysql.CurrencyDao;
import org.alexdev.kepler.dao.mysql.PlayerDao;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.messages.outgoing.user.CREDIT_BALANCE;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;
import org.alexdev.kepler.util.DateUtil;

public class SUBSCRIBE_CLUB implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        reader.readString();

        int days = -1;
        int credits = -1;

        int choice = reader.readInt();

        switch (choice) {
            case 1:
            {
                credits = 25;
                days = 31;
                break;
            }
            case 2:
            {
                credits = 60;
                days = 93;
                break;
            }
            case 3:
            {
                credits = 105;
                days = 186;
                break;
            }
        }

        if (days > 0) {
            if (player.getDetails().getCredits() < credits) {
                return;
            }

            long now = DateUtil.getCurrentTimeSeconds();

            long daysInSeconds = 24 * 60 * 60;
            long secondsToAdd = (daysInSeconds * days);

            if (player.getDetails().getClubSubscribed() == 0) {
                player.getDetails().setClubSubscribed(now);
            }

            if (player.getDetails().getClubExpiration() - now <= 0) {
                player.getDetails().setClubExpiration(now + secondsToAdd + 1);
            } else {
                player.getDetails().setClubExpiration(player.getDetails().getClubExpiration() + secondsToAdd);
            }


            player.refreshClub();
            player.refreshFuserights();

            PlayerDao.saveSubscription(player.getDetails());
            CurrencyDao.decreaseCredits(player.getDetails(), credits);

            player.send(new CREDIT_BALANCE(player.getDetails()));
        }
    }
}
