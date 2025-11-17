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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Course;
import model.Enrollment;
import model.EnrollmentRecord;
import model.Program;
import model.Student;
import util.DatabaseUtil;

/**
 *
 * @author hrkas
 */
public class EnrollmentDAO {

    public boolean createEnrollment(Enrollment enrollment) {
        String sql = "INSERT INTO enrollment (studno, academic_year, semester, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, enrollment.getStudno());
            ps.setString(2, enrollment.getAcademic_year());
            ps.setString(3, enrollment.getSemester());
            ps.setString(4, "Pending");

            int rows = ps.executeUpdate();

            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int enrollmentId = rs.getInt(1);
                    enrollment.setEnrollment_id(enrollmentId);

                    // Optionally auto-enlist courses for REGULAR students
                    enrollDefaultCourses(enrollment);
                }
                return true;
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return false;
    }

    private void enrollDefaultCourses(Enrollment enrollment) {
        String sql = "INSERT INTO enrollment_course (enrollment_id, course_id)\n"
                + " SELECT ?, course_id FROM course\n"
                + " WHERE program_id = (SELECT program_id FROM student WHERE sid = ?)\n"
                + "   AND semester = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, enrollment.getEnrollment_id());
            ps.setString(2, enrollment.getStudno());
            ps.setString(3, enrollment.getSemester());

            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.getMessage();
        }
    }

    public List<Enrollment> listAllEnrollments() {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT e.enrollment_id, s.studno, s.firstname, s.lastname, e.academic_year, e.semester, e.status "
                + "FROM enrollment e JOIN student s ON e.studno = s.studno "
                + "ORDER BY e.date_enrolled DESC";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Enrollment e = new Enrollment();
                e.setEnrollment_id(rs.getInt("enrollment_id"));
                e.setStudno(rs.getString("studno"));
                e.setAcademic_year(rs.getString("academic_year"));
                e.setSemester(rs.getString("semester"));
                e.setStatus(rs.getString("status"));

                // You can also set student name for UI display
                Student s = new Student();
                s.setStudno(rs.getString("studno"));
                s.setFirstname(rs.getString("firstname"));
                s.setLastname(rs.getString("lastname"));
                e.setStudent(s);

                list.add(e);
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return list;

    }

    public List<Course> getEnrolledCourses(int enrollmentId) {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT c.course_code, c.course_title, c.units, c.semester, c.year_level "
                + "FROM enrollment_course ec "
                + "JOIN course c ON ec.course_id = c.course_id "
                + "WHERE ec.enrollment_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, enrollmentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Course c = new Course();
                    c.setCourseCode(rs.getString("course_code"));
                    c.setCourseTitle(rs.getString("course_title"));
                    c.setUnits(rs.getDouble("units"));
                    c.setSemester(rs.getString("semester"));
                    c.setYearLevel(rs.getInt("year_level"));
                    list.add(c);
                }
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return list;

    }

    public boolean updateStatus(int enrollment_id, String newStatus) {
        String sql = "UPDATE enrollment SET status = ? WHERE enrollment_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setInt(2, enrollment_id);

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.getMessage();
        }
        return false;

    }

    public List<Enrollment> enrollmentList() {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT e.*, s.lastname, s.firstname FROM enrollment e "
                + "JOIN student s ON e.student_id = s.sid ORDER BY e.date_enrolled DESC";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Enrollment e = new Enrollment();
                e.setEnrollment_id(rs.getInt("enrollment_id"));
                e.setAcademic_year(rs.getString("academic_year"));
                e.setSemester(rs.getString("semester"));
                e.setStatus(rs.getString("status"));
                e.setDate_enrolled(rs.getString("date_enrolled"));

                Student s = new Student();
                s.setSid(rs.getInt("student_id"));
                s.setFirstname(rs.getString("firstname"));
                s.setLastname(rs.getString("lastname"));
                e.setStudent(s);

                list.add(e);
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return list;

    }

    public List<Enrollment> listEnrollmentsByStudno(String studno) {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT e.*, s.lastname, s.firstname FROM enrollment e "
                + "JOIN student s ON e.student_id = s.sid WHERE s.studno = ? "
                + "ORDER BY e.date_enrolled DESC";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studno);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Enrollment e = new Enrollment();
                    e.setEnrollment_id(rs.getInt("enrollment_id"));
                    e.setAcademic_year(rs.getString("academic_year"));
                    e.setSemester(rs.getString("semester"));
                    e.setStatus(rs.getString("status"));
                    e.setDate_enrolled(rs.getString("date_enrolled"));

                    Student s = new Student();
                    s.setSid(rs.getInt("student_id"));
                    s.setFirstname(rs.getString("firstname"));
                    s.setLastname(rs.getString("lastname"));
                    e.setStudent(s);

                    list.add(e);
                }
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return list;

    }
    public static List<EnrollmentRecord> getEnrollmentList(String programId, String year, String semester) {
    List<EnrollmentRecord> list = new ArrayList<>();
    String sql = "SELECT s.student_number, s.first_name, s.last_name, s.middle_name, p.program_name " +
                 "FROM enrollment e " +
                 "JOIN student s ON e.student_id = s.studNo " +
                 "JOIN program p ON s.program_id = p.programId " +
                 "WHERE e.year = ? AND e.semester = ? AND p.programId = ?";

    try (Connection con = DatabaseUtil.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, year);
        ps.setString(2, semester);
        ps.setString(3, programId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            EnrollmentRecord rec = new EnrollmentRecord();
            rec.setStudent(new Student(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
            rec.setProgram(new Program(rs.getString(5)));
//            rec.setStudentNumber(rs.getString("student_number"));
//            rec.setFirstName(rs.getString("first_name"));
//            rec.setLastName(rs.getString("last_name"));
//            rec.setMiddleName(rs.getString("middle_name"));
//            rec.setProgramName(rs.getString("program_name"));
            list.add(rec);
        }
    } catch (Exception ex) {
        ex.getMessage();
    }
    return list;
}

}
