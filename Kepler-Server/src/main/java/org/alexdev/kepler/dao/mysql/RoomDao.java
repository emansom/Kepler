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

    public static List<Room> getRoomsByUserId(int roomId) {
        List<Room> rooms = new ArrayList<Room>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM rooms WHERE owner_id = ?", sqlConnection);
            preparedStatement.setInt(1, roomId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Room room = new Room();
                fill(room.getData(), resultSet);
                rooms.add(room);
                //fuserights.put(resultSet.getString("fuseright"), resultSet.getInt("min_rank"));
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
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM rooms WHERE id = ?", sqlConnection);
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
    public static void save(Room room) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("UPDATE rooms SET category = ?, name = ?, description = ?, wallpaper = ?, floor = ?, showname = ?, superusers = ?, accesstype = ?, password = ?, visitors_max = ? WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, room.getData().getCategoryId());
            preparedStatement.setString(2, room.getData().getName());
            preparedStatement.setString(3, room.getData().getDescription());
            preparedStatement.setInt(4, room.getData().getWallpaper());
            preparedStatement.setInt(5, room.getData().getFloor());
            preparedStatement.setBoolean(6, room.getData().showName());
            preparedStatement.setBoolean(7, room.getData().allowSuperUsers());
            preparedStatement.setInt(8, room.getData().getAccessTypeId());
            preparedStatement.setString(9, room.getData().getPassword());
            preparedStatement.setInt(10, room.getData().getVisitorsMax());
            preparedStatement.setInt(11, room.getId());
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
     * Fill player data
     *
     * @param data the room data instance
     * @param row the row
     * @throws SQLException the SQL exception
     */
    public static void fill(RoomData data, ResultSet row) throws SQLException {
        if (data == null) {
            return;
        }

        data.fill(row.getInt("id"), row.getInt("owner_id"), row.getInt("category"),
                row.getString("name"), row.getString("description"), row.getString("model"),
                row.getString("ccts"), row.getInt("wallpaper"), row.getInt("floor"), row.getBoolean("showname"),
                row.getBoolean("superusers"), row.getInt("accesstype"), row.getString("password"),
                row.getInt("visitors_now"), row.getInt("visitors_max"));

    }
}
