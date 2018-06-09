package org.alexdev.kepler.dao.mysql;

import org.alexdev.kepler.dao.Storage;
import org.alexdev.kepler.game.song.Song;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SongMachineDao {

    /**
     * Get the song list for this machine.
     *
     * @param itemId the item id for this machine
     * @return the list of songs
     */
    public static List<Song> getSongList(int itemId) {
        List<Song> songs = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM soundmachine_songs WHERE item_id = ?", sqlConnection);
            preparedStatement.setInt(1, itemId);
            resultSet = preparedStatement.executeQuery();

            // (int id, String title, int itemId, int length, String data, boolean isBurnt)
            while (resultSet.next()) {
                songs.add(new Song(resultSet.getInt("id"), resultSet.getString("title"), itemId,
                        resultSet.getInt("length"), resultSet.getString("data"), resultSet.getBoolean("bunt")));
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return songs;
    }
}
