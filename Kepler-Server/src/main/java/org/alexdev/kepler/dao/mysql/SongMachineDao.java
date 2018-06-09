package org.alexdev.kepler.dao.mysql;

import org.alexdev.kepler.dao.Storage;
import org.alexdev.kepler.game.item.ItemManager;
import org.alexdev.kepler.game.item.base.ItemDefinition;
import org.alexdev.kepler.game.song.Song;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static Map<Integer, Integer> getTracks(int soundMachineId) {
        Map<Integer, Integer> tracks = new HashMap<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT track_id, slot_id FROM soundmachine_tracks WHERE soundmachine_id = ?", sqlConnection);
            preparedStatement.setInt(1, soundMachineId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                tracks.put(resultSet.getInt("slot_id"), resultSet.getInt("track_id"));
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return tracks;
    }

    public static void addTrack(int soundMachineId, int trackId, int slotId) throws SQLException {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("INSERT INTO soundmachine_tracks (soundmachine_id, track_id, slot_id) VALUES (?, ?, ?)", sqlConnection);
            preparedStatement.setInt(1, soundMachineId);
            preparedStatement.setInt(2, trackId);
            preparedStatement.setInt(3, slotId);
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
            throw e;
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }
}
