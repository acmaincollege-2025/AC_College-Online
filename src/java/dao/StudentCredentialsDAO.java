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
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.StudentCredential;
import org.mindrot.jbcrypt.BCrypt;
import util.DatabaseUtil;

/**
 *
 * @author hrkas
 */
public class StudentCredentialsDAO {

    private Connection con;

    public StudentCredentialsDAO() {
        try {
            con = DatabaseUtil.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(StudentCredentialsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public StudentCredentialsDAO(Connection con) {
        this.con = con;
    }

    public StudentCredential findByStudentNo(String studentNo) {
        StudentCredential cred = null;
        try {
            String sql = "SELECT * FROM student_credentials WHERE student_no = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, studentNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                cred = new StudentCredential();
                cred.setId(rs.getInt("id"));
                cred.setStudentNo(rs.getString("student_no"));
                cred.setPassword(rs.getString("password"));
                cred.setRole(rs.getString("role"));
                cred.setStatus(rs.getString("status"));
                cred.setLastLogin(rs.getTimestamp("last_login"));
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return cred;
    }

    public boolean updateLoginTime(String studentNo) {
        try {
            String sql = "UPDATE student_credentials SET last_login = NOW() WHERE student_no = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, studentNo);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return false;
    }

    public boolean save(StudentCredential cred) {
        try {
            String sql = "INSERT INTO student_credentials (student_no, password, role, status) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, cred.getStudentNo());
            ps.setString(2, BCrypt.hashpw(cred.getPassword(), BCrypt.gensalt()));
            ps.setString(3, cred.getRole());
            ps.setString(4, cred.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return false;
    }

    public boolean changePassword(String studentNo, String newPassword) {
        try {
            String sql = "UPDATE student_credentials SET password = ? WHERE student_no = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, BCrypt.hashpw(newPassword, BCrypt.gensalt()));
            ps.setString(2, studentNo);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return false;
    }

    public StudentCredential authenticate(String studentNo, String password) {
        StudentCredential sc = null;
        try (Connection con = DatabaseUtil.getConnection()) {
            String sql = "SELECT * FROM student_credentials WHERE student_no = ? AND password = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, studentNo);
            ps.setString(2, BCrypt.hashpw(password, BCrypt.gensalt()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                sc = new StudentCredential();
                sc.setId(rs.getInt("id"));
                sc.setStudentNo(rs.getString("student_no"));
                sc.setPassword(rs.getString("password"));
                sc.setStatus(rs.getString("status"));
                sc.setLastLogin(rs.getTimestamp("last_login"));
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return sc;
    }

    public boolean isStudentRegistered(String studentNo) {
        try (Connection con = DatabaseUtil.getConnection()) {
            String sql = "SELECT 1 FROM student_credentials WHERE student_no = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, studentNo);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }
    }

    public boolean registerStudentCredentials(String studentNo, String password) {
        try (Connection con = DatabaseUtil.getConnection()) {
            String sql = "INSERT INTO student_credentials (student_no, password, role, status) VALUES (?, ?, 'student', 'active')";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, studentNo);
            ps.setString(2, BCrypt.hashpw(password, BCrypt.gensalt())); // optionally hash the password here
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }
    }

    public LocalDate getLastDpaAckDate(String studentNo) {
        String sql = "SELECT last_dpa_ack_date FROM student_credentials WHERE student_no = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getDate("last_dpa_ack_date") != null) {
                return rs.getDate("last_dpa_ack_date").toLocalDate();
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }

    public void updateLastDpaAckDate(String studentNo) {
        String sql = "UPDATE student_credentials SET last_dpa_ack_date = CURDATE() WHERE student_no = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentNo);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

}
