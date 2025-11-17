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
import java.util.logging.Level;
import java.util.logging.Logger;
import model.COERequest;
import util.DatabaseUtil;

/**
 *
 * @author hrkas
 */
public class COEDAO {

    private Connection conn;

    public COEDAO() {
        try {
            conn = DatabaseUtil.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(COEDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean requestCOE(String studentId) {
        String sql = "INSERT INTO coe_request (student_id) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(COEDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<COERequest> getRequestsByStudent(String studentId) {
        List<COERequest> list = new ArrayList<>();
        String sql = "SELECT * FROM coe_request WHERE student_id = ? ORDER BY request_date DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                COERequest r = new COERequest();
                r.setRequestId(rs.getInt("request_id"));
                r.setStudentId(rs.getString("student_id"));
                r.setRequestDate(rs.getTimestamp("request_date"));
                r.setStatus(rs.getString("status"));
                r.setRemarks(rs.getString("remarks"));
                list.add(r);
            }
        } catch (SQLException ex) {
            Logger.getLogger(COEDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public boolean updateStatus(int requestId, String status, String remarks) {
        String sql = "UPDATE coe_request SET status = ?, remarks = ? WHERE request_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, remarks);
            ps.setInt(3, requestId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(COEDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}

