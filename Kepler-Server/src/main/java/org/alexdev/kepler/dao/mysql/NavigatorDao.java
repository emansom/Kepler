package org.alexdev.kepler.dao.mysql;

import org.alexdev.kepler.dao.Storage;
import org.alexdev.kepler.game.navigator.NavigatorCategory;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.util.DateUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NavigatorDao {

    /**
     * Get all categories from the database.
     *
     * @return map of categories
     */
    public static HashMap<Integer, NavigatorCategory> getCategories() {
        HashMap<Integer, NavigatorCategory> categories = new HashMap<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM rooms_categories ORDER BY order_id ASC ", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            //public NavigatorCategory(int id, String name, boolean publicSpaces, boolean allowTrading, int minimumRoleAccess, int minimumRoleSetFlat) {
            while (resultSet.next()) {
                NavigatorCategory category = new NavigatorCategory(
                        resultSet.getInt("id"), resultSet.getInt("parent_id"), resultSet.getString("name"),
                        resultSet.getBoolean("public_spaces"), resultSet.getBoolean("allow_trading"),
                        resultSet.getInt("minrole_access"), resultSet.getInt("minrole_setflatcat")
                );

                categories.put(category.getId(), category);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return categories;
    }

    /**
     * Get the list of recent rooms from database set by limit and category id.
     *
     * @param limit the maximum amount of usrs
     * @param categoryId the rooms to find under this category id
     * @return the list of recent rooms
     */
    public static List<Room> getRecentRooms(int limit, int categoryId) {
        List<Room> rooms = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM rooms WHERE category = ? AND owner_id > 0 AND visitors_now > 0 ORDER BY id DESC LIMIT ? ", sqlConnection);
            preparedStatement.setInt(1, categoryId);
            preparedStatement.setInt(2, limit);
            resultSet = preparedStatement.executeQuery();

            //public NavigatorCategory(int id, String name, boolean publicSpaces, boolean allowTrading, int minimumRoleAccess, int minimumRoleSetFlat) {
            while (resultSet.next()) {
                Room room = new Room();
                RoomDao.fill(room.getData(), resultSet);
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

    public static int createRoom(int ownerId, String roomName, String roomModel, boolean roomShowName, int accessType) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        int roomId = 0;

        try {

            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("INSERT INTO rooms (owner_id, name, description, model, showname, password, accesstype) VALUES (?,?,?,?,?, '', ?)", sqlConnection);
            preparedStatement.setInt(1, ownerId);
            preparedStatement.setString(2, roomName);
            preparedStatement.setString(3, "");
            preparedStatement.setString(4, roomModel);
            preparedStatement.setBoolean(5, roomShowName);
            preparedStatement.setInt(6, accessType);
            preparedStatement.executeUpdate();

            ResultSet row = preparedStatement.getGeneratedKeys();

            if (row != null && row.next()) {
                roomId = row.getInt(1);
            }

        } catch (SQLException e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return roomId;
    }
}
