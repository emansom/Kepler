package org.alexdev.kepler.game.messenger;

import org.alexdev.kepler.dao.mysql.MessengerDao;
import org.alexdev.kepler.game.player.Player;

import java.util.List;
import java.util.Map;

public class Messenger {
    private Player player;
    private List<MessengerUser> friends;
    private List<MessengerUser> requests;
    private Map<Integer, MessengerMessage> offlineMessages;

    public Messenger(Player player) {
        this.player = player;
        this.friends = MessengerDao.getFriends(player.getDetails().getId());
        this.requests = MessengerDao.getRequests(player.getDetails().getId());
        this.offlineMessages = MessengerDao.getUnreadMessages(player.getDetails().getId());
    }

    /**
     * Get if the user already has a request from this user id.
     *
     * @param userId the user id to check for
     * @return true, if successful
     */
    public boolean hasRequest(int userId) {
        return this.getRequest(userId) != null;
    }

    /**
     * Get if the user already has a friend with this user id.
     *
     * @param userId the user id to check for
     * @return true, if successful
     */
    public boolean isFriend(int userId) {
        return this.getFriend(userId) != null;
    }

    /**
     * Get the messenger user instance with this user id.
     *
     * @param userId the user id to check for
     * @return the messenger user instance
     */
    public MessengerUser getRequest(int userId) {
        for (MessengerUser requester : this.requests) {
            if (requester.getUserId() == userId) {
                return requester;
            }
        }

        return null;
    }

    /**
     * Get the messenger user instance with this user id.
     *
     * @param userId the user id to check for
     * @return the messenger user instance
     */
    public MessengerUser getFriend(int userId) {
        for (MessengerUser friend : this.friends) {
            if (friend.getUserId() == userId) {
                return friend;
            }
        }

        return null;
    }

    /**
     * Get the list of offline messages.
     *
     * @return the list of offline messages
     */
    public Map<Integer, MessengerMessage> getOfflineMessages() {
        return offlineMessages;
    }

    /**
     * Get the list of friends.
     *
     * @return the list of friends
     */
    public List<MessengerUser> getFriends() {
        return friends;
    }

    /**
     * Get the list of friends.
     *
     * @return the list of friends
     */
    public List<MessengerUser> getRequests() {
        return requests;
    }
}