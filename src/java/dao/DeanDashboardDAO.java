/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.EnrollmentStats;
import util.DatabaseUtil;

/**
 *
 * @author hrkas
 */
public class DeanDashboardDAO {

    public DeanDashboardDAO() {
    }

    public List<EnrollmentStats> getTopEnrolledCourses(int limit) {
        List<EnrollmentStats> list = new ArrayList<>();
        String sql = "SELECT c.course_title, COUNT(e.student_id) AS enrolledCount "
                + "FROM enrollment e "
                + "JOIN course c ON e.course_id = c.course_id "
                + "GROUP BY c.course_title "
                + "ORDER BY enrolledCount DESC "
                + "LIMIT ?";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                EnrollmentStats stat = new EnrollmentStats();
                stat.setCourseTitle(rs.getString("course_title"));
                stat.setEnrolledCount(rs.getInt("enrolledCount"));
                list.add(stat);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return list;
    }

    public Map<String, Integer> getGenderDistribution() {
        Map<String, Integer> map = new HashMap<>();
        String sql = "SELECT sex, COUNT(*) AS count FROM student GROUP BY sex";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String gender = rs.getString("sex");
                int count = rs.getInt("count");
                map.put(gender, count);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return map;
    }

    public Map<String, Integer> getDepartmentStats() {
        Map<String, Integer> map = new HashMap<>();
        String sql = "SELECT d.dept_name, COUNT(s.idNo) AS studentCount "
                + "FROM student s "
                + "JOIN program p ON s.program = p.program_id "
                + "JOIN department d ON p.department_id = d.department_id "
                + "GROUP BY d.dept_name";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("dept_name"), rs.getInt("studentCount"));
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return map;
    }

    public Map<String, Integer> getProgramStats() {
        Map<String, Integer> map = new HashMap<>();
        String sql = "SELECT p.program_name, COUNT(s.idNo) AS studentCount "
                + "FROM student s "
                + "JOIN program p ON s.program = p.program_id "
                + "GROUP BY p.program_name";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("program_name"), rs.getInt("studentCount"));
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return map;
    }

    public Map<String, Integer> getYearStats() {
        Map<String, Integer> map = new HashMap<>();
        String sql = "SELECT acad_year, COUNT(*) AS count FROM student GROUP BY acad_year";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("acad_year"), rs.getInt("count"));
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return map;
    }

}
