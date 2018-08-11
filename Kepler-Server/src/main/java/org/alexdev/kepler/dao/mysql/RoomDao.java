package org.alexdev.kepler.dao.mysql;

import org.alexdev.kepler.dao.Storage;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.RoomData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomDao {

    public static void resetVisitors() {
        Storage.getStorage().execute("UPDATE rooms SET visitors_now = 0 WHERE visitors_now > 0");
    }

    /**
     * Get a list of rooms by the owner id, use "0" for public rooms.
     *
     * @param userId the user id to get the rooms by
     * @return the list of rooms
     */
    public static List<Room> getRoomsByUserId(int userId) {
        List<Room> rooms = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM rooms LEFT JOIN users ON rooms.owner_id = users.id WHERE rooms.owner_id = ?", sqlConnection);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Room room = new Room();
                fill(room.getData(), resultSet);
                rooms.add(room);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return rooms;
    }

    /**
     * Get a list of random rooms.
     *
     * @param limit the limit of rooms
     * @return the list of rooms
     */
    public static List<Room> getRandomRooms(int limit) {
        List<Room> rooms = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //SELECT * FROM rooms LEFT JOIN users ON rooms.owner_id = users.id WHERE owner_id = 0

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM rooms LEFT JOIN users ON rooms.owner_id = users.id WHERE owner_id > 0 AND accesstype = 0 ORDER BY RAND() LIMIT ?", sqlConnection);
            preparedStatement.setInt(1, limit);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Room room = new Room();
                fill(room.getData(), resultSet);
                rooms.add(room);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return rooms;
    }

    /**
     * Get the room id of a room by its model, used for walkways.
     *
     * @param model the model used to get the id for
     * @return the id, else -1
     */
    public static int getRoomIdByModel(String model) {
        int roomId = -1;

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT id FROM rooms WHERE model = ?", sqlConnection);
            preparedStatement.setString(1, model);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                roomId = resultSet.getInt("id");
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return roomId;
    }


    /**
     * Search query for when people use the navigator search, will search either by username or room name similarities.
     *
     * @param searchQuery the query to use
     * @return the list of possible room matches
     */
    public static List<Room> querySearchRooms(String searchQuery) {
        List<Room> rooms = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM rooms INNER JOIN users ON rooms.owner_id = users.id WHERE LOWER(users.username) LIKE ? OR LOWER(rooms.name) LIKE ? LIMIT 30", sqlConnection);
            preparedStatement.setString(1, "%" + searchQuery + "%");
            preparedStatement.setString(2, "%" + searchQuery + "%");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Room room = new Room();
                fill(room.getData(), resultSet);
                rooms.add(room);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return rooms;
    }

    public static Room getRoomById(int roomId) {
        Room room = null;

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM rooms LEFT JOIN users ON rooms.owner_id = users.id WHERE rooms.id = ?", sqlConnection);
            preparedStatement.setInt(1, roomId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                room = new Room();
                fill(room.getData(), resultSet);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return room;
    }

    /**
     * Save all room information.
     *
     * @param room the room to save
     */
    public static void saveDecorations(Room room) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("UPDATE rooms SET wallpaper = ?, floor = ? WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, room.getData().getWallpaper());
            preparedStatement.setInt(2, room.getData().getFloor());
            preparedStatement.setInt(3, room.getId());
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    /**
     * Save all room information.
     *
     * @param room the room to save
     */
    public static void save(Room room) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("UPDATE rooms SET category = ?, name = ?, description = ?, showname = ?, superusers = ?, accesstype = ?, password = ?, visitors_max = ? WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, room.getData().getCategoryId());
            preparedStatement.setString(2, room.getData().getName());
            preparedStatement.setString(3, room.getData().getDescription());
            preparedStatement.setBoolean(4, room.getData().showName());
            preparedStatement.setBoolean(5, room.getData().allowSuperUsers());
            preparedStatement.setInt(6, room.getData().getAccessTypeId());
            preparedStatement.setString(7, room.getData().getPassword());
            preparedStatement.setInt(8, room.getData().getVisitorsMax());
            preparedStatement.setInt(9, room.getId());
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }


    /**
     * Save visitor count of rooms
     *
     * @param room the room to save
     */
    public static void saveVisitors(Room room) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("UPDATE rooms SET visitors_now = ? WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, room.getData().getVisitorsNow());
            preparedStatement.setInt(2, room.getId());
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }


    /**
     * Delete room.
     *
     * @param room the room to delete
     */
    public static void delete(Room room) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("DELETE FROM rooms WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, room.getId());
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    /**
     * Vote for a room
     *
     * @param userId    the User who is voting
     * @param roomId    the Room that the user is voting for
     * @param voteValue the Value of the vote (1 or -1)
     */
    public static void vote(int userId, int roomId, int voteValue) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("INSERT INTO users_room_votes (user_id, room_id, vote) VALUES (?, ?, ?)", sqlConnection);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, roomId);
            preparedStatement.setInt(3, voteValue);
            preparedStatement.execute();

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    /**
     * Vote for a room
     *
     * @param userId the User who is voting
     * @param roomId the Room that the user is voting for
     */
    public static void removeVote(int userId, int roomId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("DELETE FROM users_room_votes WHERE user_id = ? AND room_id = ?", sqlConnection);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, roomId);
            preparedStatement.execute();

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    /**
     * Get room rating.
     *
     * @param roomId the room id to get the rating for
     */
    public static int getRating(int roomId) {
        int rating = 0;

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT SUM(vote) AS rating FROM users_room_votes WHERE room_id = ?;", sqlConnection);
            preparedStatement.setInt(1, roomId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                rating = resultSet.getInt("rating");
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return rating;
    }

    /**
     * Check if a user has voted for a room
     *
     * @param userId the User who is voting
     * @param roomId the Room that the user is voting for
     * @return true if the user has voted, false if not
     */
    public static boolean hasVoted(int userId, int roomId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet;

        boolean hasVoted = false;

        try {
            sqlConnection = Storage.getStorage().getConnection();

            preparedStatement = Storage.getStorage().prepare("SELECT * FROM users_room_votes WHERE room_id = ? AND user_id = ? LIMIT 1", sqlConnection);
            preparedStatement.setInt(1, roomId);
            preparedStatement.setInt(2, userId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                hasVoted = true;
            }
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return hasVoted;
    }

    /**
     * Fill player data
     *
     * @param data the room data instance
     * @param row  the row
     * @throws SQLException the SQL exception
     */
    public static void fill(RoomData data, ResultSet row) throws SQLException {
        if (data == null) {
            return;
        }

        String ownerName = row.getString("username");

        data.fill(row.getInt("id"), row.getInt("owner_id"), ownerName != null ? ownerName : "", row.getInt("category"),
                row.getString("name"), row.getString("description"), row.getString("model"),
                row.getString("ccts"), row.getInt("wallpaper"), row.getInt("floor"), row.getBoolean("showname"),
                row.getBoolean("superusers"), row.getInt("accesstype"), row.getString("password"),
                row.getInt("visitors_now"), row.getInt("visitors_max"), 0);

    }
}
