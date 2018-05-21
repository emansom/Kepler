package org.alexdev.kepler.game.player;

import org.alexdev.kepler.dao.mysql.PlayerDao;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager {
    private static PlayerManager instance;

    private ConcurrentHashMap<Integer, Player> playerIdMap;
    private ConcurrentHashMap<String, Player> playerNameMap;

    public PlayerManager() {
        this.playerIdMap = new ConcurrentHashMap<>();
        this.playerNameMap = new ConcurrentHashMap<>();
    }

    public Player getPlayerById(int userId) {
        if (this.playerIdMap.containsKey(userId)) {
            return this.playerIdMap.get(userId);
        }

        return null;
    }

    public PlayerDetails getPlayerData(int userId) {
        if (this.playerIdMap.containsKey(userId)) {
            return this.playerIdMap.get(userId).getDetails();
        }

        return PlayerDao.getDetails(userId);
    }

    public Player getPlayerByName(String username) {
        if (this.playerNameMap.containsKey(username)) {
            return this.playerNameMap.get(username);
        }

        return null;
    }

    public Collection<Player> getPlayers() {
        return this.playerIdMap.values();
    }

    public void removePlayer(Player player) {
        if (player.getDetails() == null) {
            return;
        }

        this.playerNameMap.remove(player.getDetails().getName());
        this.playerIdMap.remove(player.getDetails().getId());
    }

    public void addPlayer(Player player) {
        if (player.getDetails() == null) {
            return;
        }

        this.playerNameMap.put(player.getDetails().getName(), player);
        this.playerIdMap.put(player.getDetails().getId(), player);
    }

    public static PlayerManager getInstance() {
        if (instance == null) {
            instance = new PlayerManager();
        }

        return instance;
    }
}
