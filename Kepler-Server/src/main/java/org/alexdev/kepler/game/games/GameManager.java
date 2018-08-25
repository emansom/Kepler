package org.alexdev.kepler.game.games;

import org.alexdev.kepler.dao.mysql.GameDao;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.RoomManager;

import java.util.List;
import java.util.stream.Collectors;

public class GameManager {
    private static GameManager instance = null;
    private List<GameRank> rankList;

    public GameManager() {
        this.rankList = GameDao.getRanks();
    }

    /**
     * Get the game rank by the player points.
     *
     * @param type the type of game to get the points for
     * @param player the player to get the points for
     * @return the rank, null otherwise
     */
    public GameRank getRankByPoints(GameType type, Player player) {
        int score = player.getDetails().getGamePoints(type);

        for (GameRank rank : this.rankList) {
            if (score >= rank.getMinPoints()) {
                if (rank.getMaxPoints() == 0 || score <= rank.getMaxPoints()) {
                    return rank;
                }
            }
        }

        return null;
    }

    /**
     * Get the list of game ranks by type
     *
     * @param gameType the type of game to get the ranks for
     * @return the list of ranks
     */
    public List<GameRank> getRanksByType(GameType gameType) {
        return this.rankList.stream().filter(
                rank -> rank.getType() == gameType
        ).collect(Collectors.toList());
    }

    /**
     * Get the instance of {@link GameManager}
     *
     * @return the instance
     */
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }

        return instance;
    }
}
