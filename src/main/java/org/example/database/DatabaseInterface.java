package org.example.database;

import org.example.user.User;

import java.sql.SQLException;

public interface DatabaseInterface {
    void addUser(User user) throws SQLException;
}
