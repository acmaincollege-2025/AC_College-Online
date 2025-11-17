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
import model.EnrollmentPeriod;
import util.DatabaseUtil;

/**
 *
 * @author hrkas
 */
public class EnrollmentPeriodDAO {

    public boolean openEnrollment(String year, String sem) {
        String sql = "INSERT INTO enrollment_period (academic_year, semester, is_open) VALUES (?, ?, TRUE)";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, year);
            ps.setString(2, sem);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return false;
    }

    public boolean closeEnrollment(int id) {
        String sql = "UPDATE enrollment_period SET is_open = FALSE WHERE id = ?";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return false;
    }

    public EnrollmentPeriod getCurrentPeriod() {
        String sql = "SELECT * FROM enrollment_period WHERE is_open = TRUE ORDER BY id DESC LIMIT 1";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                EnrollmentPeriod ep = new EnrollmentPeriod();
                ep.setId(rs.getInt("id"));
                ep.setAcademicYear(rs.getString("academic_year"));
                ep.setSemester(rs.getString("semester"));
                ep.setIsOpen(rs.getBoolean("is_open"));
                return ep;
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return null;
    }

}
