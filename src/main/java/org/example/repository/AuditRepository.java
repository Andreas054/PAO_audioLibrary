package org.example.repository;

import org.example.audit.Audit;
import org.example.database.MySQLDatabase;
import org.example.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class AuditRepository extends Repository {
    private static AuditRepository instance;

    private AuditRepository() {
        super(MySQLDatabase.getInstance());
    }

    public static AuditRepository getInstance() {
        if (instance == null) {
            instance = new AuditRepository();
        }
        return instance;
    }

    public boolean addAudit(Audit audit) {
        String sql = String.format("INSERT INTO audit (user_id, command, rulat_ok) VALUES ('%s', '%s', %b)", audit.getUserId(), audit.getCommand(), audit.isRulatOk());
        mySQLDatabase.insertSql(sql);
        return true;
    }

    public Audit getAuditById(int id) {
        String sql = "SELECT * FROM audit WHERE id = ?";
        try (Connection conn = this.mySQLDatabase.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int userId = resultSet.getInt("user_id");
                    String command = resultSet.getString("command");
                    boolean rulatOk = resultSet.getBoolean("rulat_ok");
                    return new Audit(id, userId, command, rulatOk);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Audit> getAuditsForUser(User user, int paginaCurenta) {
        paginaCurenta = paginaCurenta * MySQLDatabase.nrElementePagina;

        List<Audit> audits = new LinkedList<>();
        String sql = "SELECT * FROM audit WHERE user_id = ? LIMIT ? OFFSET ?";
        try (Connection conn = this.mySQLDatabase.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setInt(2, MySQLDatabase.nrElementePagina);
            preparedStatement.setInt(3, paginaCurenta);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int userId = resultSet.getInt("user_id");
                    String command = resultSet.getString("command");
                    boolean rulatOk = resultSet.getBoolean("rulat_ok");
                    audits.add(new Audit(id, userId, command, rulatOk));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return audits;
    }
}
