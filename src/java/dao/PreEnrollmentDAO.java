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
import java.time.format.DateTimeFormatter;
import model.PreEnrollmentRequest;
import util.DatabaseUtil;

/**
 *
 * @author hrkas
 */
public class PreEnrollmentDAO {

    public boolean submitRequest(PreEnrollmentRequest req) {
        LocalDate currentDate = LocalDate.now();
        String sql = "INSERT INTO pre_enrollment_requests (studno, academic_year, semester, request_date) VALUES (?, ?, ?)";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String request_date = currentDate.format(formatter);
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, req.getStudno());
            ps.setString(2, req.getAcademic_year());
            ps.setString(3, req.getSemester());
            ps.setString(4, request_date);

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }
    }

    public PreEnrollmentRequest getLatestRequestByStudent(String studno) {
        String sql = "SELECT * FROM pre_enrollment_requests WHERE studno = ? ORDER BY request_date DESC LIMIT 1";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studno);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                PreEnrollmentRequest req = new PreEnrollmentRequest();
                req.setId(rs.getInt("id"));
                req.setStudno(rs.getString("studno"));
                req.setAcademic_year(rs.getString("academic_year"));
                req.setSemester(rs.getString("semester"));
                req.setRequest_date(rs.getString("request_date"));
                req.setStatus(rs.getString("status"));
                req.setRemarks(rs.getString("remarks"));
                return req;
            }

        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }

}
