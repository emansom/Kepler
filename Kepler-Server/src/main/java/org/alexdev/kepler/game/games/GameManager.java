package org.alexdev.kepler.game.games;

import com.github.bhlangonijr.chesslib.game.Game;
import org.alexdev.kepler.dao.mysql.GameDao;
import org.alexdev.kepler.game.games.player.GameRank;
import org.alexdev.kepler.game.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class GameManager {
    private static GameManager instance = null;

    private AtomicInteger idTracker;
    private List<GameRank> rankList;
    private List<Game> games;

    public GameManager() {
        this.rankList = GameDao.getRanks();
        this.games = new ArrayList<>();
        this.idTracker = new AtomicInteger(0);
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

    /**
     * Creates a new game id for the game
     *
     * @return the game id
     */
    public int createId() {
        return idTracker.incrementAndGet();
    }

    /**
     * Gets the list of currently active games
     *
     * @return the list of games
     */
    public List<Game> getGames() {
        return games;
    }
}
