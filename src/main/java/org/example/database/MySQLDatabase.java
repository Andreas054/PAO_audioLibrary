package org.example.database;

import org.example.user.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLDatabase implements DatabaseInterface {
    private String url;
    private String user;
    private String password;

    public MySQLDatabase() throws IOException {
        loadProperties();
    }

    private void loadProperties() throws IOException {
        url = "jdbc:mysql://16.171.166.64:3306/pao";
        user = "admin";
        password = "admin";
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
    
    private void createTables() {
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                + "id INT AUTO_INCREMENT PRIMARY KEY,\n"
                + "name VARCHAR(255) NOT NULL,\n"
                + "password VARCHAR(255) NOT NULL\n"
                + "user_type ENUM('ADMIN', 'USER')\n"
                + ");";

        try (Connection conn = connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO users (name, password, user_type) VALUES (?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getUserTypeEnum().name());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }
}