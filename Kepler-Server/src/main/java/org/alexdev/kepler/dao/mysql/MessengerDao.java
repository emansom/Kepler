package org.alexdev.kepler.dao.mysql;

import org.alexdev.kepler.dao.Storage;
import org.alexdev.kepler.game.messenger.MessengerUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessengerDao {

    /**
     * Gets the friends.
     *
     * @param from_id the user id
     * @return the friends
     */
    public static List<MessengerUser> getFriends(int from_id) {
        List<MessengerUser> friends = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM messenger_friends WHERE (from_id = " + from_id + ") OR (to_id = " + from_id + ")", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                MessengerUser friend = null;

                if (resultSet.getInt("from_id") != from_id) {
                    friend = new MessengerUser(resultSet.getInt("from_id"));
                } else {
                    friend = new MessengerUser(resultSet.getInt("to_id"));
                }

                friends.add(friend);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return friends;
    }


    /**
     * Gets the requests.
     *
     * @param userId the user id
     * @return the requests
     */
    public static List<MessengerUser> getRequests(int userId) {
        List<MessengerUser> users = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM messenger_requests WHERE to_id = " + userId, sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                users.add(new MessengerUser(resultSet.getInt("from_id")));
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return users;
    }

    /**
     * Search.
     *
     * @param query the query
     * @return the list
     */
    public static Integer search(String query) {
        int userId = -1;

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT id FROM users WHERE username LIKE ? LIMIT 30", sqlConnection);
            preparedStatement.setString(1, query + "%");

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                userId = resultSet.getInt("id");
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return userId;
    }

    /**
     * New request.
     *
     * @param fromId the from id
     * @param toId   the to id
     * @return true, if successful
     */
    public static boolean newRequest(int fromId, int toId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        boolean success = false;

        if (!requestExists(fromId, toId)) {

            try {
                sqlConnection = Storage.getStorage().getConnection();
                preparedStatement = Storage.getStorage().prepare("INSERT INTO messenger_requests (to_id, from_id) VALUES (?, ?)", sqlConnection);
                preparedStatement.setInt(1, toId);
                preparedStatement.setInt(2, fromId);
                preparedStatement.execute();
                success = true;
            } catch (SQLException e) {
                Storage.logError(e);
            } finally {
                Storage.closeSilently(preparedStatement);
                Storage.closeSilently(sqlConnection);
            }
        }

        return success;
    }

    /**
     * Request exists.
     *
     * @param fromId the from id
     * @param toId   the to id
     * @return true, if successful
     */
    public static boolean requestExists(int fromId, int toId) {
        boolean exists = false;

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM messenger_requests WHERE (to_id = '" + toId + "') AND (from_id = '" + fromId + "') OR (from_id = '" + toId + "') AND (to_id = '" + fromId + "')", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                exists = true;
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return exists;
    }

    /**
     * Removes the request.
     *
     * @param fromId the from id
     * @param toId   the to id
     */
    public static void removeRequest(int fromId, int toId) {
        Storage.getStorage().execute("DELETE FROM messenger_requests WHERE from_id = " + fromId + " AND to_id = " + toId);
    }

    /**
     * Removes the friend.
     *
     * @param toId the friend id
     * @param fromId   the user id
     */
    public static void removeFriend(int toId, int fromId) {
        Storage.getStorage().execute("DELETE FROM messenger_friends WHERE (from_id = " + fromId + " AND to_id = " + toId + ") OR (from_id = " + fromId + " AND to_id = " + toId + ")");
    }

    /**
     * New friend.
     *
     * @param fromId the sender
     * @param toId the receiver
     * @return true, if successful
     */
    public static void newFriend(int fromId, int toId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("INSERT INTO messenger_friends (from_id, to_id) VALUES (?, ?)", sqlConnection);
            preparedStatement.setInt(1, fromId);
            preparedStatement.setInt(2, toId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }
}