package org.alexdev.kepler.game.messenger;

import org.alexdev.kepler.dao.mysql.MessengerDao;
import org.alexdev.kepler.game.player.Player;

import java.util.List;

public class Messenger {
    private Player player;
    private List<MessengerUser> friends;
    private List<MessengerUser> requests;

    public Messenger(Player player) {
        this.player = player;
        this.friends = MessengerDao.getFriends(player.getDetails().getId());
        this.requests = MessengerDao.getRequests(player.getDetails().getId());
    }

    public boolean isFriend(int userId) {
        return this.getFriend(userId) != null;
    }

    public boolean hasRequest(int userId) {
        return this.getRequest(userId) != null;
    }

    public MessengerUser getRequest(int userId) {
        for (MessengerUser requester : this.requests) {
            if (requester.getUserId() == userId) {
                return requester;
            }
        }

        return null;
    }

    public MessengerUser getFriend(int userId) {
        for (MessengerUser friend : this.friends) {
            if (friend.getUserId() == userId) {
                return friend;
            }
        }

        return null;
    }

    public List<MessengerUser> getFriends() {
        return friends;
    }

    public List<MessengerUser> getRequests() {
        return requests;
    }
}
