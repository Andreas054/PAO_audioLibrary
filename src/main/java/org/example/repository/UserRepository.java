package org.example.repository;

import org.example.database.MySQLDatabase;
import org.example.user.User;
import org.example.user.UserTypeEnum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository extends Repository {
    private static UserRepository instance = null;

    private UserRepository(MySQLDatabase mySqlDatabase) {
        super(mySqlDatabase);
    }

    public static UserRepository getInstance(MySQLDatabase mySqlDatabase) {
        if (instance == null) {
            instance = new UserRepository(mySqlDatabase);
        }
        return instance;
    }

    public boolean addUser(User user) {
        String sql = String.format("INSERT INTO users (name, password, user_type) VALUES ('%s', '%s', '%s')", user.getName(), user.getPassword(), user.getUserTypeEnum().name());
        mySQLDatabase.insertSql(sql);
        return true;
    }

    public boolean promoteUser(User user) {
        String sql = String.format("UPDATE users SET user_type = '%s' WHERE name = '%s'", UserTypeEnum.ADMIN, user.getName());
        mySQLDatabase.insertSql(sql);
        return true;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = this.mySQLDatabase.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String password = resultSet.getString("password");
                UserTypeEnum userTypeEnum = UserTypeEnum.valueOf(resultSet.getString("user_type"));
                users.add(new User(id, name, password, userTypeEnum));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }

    public User getUserByName(String name) {
        String sql = String.format("SELECT * FROM users WHERE name = '%s'", name);
        try (Connection conn = this.mySQLDatabase.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String password = resultSet.getString("password");
                UserTypeEnum userTypeEnum = UserTypeEnum.valueOf(resultSet.getString("user_type"));
                return new User(id, name, password, userTypeEnum);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
