package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLDatabase implements DatabaseInterface {
    private static MySQLDatabase instance = null;
    private String url;
    private String user;
    private String password;

    public static int nrElementePagina = 5;

    private MySQLDatabase()  {
        url = "jdbc:mysql://16.171.166.64:3306/pao";
//        url = "jdbc:mysql://localhost:3306/pao";
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
    
    public void createTables() {
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                + "id INT AUTO_INCREMENT PRIMARY KEY,\n"
                + "name VARCHAR(255) NOT NULL,\n"
                + "password VARCHAR(255) NOT NULL,\n"
                + "user_type ENUM('ADMIN', 'USER')\n"
                + ");";

        try (Connection conn = connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        sql = "CREATE TABLE IF NOT EXISTS songs (\n"
                + "uuid VARCHAR(36) PRIMARY KEY,\n"
                + "title VARCHAR(255) NOT NULL,\n"
                + "artist VARCHAR(255) NOT NULL,\n"
                + "year INT NOT NULL\n"
                + ");";

        try (Connection conn = connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        sql = "CREATE TABLE IF NOT EXISTS playlists (\n"
                + "uuid VARCHAR(36) PRIMARY KEY,\n"
                + "name VARCHAR(255) NOT NULL,\n"
                + "user_id INT\n"
                + ");";

        try (Connection conn = connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        sql = "CREATE TABLE IF NOT EXISTS playlists_songs (\n"
                + "playlist_uuid VARCHAR(36),\n"
                + "song_uuid VARCHAR(36),\n"
                + "PRIMARY KEY (playlist_uuid, song_uuid) \n"
                + ");";

        try (Connection conn = connect();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        sql = "CREATE TABLE IF NOT EXISTS audit (\n"
                + "id INT AUTO_INCREMENT PRIMARY KEY,\n"
                + "user_id INT,\n"
                + "command VARCHAR(500),\n"
                + "rulat_ok BOOL\n"
                + ");";

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