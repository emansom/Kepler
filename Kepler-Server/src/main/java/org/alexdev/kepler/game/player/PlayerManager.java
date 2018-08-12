package org.alexdev.kepler.game.player;

import org.alexdev.kepler.dao.mysql.PlayerDao;
import org.alexdev.kepler.game.room.enums.StatusType;
import org.alexdev.kepler.messages.outgoing.openinghours.INFO_HOTEL_CLOSED;
import org.alexdev.kepler.util.DateUtil;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class PlayerManager {
    private static PlayerManager instance;
    private List<Player> players;

    private long timeUntilNextReset;
    private long dailyPlayerPeak;

    public PlayerManager() {
        this.players = new CopyOnWriteArrayList<>();
    }

    /**
     * Checks and sets the daily player peak.
     */
    public void checkPlayerPeak() {
        if (DateUtil.getCurrentTimeSeconds() > this.timeUntilNextReset) {
            this.timeUntilNextReset = DateUtil.getCurrentTimeSeconds() + TimeUnit.DAYS.toSeconds(1);
            this.dailyPlayerPeak = PlayerManager.getInstance().getPlayers().size();
        } else {
            int newSize = PlayerManager.getInstance().getPlayers().size();

            if (newSize > this.dailyPlayerPeak) {
                this.dailyPlayerPeak = newSize;
            }
        }

    }

    /**
     * Get a player by user id.
     *
     * @param userId the user id to get with
     * @return the player, else null if not found
     */
    public Player getPlayerById(int userId) {
        for (Player player : this.players) {
            if (player.getDetails().getId() == userId) {
                return player;
            }
        }

        return null;
    }

    /**
     * Get the player by name.
     *
     * @param username the name to get with
     * @return the player, else null if not found
     */
    public Player getPlayerByName(String username) {
        for (Player player : this.players) {
            if (player.getDetails().getName().equalsIgnoreCase(username)) {
                return player;
            }
        }

        return null;
    }

    /**
     * Get a player data by user id.
     *
     * @param userId the user id to get with
     * @return the player data, else if offline will query the database, else null
     */
    public PlayerDetails getPlayerData(int userId) {
        if (getPlayerById(userId) != null) {
            return getPlayerById(userId).getDetails();
        }

        return PlayerDao.getDetails(userId);
    }

    /**
     * Remove player from map, this is handled automatically when
     * the socket is closed.
     *
     * @param player the player to remove
     */
    public void removePlayer(Player player) {
        if (player.getDetails().getName() == null) {
            return;
        }

        this.players.remove(player);
    }

    /**
     * Remove player from map, this is handled automatically when
     * the player is logged in.
     *
     * @param player the player to remove
     */
    public void addPlayer(Player player) {
        if (player.getDetails() == null) {
            return;
        }

        this.players.add(player);
    }

    /**
     * Disconnect a session by user id.
     *
     * @param userId the user id of the session to disconnect
     */
    public void disconnectSession(int userId) {
        for (Player player : this.players) {
            if (player.getDetails().getId() == userId) {
                player.kickFromServer(true);
            }
        }
    }

    /**
     * Close and dispose all users.
     */
    public void dispose() {
        for (Player p : this.getPlayers()) {
            // First send fancy maintenance popup to client
            // (disconnect parameter denotes if the ugly popup is used or the more fancy one)
            p.send(new INFO_HOTEL_CLOSED(LocalTime.now(), false));

            // Now disconnect the player
            p.kickFromServer(true);
        }
    }

    /**
     * Get the collection of players on the server.
     *
     * @return the collection of players
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(this.players);
    }

    /**
     * Get the collection of active players on the server.
     *
     * @return the collection of active players
     */
    public Collection<Player> getActivePlayers() {
        List<Player> activePlayers = new ArrayList<>();
        for (Player player : PlayerManager.getInstance().getPlayers()) {
            if (player.getRoomUser().getRoom() == null) {
                continue;
            }

            if (player.getRoomUser().containsStatus(StatusType.SLEEP)) {
                continue;
            }

            activePlayers.add(player);
        }

        return activePlayers;
    }

    public long getDailyPlayerPeak() {
        return dailyPlayerPeak;
    }

    /**
     * Gets the instance
     *
     * @return the instance
     */
    public static PlayerManager getInstance() {
        if (instance == null) {
            instance = new PlayerManager();
        }

        return instance;
    }
}
