package org.example.database;

import java.sql.SQLException;

public interface DatabaseInterface {
    /**
     * Insert the given SQL string into the database.
     * @param sql the SQL string to insert
     */
    void insertSql(String sql) throws SQLException;
}
