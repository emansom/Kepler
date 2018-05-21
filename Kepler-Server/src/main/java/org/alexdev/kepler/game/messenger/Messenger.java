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

    public List<MessengerUser> getFriends() {
        return friends;
    }

    public List<MessengerUser> getRequests() {
        return requests;
    }
}
