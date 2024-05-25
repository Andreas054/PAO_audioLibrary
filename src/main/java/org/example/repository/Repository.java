package org.example.repository;

import org.example.database.MySQLDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Repository {
    protected MySQLDatabase mySQLDatabase;
    
    public Repository(MySQLDatabase mySQLDatabase) {
        this.mySQLDatabase = mySQLDatabase;
    }

    public int getNrPagini(String tableName) {
        int nrCount = 0;
        String sql = "SELECT COUNT(*) FROM ?";
        try (Connection conn = this.mySQLDatabase.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, tableName);
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

    public <T> int getNrPagini(String tableName, String column, T id) {
        int nrCount = 0;
        String sql = String.format("SELECT COUNT(*) FROM %s WHERE %s = %s", tableName, column, id);
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
}
