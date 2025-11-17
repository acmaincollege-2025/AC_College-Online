/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.AuditLog;
import util.DatabaseUtil;

/**
 *
 * @author hrkas
 */
public class AuditLogDAO {

    public void log(String username, String action, String description) {
        String sql = "INSERT INTO audit_log (username, action, description) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, action);
            stmt.setString(3, description);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.getMessage();
        }
    }

    public List<AuditLog> getAllLogs() {
        List<AuditLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM audit_log ORDER BY timestamp DESC";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                AuditLog log = new AuditLog();
                log.setId(rs.getInt("id"));
                log.setUsername(rs.getString("username"));
                log.setAction(rs.getString("action"));
                log.setDescription(rs.getString("description"));
                log.setTimestamp(rs.getTimestamp("timestamp"));
                logs.add(log);
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return logs;
    }
}
