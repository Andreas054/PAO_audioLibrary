package org.example.repository;

import org.example.database.MySQLDatabase;
import org.example.music.Playlist;
import org.example.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class PlaylistRepository extends Repository {
    private static PlaylistRepository instance;

    private PlaylistRepository() {
        super(MySQLDatabase.getInstance());
    }

    public static PlaylistRepository getInstance() {
        if (instance == null) {
            instance = new PlaylistRepository();
        }
        return instance;
    }

    public boolean addPlaylist(Playlist playlist) {
        String uuidTmp = UUID.randomUUID().toString();
        String sql = String.format("INSERT INTO playlists (uuid, name, user_id) VALUES ('%s', '%s', %d)", uuidTmp, playlist.getName(), User.currentUser.getId());
        mySQLDatabase.insertSql(sql);
        return true;
    }

    public List<Playlist> getAllPlaylistsForUser(User user, int paginaCurenta) {
        paginaCurenta = paginaCurenta * MySQLDatabase.nrElementePagina;

        List<Playlist> playlists = new LinkedList<>();
        String sql = "SELECT uuid, name FROM playlists WHERE user_id = ? LIMIT ? OFFSET ?";
        try (Connection conn = this.mySQLDatabase.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setInt(2, MySQLDatabase.nrElementePagina);
            preparedStatement.setInt(3, paginaCurenta);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                    String name = resultSet.getString("name");
                    playlists.add(new Playlist(uuid, name, user.getId()));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return playlists;
    }

    public Playlist getPlaylistById(String playlistUuid) {
        String sql = String.format("SELECT * FROM playlists WHERE uuid = '%s'", playlistUuid);
        try (Connection conn = this.mySQLDatabase.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int userId = resultSet.getInt("user_id");
                return new Playlist(UUID.fromString(playlistUuid), name, userId);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Playlist getPlaylistByName(String playlistName, User user) {
        String sql = String.format("SELECT * FROM playlists WHERE name = '%s' AND user_id = %d", playlistName, user.getId());
        try (Connection conn = this.mySQLDatabase.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String uuid = resultSet.getString("uuid");
                return new Playlist(UUID.fromString(uuid), playlistName, user.getId());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
