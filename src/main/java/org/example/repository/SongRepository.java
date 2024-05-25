package org.example.repository;

import org.example.database.MySQLDatabase;
import org.example.music.Song;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SongRepository extends Repository {
    private static SongRepository instance;

    private SongRepository() {
        super(MySQLDatabase.getInstance());
    }

    public static SongRepository getInstance() {
        if (instance == null) {
            instance = new SongRepository();
        }
        return instance;
    }

    @Override
    public <T> int getNrPagini(String tableName, String column, T id) {
        int nrCount = 0;
        String value = id.toString().substring(1, id.toString().length() - 1);
        String sql = String.format("SELECT COUNT(*) FROM %s WHERE %s LIKE '%s%%'", tableName, column.toUpperCase(), value.toUpperCase());
        try (Connection conn = this.mySQLDatabase.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    nrCount = resultSet.getInt(1);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return (int) Math.ceil((double) nrCount / MySQLDatabase.nrElementePagina);
    }

    public boolean createSong(Song song) {
        String sql = String.format("INSERT INTO songs (uuid, title, artist, year) VALUES ((SELECT UUID()), '%s', '%s', '%s')", song.getTitle(), song.getArtist(), song.getYear());
        mySQLDatabase.insertSql(sql);
        return true;
    }

    public List<Song> getAllSongs() {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT * FROM songs";
        try (Connection conn = this.mySQLDatabase.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String artist = resultSet.getString("artist");
                String year = resultSet.getString("year");
                songs.add(new Song(title, artist, year));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return songs;
    }

    public Song getSongById(String songUuid) {
        String sql = String.format("SELECT * FROM songs WHERE uuid = '%s'", songUuid);
        try (Connection conn = this.mySQLDatabase.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                String title = resultSet.getString("title");
                String artist = resultSet.getString("artist");
                String year = resultSet.getString("year");
                return new Song(uuid, title, artist, year);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Song> getSongsByCriteria(String criteria, String value, int paginaCurenta) {
        paginaCurenta = paginaCurenta * MySQLDatabase.nrElementePagina;

        List<Song> songs = new ArrayList<>();
        String sql = String.format("SELECT * FROM songs WHERE %s LIKE '%s%%' LIMIT %d OFFSET %d", criteria.toUpperCase(), value.toUpperCase(), MySQLDatabase.nrElementePagina, paginaCurenta);
        try (Connection conn = this.mySQLDatabase.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                String title = resultSet.getString("title");
                String artist = resultSet.getString("artist");
                String year = resultSet.getString("year");
                songs.add(new Song(uuid, title, artist, year));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return songs;
    }
}
