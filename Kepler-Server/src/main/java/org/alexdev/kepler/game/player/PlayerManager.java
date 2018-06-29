package org.alexdev.kepler.game.player;

import org.alexdev.kepler.dao.mysql.PlayerDao;
import org.alexdev.kepler.game.room.enums.StatusType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager {
    private static PlayerManager instance;

    private ConcurrentHashMap<Integer, Player> playerIdMap;
    private ConcurrentHashMap<String, Player> playerNameMap;

    public PlayerManager() {
        this.playerIdMap = new ConcurrentHashMap<>();
        this.playerNameMap = new ConcurrentHashMap<>();
    }

    /**
     * Get a player by user id.
     *
     * @param userId the user id to get with
     * @return the player, else null if not found
     */
    public Player getPlayerById(int userId) {
        if (this.playerIdMap.containsKey(userId)) {
            return this.playerIdMap.get(userId);
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
        if (this.playerNameMap.containsKey(username)) {
            return this.playerNameMap.get(username);
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

        this.playerNameMap.remove(player.getDetails().getName());
        this.playerIdMap.remove(player.getDetails().getId());
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

        this.playerNameMap.put(player.getDetails().getName(), player);
        this.playerIdMap.put(player.getDetails().getId(), player);
    }

    /**
     * Disconnect a session by user id.
     *
     * @param userId the user id of the session to disconnect
     */
    public void disconnectSession(int userId) {
        Player player = this.getPlayerById(userId);

        if (player == null) {
            return;
        }

        player.kickFromServer();
    }

    public void dispose() {
        for (Player player : this.getPlayers()) {
            player.kickFromServer();
        }
    }

    /**
     * Get the collection of players on the server.
     *
     * @return the collection of players
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(this.playerIdMap.values());
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
