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
import model.CourseProgress;
import util.DatabaseUtil;

/**
 *
 * @author hrkas
 */
public class CourseProgressDAO {

    public List<CourseProgress> getProgressByStudentId(String studentId) {
        List<CourseProgress> progressList = new ArrayList<>();

        String query = "SELECT cp.course_id, c.course_code, c.course_title, c.units, cp.status "
                + "FROM course_progress cp "
                + "JOIN course c ON cp.course_id = c.course_id "
                + "WHERE cp.student_id = ?";

        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CourseProgress cp = new CourseProgress();
                    cp.setCourseId(rs.getInt("course_id"));
                    cp.setCourseCode(rs.getString("course_code"));
                    cp.setCourseTitle(rs.getString("course_title"));
                    cp.setUnits(rs.getDouble("units"));
                    cp.setStatus(rs.getString("status")); // e.g., "Completed", "In Progress"

                    progressList.add(cp);
                }
            }

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage()); // Replace with proper logging if needed
        }

        return progressList;
    }

    public void syncProgressFromEnrollment(String studentId, List<Integer> enrolledCourseIds) {
        String insertSQL = "INSERT IGNORE INTO course_progress (student_id, course_id, status) VALUES (?, ?, 'Enrolled')";

        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(insertSQL)) {

            for (Integer courseId : enrolledCourseIds) {
                ps.setString(1, studentId);
                ps.setInt(2, courseId);
                ps.addBatch();
            }

            ps.executeBatch();

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
    }

   public boolean updateCourseProgressStatus(String studno, String courseId, String newStatus) {
        String sql = "UPDATE course_progress SET status = ? WHERE studno = ? AND course_code = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setString(2, studno);
            ps.setString(3, courseId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException ex) {
            System.out.println("Error updating course progress status: " + ex.getMessage());
            return false;
        }
    }

}
