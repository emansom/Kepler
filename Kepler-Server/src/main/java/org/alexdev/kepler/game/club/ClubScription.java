package org.alexdev.kepler.game.club;

import org.alexdev.kepler.dao.mysql.CurrencyDao;
import org.alexdev.kepler.dao.mysql.PlayerDao;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.messages.outgoing.user.CLUB_INFO;
import org.alexdev.kepler.messages.outgoing.user.CREDIT_BALANCE;
import org.alexdev.kepler.util.DateUtil;

public class ClubScription {
    public static void refreshSubscription(Player player) {
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
        }

        player.send(new CLUB_INFO(remainingDaysThisMonth, sinceMonths, prepaidMonths));
    }

    public static void subscribeClub(Player player, int days, int credits) {
        if (days <= 0) {
            return;
        }

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

        if (credits > 0) {
            CurrencyDao.decreaseCredits(player.getDetails(), credits);
            player.send(new CREDIT_BALANCE(player.getDetails()));
        }
    }
}
