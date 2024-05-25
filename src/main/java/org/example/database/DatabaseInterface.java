package org.example.database;

import java.sql.SQLException;

public interface DatabaseInterface {
    void insertSql(String sql) throws SQLException;
}
