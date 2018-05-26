package org.alexdev.kepler.dao.mysql;

import org.alexdev.kepler.dao.Storage;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.player.PlayerDetails;
import org.alexdev.kepler.util.DateUtil;

import java.sql.*;
import java.util.*;

public class PlayerDao {
    
    /**
     * Gets the details.
     *
     * @param userId the user id
     * @return the details
     */
    public static PlayerDetails getDetails(int userId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        PlayerDetails details = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM users WHERE id = ? LIMIT 1", sqlConnection);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                details = new PlayerDetails();
                fill(details, resultSet);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return details;
    }

    /**
     * Login.
     *
     * @param player the player
     * @param ssoTicket the sso ticket
     * @return true, if successful
     */
    public static boolean login(Player player, String ssoTicket) {
        boolean success = false;
        
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM users WHERE sso_ticket = ? LIMIT 1", sqlConnection);
            preparedStatement.setString(1, ssoTicket);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                fill(player.getDetails(), resultSet);
                success = true;
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return success;
    }

    /**
     * Gets the id.
     *
     * @param username the username
     * @return the id
     */
    public static int getId(String username) {
        int id = -1;
        
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT id FROM users WHERE username = ? LIMIT 1", sqlConnection);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return id;    
    }
    
    /**
     * Gets the name.
     *
     * @param id the id
     * @return the name
     */
    public static String getName(int id) {
        String name = null;
        
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT username FROM users WHERE id = ? LIMIT 1", sqlConnection);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                name = resultSet.getString("username");
            }
            
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return name;    
    }

    /**
     * Update last online.
     *
     * @param details the details of the user
     */
    public static void saveLastOnline(PlayerDetails details, long currentTime) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("UPDATE users SET last_online = ? WHERE id = ?", sqlConnection);
            preparedStatement.setLong(1, currentTime);
            preparedStatement.setInt(2, details.getId());
            preparedStatement.execute();

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    /**
     * Update details.
     *
     * @param details the player details to save
     */
    public static void saveDetails(PlayerDetails details) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("UPDATE users SET figure = ?, pool_figure = ?, sex = ?, rank = ? WHERE id = ?", sqlConnection);
            preparedStatement.setString(1, details.getFigure());
            preparedStatement.setString(2, details.getPoolFigure());
            preparedStatement.setString(3, details.getSex());
            preparedStatement.setInt(4, details.getRank());
            preparedStatement.setInt(5, details.getId());
            preparedStatement.execute();

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    /**
     * Update details.
     *
     * @param details the player details to save
     */
    public static void saveMotto(PlayerDetails details) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("UPDATE users SET motto = ?, console_motto = ? WHERE id = ?", sqlConnection);
            preparedStatement.setString(1, details.getMotto());
            preparedStatement.setString(2, details.getConsoleMotto());
            preparedStatement.setInt(3, details.getId());
            preparedStatement.execute();

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    /**
     * Update currency.
     *
     * @param details the player details to save
     */
    public static void saveCurrency(PlayerDetails details) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("UPDATE users SET credits = ?, tickets = ?, film = ? WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, details.getCredits());
            preparedStatement.setInt(2, details.getTickets());
            preparedStatement.setInt(3, details.getFilm());
            preparedStatement.setInt(4, details.getId());
            preparedStatement.execute();

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    /**
     * Update current badge
     *
     * @param details the player details to save
     */
    public static void saveCurrentBadge(PlayerDetails details) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("UPDATE users SET badge = ?, badge_active = ? WHERE id = ?", sqlConnection);
            preparedStatement.setString(1, details.getCurrentBadge());
            preparedStatement.setBoolean(2, details.getShowBadge());
            preparedStatement.setInt(3, details.getId());
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    public static List<String> getBadges(int userId) {
        List<String> badges = new ArrayList<>();

        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        ResultSet row = null;
        ResultSet row2 = null;

        // TODO: merge two queries somehow
        try {
            conn = Storage.getStorage().getConnection();

            stmt = Storage.getStorage().prepare("SELECT badge FROM users_badges WHERE user_id = ?", conn);
            stmt.setInt(1, userId);
            row = stmt.executeQuery();

            stmt2 = Storage.getStorage().prepare("SELECT rank_badges.badge FROM rank_badges LEFT JOIN users ON rank_badges.rank <= users.rank WHERE users.id = ?", conn);
            stmt2.setInt(1, userId);
            row2 = stmt2.executeQuery();

            while (row.next()) {
                badges.add(row.getString("badge"));
            }

            while (row2.next()) {
                badges.add(row2.getString("badge"));
            }

        } catch (Exception err) {
            Storage.logError(err);
        } finally {
            Storage.closeSilently(row);
            Storage.closeSilently(row2);
            Storage.closeSilently(stmt);
            Storage.closeSilently(stmt2);
            Storage.closeSilently(conn);
        }

        return badges;
    }

    /**
     * Fill player data
     *
     * @param details the details
     * @param row the row
     * @throws SQLException the SQL exception
     */
    private static void fill(PlayerDetails details, ResultSet row) throws SQLException {
        // public void fill(int id, String username, String password, String figure, String poolFigure, int credits, String motto, String consoleMotto, String sex,
        // int tickets, int film, int rank, long lastOnline, long clubSubscribed, long clubExpiration, String badge, String badgeActive) {
        if (details == null) {
            return;
        }

        details.fill(row.getInt("id"), row.getString("username"), row.getString("password"),
                row.getString("figure"), row.getString("pool_figure"), row.getInt("credits"),
                row.getString("motto"), row.getString("console_motto"), row.getString("sex"),
                row.getInt("tickets"), row.getInt("film"), row.getInt("rank"), row.getLong("last_online"),
                row.getLong("club_subscribed"), row.getLong("club_expiration"), row.getString("badge"),
                row.getBoolean("badge_active"), PlayerDao.getBadges(row.getInt("id")), row.getBoolean("allow_stalking"));
    }
}
