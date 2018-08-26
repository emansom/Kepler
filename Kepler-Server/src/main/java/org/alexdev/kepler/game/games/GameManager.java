package org.alexdev.kepler.game.games;

import org.alexdev.kepler.dao.mysql.GameDao;
import org.alexdev.kepler.dao.mysql.GameSpawn;
import org.alexdev.kepler.game.games.player.GameRank;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.models.RoomModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class GameManager {
    private static GameManager instance = null;

    private AtomicInteger idTracker;

    private List<GameSpawn> spawnList;
    private List<GameRank> rankList;
    private List<RoomModel> modelList;
    private List<Game> games;

    public GameManager() {
        this.rankList = GameDao.getRanks();
        this.modelList = GameDao.getGameMaps();
        this.spawnList = GameDao.getGameSpawns();

        this.games = new ArrayList<>();
        this.idTracker = new AtomicInteger(0);
    }

    /**
     * Get the game spawn by gamr type, map id and team id
     * @param gameType the game type (battleball or snowstorm)
     * @param mapId the map id
     * @param teamId the team id
     * @return the game spawn
     */
    public GameSpawn getGameSpawn(GameType gameType, int mapId, int teamId) {
        for (GameSpawn gameSpawn : this.spawnList) {
            if ((gameSpawn.getGameType() == gameType) && (gameSpawn.getMapId() == mapId) && (gameSpawn.getTeamId() == teamId)) {
                return gameSpawn;
            }
        }

        return null;
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
     * Gets a game instance by specified game id
     *
     * @param gameId the game id used
     * @return the game instance
     */
    public Game getGameById(int gameId) {
        for (Game game : this.games) {
            if (game.getId() == gameId) {
                return game;
            }
        }

        return null;
    }

    /**
     * Get the list of games by type
     *
     * @param gameType the type of game
     * @return the list of games
     */
    public List<Game> getGamesByType(GameType gameType) {
        return this.games.stream().filter(
                game -> game.getGameType() == gameType
        ).collect(Collectors.toList());
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

    /**
     * Get model by type and map id
     *
     * @return the room model instance
     */
    public RoomModel getModel(GameType type, int mapId) {
        String prefix = (type == GameType.BATTLEBALL ? "bb" : "ss");

        for (RoomModel roomModel : this.modelList) {
            if (roomModel.getName().equals(prefix + "_arena_" + mapId)) {
                return roomModel;
            }
        }

        return null;
    }
}
