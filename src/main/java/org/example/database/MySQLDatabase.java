package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLDatabase implements DatabaseInterface {
    private static MySQLDatabase instance = null;
    private final String url;
    private final String user;
    private final String password;

    public static int nrElementePagina = 5;

    private MySQLDatabase()  {
//        url = "jdbc:mysql://16.171.166.64:3306/pao";
        url = "jdbc:mysql://localhost:3306/pao";
        user = "admin";
        password = "admin";
    }

    public static MySQLDatabase getInstance() {
        if (instance == null) {
            instance = new MySQLDatabase();
        }
        return instance;
    }

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Create tables
     */
    public void createTables() {
        String sql = """
                CREATE TABLE IF NOT EXISTS users (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                password VARCHAR(255) NOT NULL,
                user_type ENUM('ADMIN', 'USER')
                );""";

        try (Connection conn = connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        sql = """
                CREATE TABLE IF NOT EXISTS songs (
                uuid VARCHAR(36) PRIMARY KEY,
                title VARCHAR(255) NOT NULL,
                artist VARCHAR(255) NOT NULL,
                year INT NOT NULL
                );""";

        try (Connection conn = connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        sql = """
                CREATE TABLE IF NOT EXISTS playlists (
                uuid VARCHAR(36) PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                user_id INT
                );""";

        try (Connection conn = connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        sql = """
                CREATE TABLE IF NOT EXISTS playlists_songs (
                playlist_uuid VARCHAR(36),
                song_uuid VARCHAR(36),
                PRIMARY KEY (playlist_uuid, song_uuid)\s
                );""";

        try (Connection conn = connect();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        sql = """
                CREATE TABLE IF NOT EXISTS audit (
                id INT AUTO_INCREMENT PRIMARY KEY,
                user_id INT,
                command VARCHAR(500),
                rulat_ok BOOL
                );""";

        try (Connection conn = connect();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
    }

    @Override
    public void insertSql(String sql) {
        try (Connection conn = connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}