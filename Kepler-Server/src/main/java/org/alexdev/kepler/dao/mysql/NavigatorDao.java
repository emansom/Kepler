package org.alexdev.kepler.dao.mysql;

import org.alexdev.kepler.dao.Storage;
import org.alexdev.kepler.game.navigator.NavigatorCategory;
import org.alexdev.kepler.game.room.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NavigatorDao {

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
}
