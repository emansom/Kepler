package org.alexdev.kepler.dao.mysql;

import org.alexdev.kepler.dao.Storage;
import org.alexdev.kepler.game.room.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SettingsDao {

    /**
     * Update setting in the database.
     *
     * @param key the key used to set the value
     * @param value the new value to set
     */
    public static void updateSetting(String key, String value) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("UPDATE settings SET value = ? WHERE setting = ?", sqlConnection);
            preparedStatement.setString(1, value);
            preparedStatement.setString(2, key);
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    /**
     * Get a value from the settings table.
     *
     * @param key the key used to get the value
     * @return the value, null if no value was found
     */
    public static String getSetting(String key) {
        String value = null;

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT value FROM settings WHERE setting = ?", sqlConnection);
            preparedStatement.setString(1, key);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                value = resultSet.getString("value");
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return value;
    }

    /**
     * Create a new setting entry in the database using a key value lookup.
     *
     * @param key the key used for the lookup
     * @param value the value for the key
     */
    public static void newSetting(String key, String value) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("INSERT INTO settings (setting, value) VALUES (?, ?)", sqlConnection);
            preparedStatement.setString(1, key);
            preparedStatement.setString(2, value);
            preparedStatement.execute();

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }
}
