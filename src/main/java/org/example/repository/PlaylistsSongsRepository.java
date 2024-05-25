package org.example.repository;

import org.example.database.MySQLDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class PlaylistsSongsRepository extends Repository {
    private static PlaylistsSongsRepository instance;

    private PlaylistsSongsRepository() {
        super(MySQLDatabase.getInstance());
    }

    public static PlaylistsSongsRepository getInstance() {
        if (instance == null) {
            instance = new PlaylistsSongsRepository();
        }
        return instance;
    }

    public boolean addSongToPlaylist(String songUuid, String playlistUuid) {
        String sql = String.format("INSERT INTO playlists_songs (playlist_uuid, song_uuid) VALUES ('%s', '%s')", playlistUuid, songUuid);
        mySQLDatabase.insertSql(sql);
        return true;
    }

    public List<String> getSongsForPlaylist(String playlistUuid) {
        List<String> songUuids = new LinkedList<>();
        String sql = String.format("SELECT song_uuid FROM playlists_songs WHERE playlist_uuid = '%s'", playlistUuid);
        try (Connection conn = this.mySQLDatabase.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String title = resultSet.getString("song_uuid");
                songUuids.add(title);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return songUuids;
    }
}
