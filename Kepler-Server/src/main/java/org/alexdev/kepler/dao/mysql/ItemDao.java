package org.alexdev.kepler.dao.mysql;

import org.alexdev.kepler.dao.Storage;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.base.ItemDefinition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemDao {
    
    /**
     * Get the item definitions.
     *
     * @return the list of item definitions
     */
    public static Map<Integer, ItemDefinition> getItemDefinitions() {
        Map<Integer, ItemDefinition> definitions = new HashMap<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM items_definitions", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ItemDefinition definition = new ItemDefinition(resultSet.getInt("id"), resultSet.getString("sprite"),
                        resultSet.getString("behaviour"), resultSet.getDouble("top_height"), resultSet.getInt("length"),
                        resultSet.getInt("width"), resultSet.getString("colour"));

                definitions.put(definition.getId(), definition);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return definitions;
    }

    /**
     * Create new item entry with the definition id, user id and custom data. It will
     * override the current item id with its database id.
     *
     * @param item the item to create
     */
    public static void newItem(Item item) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        int itemId = 0;

        try {

            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("INSERT INTO items (user_id, definition_id, custom_data) VALUES (?,?,?)", sqlConnection);
            preparedStatement.setInt(1, item.getOwnerId());
            preparedStatement.setInt(2, item.getDefinition().getId());
            preparedStatement.setString(3, item.getCustomData());
            preparedStatement.executeUpdate();

            ResultSet row = preparedStatement.getGeneratedKeys();

            if (row != null && row.next()) {
                itemId = row.getInt(1);
            }

        } catch (SQLException e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        item.setId(itemId);
    }

    /**
     * Get the inventory list of items.
     *
     * @param userId the id of the user to get the inventory for
     * @return the list of items
     */
    public static List<Item> getInventory(int userId) {
        List<Item> items = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM items WHERE user_id = ? AND room_id = 0", sqlConnection);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Item item = new Item();
                fill(item, resultSet);
                items.add(item);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return items;
    }

    /**
     * Fill item with data retrieved from the SQL query.
     *
     * @param item the item to fill data for
     * @param resultSet the result set returned with the data
     * @throws SQLException an exception if an error happened
     */
    private static void fill(Item item, ResultSet resultSet) throws SQLException {
        item.setId(resultSet.getInt("id"));
        item.setOwnerId(resultSet.getInt("user_id"));
        item.setRoomId(resultSet.getInt("room_id"));
        item.setDefinitionId(resultSet.getInt("definition_id"));
        item.getPosition().setX(resultSet.getInt("x"));
        item.getPosition().setY(resultSet.getInt("y"));
        item.getPosition().setZ(resultSet.getDouble("z"));
        item.getPosition().setRotation(resultSet.getInt("rotation"));
        item.setWallPosition(resultSet.getString("wall_position"));
        item.setCustomData(resultSet.getString("custom_data"));
    }
}
