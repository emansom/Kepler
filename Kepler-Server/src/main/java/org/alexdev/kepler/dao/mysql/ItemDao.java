package org.alexdev.kepler.dao.mysql;

import org.alexdev.kepler.dao.Storage;
import org.alexdev.kepler.game.item.ItemDefinition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
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
}
