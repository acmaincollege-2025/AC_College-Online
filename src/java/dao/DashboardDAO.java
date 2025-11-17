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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.EnrollmentStats;
import model.Program;
import util.DatabaseUtil;

/**
 *
 * @author hrkas
 */
public class DashboardDAO {

    public List<EnrollmentStats> getEnrollmentStats(String year, String semester, int programId) {
        List<EnrollmentStats> statsList = new ArrayList<>();
        String sql = "SELECT p.program_name, e.semester, e.year, COUNT(e.student_id) as totalEnrolled "
                + "FROM enrollment e "
                + "JOIN program p ON e.program_id = p.program_id "
                + "WHERE (? = 0 OR p.program_id = ?) "
                + "AND (? = '' OR e.semester = ?) "
                + "AND (? = 0 OR e.year_level = ?) "
                + "GROUP BY p.program_name, e.semester, e.year_level";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, programId);
            ps.setInt(2, programId);
            ps.setString(3, semester);
            ps.setString(4, semester);
            ps.setString(5, year);
            ps.setString(6, year);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                EnrollmentStats stats = new EnrollmentStats();
                stats.setProgramName(rs.getString("program_name"));
                stats.setSemester(rs.getString("semester"));
                stats.setYear(rs.getInt("year_level"));
                stats.setTotalEnrolled(rs.getInt("totalEnrolled"));
                statsList.add(stats);
            }
        } catch (SQLException ex) {
            System.out.println("Error fetching enrollment stats: " + ex.getMessage());
        }

        return statsList;
    }

    public List<Program> getAllPrograms() {
        List<Program> programs = new ArrayList<>();
        String sql = "SELECT * FROM program ORDER BY program_name";

        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Program p = new Program();
                p.setProgram_id(rs.getInt("program_id"));
                p.setProgram_name(rs.getString("program_name"));
                programs.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Error in getAllPrograms: " + e.getMessage());
        }

        return programs;
    }

    public List<String> getAvailableYears() {
        List<String> years = new ArrayList<>();
        String sql = "SELECT DISTINCT YEAR(date_enrolled) AS year FROM enrollment ORDER BY year DESC";

        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                years.add(rs.getString("year"));
            }
        } catch (SQLException e) {
            System.out.println("Error in getAvailableYears: " + e.getMessage());
        }

        return years;
    }

    public List<EnrollmentStats> getTopCourses(String year, String semester, int programId) {
        List<EnrollmentStats> list = new ArrayList<>();

        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(
                        "SELECT c.courseTitle, COUNT(e.studNo) AS enrolledCount "
                        + "FROM enrollment e "
                        + "JOIN course c ON e.courseId = c.courseId "
                        + "WHERE e.academicYear = ? AND e.semester = ? AND e.programId = ? "
                        + "GROUP BY c.courseTitle ORDER BY enrolledCount DESC LIMIT 5")) {

            ps.setString(1, year);
            ps.setString(2, semester);
            ps.setInt(3, programId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    EnrollmentStats stat = new EnrollmentStats();
                    stat.setCourseTitle(rs.getString("courseTitle"));
                    stat.setEnrolledCount(rs.getInt("enrolledCount"));
                    list.add(stat);
                }
            }
        } catch (Exception e) {
            System.out.println("Error in Get Top Courses: " + e.getMessage());
        }

        return list;
    }

    public int getTotalApplicants() {
        String sql = "SELECT COUNT(*) FROM student";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return 0;
    }

    public int getPendingVerifications() {
        String sql = "SELECT COUNT(*) FROM student WHERE status = 'Pending'";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return 0;
    }

    public Map<String, Integer> getApplicantsByProgram() {
        Map<String, Integer> map = new HashMap<>();
        String sql = "SELECT p.program_name, COUNT(s.idNo) AS total "
                + "FROM student s "
                + "JOIN program p ON s.program = p.program_id "
                + "GROUP BY p.program_name";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("program_name"), rs.getInt("total"));
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return map;
    }
    // For Dean: Get total enrolled per program

    public Map<String, Integer> getEnrolledPerProgram() {
        Map<String, Integer> map = new LinkedHashMap<>();
        String sql = "SELECT p.program_name, COUNT(e.id) AS total "
                + "FROM enrollment e "
                + "JOIN program p ON e.program_id = p.program_id "
                + "GROUP BY p.program_name ORDER BY total DESC";

        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                map.put(rs.getString("program_name"), rs.getInt("total"));
            }

        } catch (SQLException ex) {
            ex.getMessage();
        }

        return map;
    }

    // For Dean: Gender distribution
    public Map<String, Integer> getGenderDistribution() {
        Map<String, Integer> map = new LinkedHashMap<>();
        String sql = "SELECT sex, COUNT(*) AS total FROM student GROUP BY sex";

        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                map.put(rs.getString("sex"), rs.getInt("total"));
            }

        } catch (SQLException ex) {
            ex.getMessage();
        }

        return map;
    }

    // For Cashier: Total collection
    public double getTotalCollections() {
        String sql = "SELECT SUM(amount) AS total FROM payment WHERE status = 'Paid'";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble("total");
            }

        } catch (SQLException ex) {
            ex.getMessage();
        }
        return 0.0;
    }

    // For Cashier/Admin: Payment methods summary
    public Map<String, Double> getPaymentSummaryByType() {
        Map<String, Double> map = new LinkedHashMap<>();
        String sql = "SELECT payment_method, SUM(amount) AS total FROM payment WHERE status = 'Paid' GROUP BY payment_method";

        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                map.put(rs.getString("payment_method"), rs.getDouble("total"));
            }

        } catch (SQLException ex) {
            ex.getMessage();
        }

        return map;
    }

    // For Admission: Count of enrolled students
    public int getTotalEnrolledStudents() {
        String sql = "SELECT COUNT(*) AS total FROM enrollment WHERE status = 'Approved'";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException ex) {
            ex.getMessage();
        }
        return 0;
    }

    // For Registrar: Enrollment trend per semester/year
    public Map<String, Integer> getEnrollmentByYearSem() {
        Map<String, Integer> map = new LinkedHashMap<>();
        String sql = "SELECT CONCAT(year, '-', semester) AS term, COUNT(*) AS total "
                + "FROM enrollment WHERE status = 'Approved' GROUP BY year, semester ORDER BY year DESC, semester DESC";

        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                map.put(rs.getString("term"), rs.getInt("total"));
            }

        } catch (SQLException ex) {
            ex.getMessage();
        }

        return map;
    }
//    public static int getTotalEnrolledStudents() {
//        int total = 0;
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(
//                     "SELECT COUNT(*) FROM enrollment WHERE status = 'Approved'")) {
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                total = rs.getInt(1);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return total;
//    }

    public static int getTotalPrograms() {
        int total = 0;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT COUNT(*) FROM program")) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    public static int getPendingUserCount() {
        int total = 0;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT COUNT(*) FROM users WHERE status = 'Pending'")) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }
}
