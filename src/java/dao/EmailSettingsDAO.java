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
import java.util.logging.Level;
import java.util.logging.Logger;
import model.EmailSettings;
import util.AESUtil;
import util.DatabaseUtil;

/**
 *
 * @author hrkas
 */
public class EmailSettingsDAO {

    private Connection conn;

    public EmailSettingsDAO() {
        try {
            this.conn = DatabaseUtil.getConnection(); // optional default connection
        } catch (SQLException ex) {
            Logger.getLogger(EmailSettingsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public EmailSettingsDAO(Connection conn) {
        this.conn = conn;
    }

    public EmailSettings loadSettings() {
        String sql = "SELECT sender_email, sender_password, smtp_host, smtp_port FROM email_settings";
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                EmailSettings settings = new EmailSettings();
                settings.setSenderEmail(rs.getString("sender_email"));
                settings.setSenderPassword(AESUtil.decrypt(rs.getString("sender_password")));
                settings.setSmtpHost(rs.getString("smtp_host"));
                settings.setSmtpPort(rs.getString("smtp_port"));
                return settings;
            }
        } catch (SQLException ex) {
            System.out.println("Error loading email settings: " + ex);
        }
        return new EmailSettings(); // Return empty but non-null to prevent issues
    }

    public void saveSettings(EmailSettings settings) {
        String sql = "UPDATE email_settings SET sender_email = ?, sender_password = ?, smtp_host = ?, smtp_port = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, settings.getSenderEmail());
            ps.setString(2, AESUtil.encrypt(settings.getSenderPassword()));
            ps.setString(3, settings.getSmtpHost());
            ps.setString(4, settings.getSmtpPort());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error saving email settings: " + ex);
        }
    }
}
