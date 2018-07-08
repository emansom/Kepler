package org.alexdev.kepler.dao.mysql;

import org.alexdev.kepler.dao.Storage;
import org.alexdev.kepler.game.messenger.MessengerMessage;
import org.alexdev.kepler.game.messenger.MessengerUser;
import org.alexdev.kepler.log.Log;
import org.alexdev.kepler.util.DateUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessengerDao {

    /**
     * Gets the friends.
     *
     * @param userId the user id
     * @return the friends
     */
    public static List<MessengerUser> getFriends(int userId) {
        List<MessengerUser> friends = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT to_id, from_id FROM messenger_friends WHERE (to_id = ?) OR (from_id = ?)", sqlConnection);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, userId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int toId = resultSet.getInt("to_id");
                int fromId = resultSet.getInt("from_id");

                int friendId = -1;

                if (toId != userId) {
                    friendId = toId;
                } else {
                    friendId = fromId;
                }

                friends.add(new MessengerUser(friendId));
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
            preparedStatement = Storage.getStorage().prepare("SELECT id FROM users WHERE username = ? LIMIT 30", sqlConnection);
            preparedStatement.setString(1, query);

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

    /**
     * Create a message for other people to read them later, if they're offline.
     *
     * @param fromId the id the user sending the message
     * @param toId the id of the user to receive it
     * @param message the body of the message
     * @return the id of the message
     */
    public static int newMessage(int fromId, int toId, String message) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        int messageID = 0;

        try {

            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("INSERT INTO messenger_messages (receiver_id, sender_id, unread, body, date) VALUES (?, ?, ?, ?, ?)", sqlConnection);
            preparedStatement.setInt(1, toId);
            preparedStatement.setInt(2, fromId);
            preparedStatement.setInt(3, 1);
            preparedStatement.setString(4, message);
            preparedStatement.setLong(5, DateUtil.getCurrentTimeSeconds());
            preparedStatement.executeUpdate();

            ResultSet row = preparedStatement.getGeneratedKeys();

            if (row != null && row.next()) {
                messageID = row.getInt(1);
            }

        } catch (SQLException e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return messageID;
    }

    /**
     * Get unread messages for the user.
     *
     * @param userId the id of the user to get the offline messages for
     * @return the list of messages
     */
    public static List<MessengerMessage> getUnreadMessages(int userId) {
        List<MessengerMessage> messages = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM messenger_messages WHERE receiver_id = " + userId + " AND unread = 1", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                messages.add(new MessengerMessage(
                        resultSet.getInt("id"), resultSet.getInt("receiver_id"), resultSet.getInt("sender_id"),
                        resultSet.getLong("date"), resultSet.getString("body")));

            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return messages;
    }

    /**
     * Mark a message as read.
     *
     * @param messageId the message id to reset
     */
    public static void markMessageRead(int messageId) {
        Storage.getStorage().execute("UPDATE messenger_messages SET unread = 0 WHERE id = " + messageId);
    }
}