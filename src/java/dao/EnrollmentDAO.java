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
import model.CourseProgress;
import model.EnrolledCourse;
import model.Enrollment;
import model.EnrollmentCourse;
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
        String sql = "INSERT INTO enrollment (student_id, academic_year, semester, status, year_level) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, enrollment.getStudent().getSid());
            ps.setString(2, enrollment.getAcademic_year());
            ps.setString(3, enrollment.getSemester());
            ps.setString(4, "Pending");
            ps.setInt(5, enrollment.getYearLevel());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int enrollmentId = rs.getInt(1);
                    enrollment.setEnrollment_id(enrollmentId);

                    // Auto-enlist default courses
                    enrollDefaultCourses(enrollment);
                }
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("Exception: " + ex); // Replace ex.getMessage() for proper logging
        }
        return false;
    }

    private void enrollDefaultCourses(Enrollment enrollment) {
        String sql = "INSERT INTO enrollment_course (enrollment_id, course_id) "
                + "SELECT ?, course_id FROM course "
                + "WHERE program_id = (SELECT program_id FROM student WHERE sid = ?) "
                + "AND semester = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, enrollment.getEnrollment_id());
            ps.setInt(2, enrollment.getStudent().getSid());
            ps.setString(3, enrollment.getSemester());

            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Exception: " + ex);
        }
    }

    public List<Enrollment> listAllEnrollments() {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT e.enrollment_id, s.sid, s.studno, s.firstname, s.lastname, "
                + "e.academic_year, e.semester, e.status "
                + "FROM enrollment e JOIN student s ON e.student_id = s.sid "
                + "ORDER BY e.date_enrolled DESC";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Enrollment e = new Enrollment();
                e.setEnrollment_id(rs.getInt("enrollment_id"));
                e.setAcademic_year(rs.getString("academic_year"));
                e.setSemester(rs.getString("semester"));
                e.setStatus(rs.getString("status"));

                Student s = new Student();
                s.setSid(rs.getInt("sid"));
                s.setStudno(rs.getString("studno"));
                s.setFirstname(rs.getString("firstname"));
                s.setLastname(rs.getString("lastname"));
                e.setStudent(s);

                list.add(e);
            }
        } catch (SQLException ex) {
            System.out.println("Exception: " + ex);
        }
        return list;
    }

    public List<Enrollment> listEnrollmentsByStudentId(int sid) {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT e.*, s.studno, s.firstname, s.lastname "
                + "FROM enrollment e "
                + "JOIN student s ON e.student_id = s.sid "
                + "WHERE s.sid = ? "
                + "ORDER BY e.date_enrolled DESC";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, sid);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Enrollment e = new Enrollment();
                    e.setEnrollment_id(rs.getInt("enrollment_id"));
                    e.setAcademic_year(rs.getString("academic_year"));
                    e.setSemester(rs.getString("semester"));
                    e.setStatus(rs.getString("status"));
                    e.setDate_enrolled(rs.getString("date_enrolled"));

                    Student s = new Student();
                    s.setSid(sid);
                    s.setStudno(rs.getString("studno"));
                    s.setFirstname(rs.getString("firstname"));
                    s.setLastname(rs.getString("lastname"));
                    e.setStudent(s);

                    list.add(e);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Exception: " + ex);
        }
        return list;
    }

    public List<Enrollment> enrollmentList() {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT e.*, s.lastname, s.firstname, s.sid, s.studno "
                + "FROM enrollment e "
                + "JOIN student s ON e.student_id = s.sid "
                + "ORDER BY e.date_enrolled DESC";

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
                s.setSid(rs.getInt("sid"));
                s.setStudno(rs.getString("studno"));
                s.setFirstname(rs.getString("firstname"));
                s.setLastname(rs.getString("lastname"));
                e.setStudent(s);

                list.add(e);
            }
        } catch (SQLException ex) {
            System.out.println("Exception: " + ex);
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
            System.out.println("Exception: " + ex);
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
            System.out.println("Exception: " + ex);
        }
        return false;
    }

    public static List<EnrollmentRecord> getEnrollmentList(String programId, String year, String semester) {
        List<EnrollmentRecord> list = new ArrayList<>();
        String sql = "SELECT s.studno, s.first_name, s.last_name, s.middle_name, p.program_name "
                + "FROM enrollment e "
                + "JOIN student s ON e.student_id = s.sid "
                + "JOIN program p ON s.program_id = p.programId "
                + "WHERE e.academic_year = ? AND e.semester = ? AND p.programId = ?";

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
                list.add(rec);
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
        }
        return list;
    }

    public boolean isAlreadyEnrolled(String studno, String academicYear, String semester) {
        boolean exists = false;

        String sql = "SELECT COUNT(*) FROM enrollment WHERE studno = ? AND academic_year = ? AND semester = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studno);
            ps.setString(2, academicYear);
            ps.setString(3, semester);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    exists = (count > 0);
                }
            }

        } catch (SQLException ex) {
            System.out.println("Exception: " + ex);
        }

        return exists;
    }

    public boolean addEnrollment(Enrollment enrollment) {
        String sql = "INSERT INTO enrollment (studno, academic_year, semester, status, year_level) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, enrollment.getStudno());
            ps.setString(2, enrollment.getAcademic_year());
            ps.setString(3, enrollment.getSemester());
            ps.setString(4, enrollment.getStatus() != null ? enrollment.getStatus() : "Pending");
            ps.setInt(5, enrollment.getYearLevel());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                // 🔁 Fetch the course IDs associated with the student's current enrollment
                List<Integer> courseIds = getCourseIdsByStudent(enrollment.getStudno());

                // ✅ Sync course progress
                new CourseProgressDAO().syncProgressFromEnrollment(enrollment.getStudno(), courseIds);

                return true;
            }

        } catch (SQLException ex) {
            System.out.println("Exception: " + ex);
        }

        return false;
    }

    public List<Enrollment> getEnrollmentsByStudentNo(String studNo) {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT * FROM enrollment WHERE studno = ? ORDER BY academic_year DESC, semester DESC";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studNo);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Enrollment e = new Enrollment();
                e.setEnrollment_id(rs.getInt("eid"));
                e.setStudno(rs.getString("studno"));
                e.setAcademic_year(rs.getString("academic_year"));
                e.setSemester(rs.getString("semester"));
                e.setDate_enrolled(rs.getString("date_enrolled"));
//            e.setDate_enrolled(rs.getDate("date_enrolled"));
                // set other properties as needed
                list.add(e);
            }

        } catch (SQLException ex) {
            System.out.println("Exception: " + ex);
        }

        return list;
    }

    public List<Enrollment> findPendingEnrollments() {
        String sql = "SELECT e.*, s.first_name, s.last_name, p.program_name "
                + "FROM enrollment e "
                + "JOIN student s ON e.studentNo = s.studNo "
                + "JOIN program p ON s.program_id = p.id "
                + "WHERE e.payment_status = 'pending'";

        List<Enrollment> list = new ArrayList<>();
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Enrollment e = new Enrollment();

                // Access by column names
                e.setEnrollment_id(rs.getInt("id")); // or "enrollment_id" if that's your actual column name
                e.setStudno(rs.getString("studentNo"));
                e.setAcademic_year(rs.getString("academic_year"));
                e.setSemester(rs.getString("semester"));
                e.setDate_enrolled(rs.getString("date_enrolled"));
                e.setStatus(rs.getString("payment_status"));

                // Set student name and program name
                String fullName = rs.getString("last_name") + ", " + rs.getString("first_name");
                e.setStudentName(fullName);
                e.setProgramName(rs.getString("program_name"));

                list.add(e);
            }

        } catch (SQLException ex) {
            System.out.println("Exception: " + ex);
        }
        return list;
    }

    public boolean updatePaymentStatus(int enrollmentId, String status) {
        String sql = "UPDATE enrollment SET payment_status = ? WHERE id = ?";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, enrollmentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Exception: " + ex);
            return false;
        }
    }

    public boolean hasDeficiencies(String studentNo) {
        String sql = "SELECT COUNT(*) FROM course_deficiency WHERE student_no = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            System.out.println("Error checking deficiencies: " + ex.getMessage());
        }
        return false;
    }

    public int saveEnrollment(Enrollment e) {
        String sql = "INSERT INTO enrollment (student_id, academic_year, semester, date_enrolled, status, payment_status, year_level) VALUES (?, ?, ?, NOW(), ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, getStudentIdByStudNo(e.getStudno())); // Helper method
            ps.setString(2, e.getAcademic_year());
            ps.setString(3, e.getSemester());
            ps.setString(4, e.getStatus());
            ps.setString(5, e.getPayment_status());
            ps.setInt(6, e.getYearLevel());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println("Error saving enrollment: " + ex.getMessage());
        }
        return -1;
    }

    private int getStudentIdByStudNo(String studno) throws SQLException {
        String sql = "SELECT sid FROM student WHERE studno = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studno);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("sid");
            }
        }
        throw new SQLException("Student not found for studno: " + studno);
    }

    public void addEnrollmentDetail(int enrollmentId, String courseCode, int units) {
        String sql = "INSERT INTO enrollment_course (enrollment_id, course_code, units) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, enrollmentId);
            ps.setString(2, courseCode);
            ps.setInt(3, units);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error adding enrollment detail: " + ex.getMessage());
        }
    }

    public List<Enrollment> findByStudentNo(String studNo) {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT e.*, s.first_name, s.last_name, s.middle_name"
                + "        , p.program_name FROM enrollment e"
                + "        JOIN student s ON e.student_id = s.sid\n"
                + "        JOIN program p ON s.program_id = p.program_id"
                + "        WHERE s.studno = ?"
                + "                ORDER  BY e.date_enrolled DESC";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studNo);
            ResultSet rs = ps.executeQuery();
            CourseDAO courseDAO = new CourseDAO();

            while (rs.next()) {
                Enrollment e = new Enrollment();
                e.setEnrollment_id(rs.getInt("enrollment_id"));
                e.setAcademic_year(rs.getString("academic_year"));
                e.setSemester(rs.getString("semester"));
                e.setDate_enrolled(rs.getDate("date_enrolled").toString());
                e.setStatus(rs.getString("status"));
                e.setPayment_status(rs.getString("payment_status"));
                e.setStudno(studNo);
                e.setStudentName(rs.getString("last_name") + ", " + rs.getString("first_name") + " " + rs.getString("middle_name"));
                e.setProgramName(rs.getString("program_name"));

                // Fetch enrolled subjects
                List<Course> courses = courseDAO.getCoursesByEnrollmentId(rs.getInt("enrollment_id"));
                e.setEnrolledCourses(courses);

                list.add(e);
            }

        } catch (SQLException ex) {
            System.out.println("Error fetching enrollments with subjects: " + ex.getMessage());
        }

        return list;
    }

    public List<EnrollmentCourse> getEnrolledCoursesByEnrollmentId(int enrollmentId) {
        List<EnrollmentCourse> enrolledCourses = new ArrayList<>();
        String sql = "SELECT ec.id, ec.enrollment_id, c.course_id, c.course_code, c.course_title, c.units "
                + "FROM enrollment_courses ec "
                + "JOIN course c ON ec.course_id = c.course_id "
                + "WHERE ec.enrollment_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, enrollmentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                EnrollmentCourse ec = new EnrollmentCourse();
                ec.setId(rs.getInt("id"));
                ec.setEnrollmentId(rs.getInt("enrollment_id"));

                Course course = new Course();
                course.setCourseId(rs.getInt("course_id"));
                course.setCourseCode(rs.getString("course_code"));
                course.setCourseTitle(rs.getString("course_title"));
                course.setUnits(rs.getInt("units"));

                ec.setCourse(course);
                enrolledCourses.add(ec);
            }

        } catch (SQLException ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        return enrolledCourses;

    }

    public boolean approveEnrollment(int enrollmentId) {
        String sql = "UPDATE enrollment SET is_approved = TRUE WHERE enrollment_id = ?";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, enrollmentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        return false;
    }

    public List<Enrollment> getPendingEnrollments() {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT * FROM enrollment WHERE is_approved = FALSE";

        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Enrollment e = new Enrollment();
                e.setEnrollment_id(rs.getInt("enrollment_id"));
                e.setStudno(rs.getString("studno"));
                e.setAcademic_year(rs.getString("academic_year"));
                e.setSemester(rs.getString("semester"));
                e.setIsApproved(rs.getBoolean("is_approved"));
                list.add(e);
            }
        } catch (SQLException ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        return list;
    }

    public boolean addCourseToEnrollment(int enrollmentId, int courseId) {
        String sql = "INSERT INTO enrollment_courses (enrollment_id, course_id) VALUES (?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, enrollmentId);
            ps.setInt(2, courseId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        return false;
    }

    public boolean dropCourseFromEnrollment(int enrollmentCourseId) {
        String sql = "DELETE FROM enrollment_courses WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, enrollmentCourseId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        return false;
    }

    public int determineYearLevel(String studno) {
        String sql = "SELECT c.year_level, COUNT(*) as total "
                + "FROM enrollment_course ec "
                + // Assuming a join table like this
                "JOIN course c ON ec.course_id = c.course_id "
                + "WHERE ec.studno = ? "
                + "GROUP BY c.year_level "
                + "ORDER BY total DESC LIMIT 1";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studno);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("year_level");
            }
        } catch (SQLException ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        return 1; // Default to 1st year if no data
    }

    public List<EnrolledCourse> getDetailedEnrolledCourses(int enrollmentId) {
        List<EnrolledCourse> list = new ArrayList<>();
        try {
            Connection conn = DatabaseUtil.getConnection();
            String sql = "SELECT ec.*, c.course_code, c.course_description, c.units "
                    + "FROM enrolled_course ec "
                    + "JOIN course c ON ec.course_id = c.course_id "
                    + "WHERE ec.enrollment_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, enrollmentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                EnrolledCourse ec = new EnrolledCourse();
                ec.setCourse_id(rs.getInt("course_id"));
                ec.setEnrollment_id(enrollmentId);
                ec.setCourse_code(rs.getString("course_code"));
                ec.setCourse_description(rs.getString("course_description"));
                ec.setUnits(rs.getInt("units"));
                ec.setGrade(rs.getString("grade"));
                ec.setRemarks(rs.getString("remarks"));
                list.add(ec);
            }
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        return list;
    }

    public List<CourseProgress> getStudentCourseProgress(String studentId) {
        List<CourseProgress> progressList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT c.course_code, c.course_title, e.grade, "
                    + "CASE "
                    + "    WHEN e.grade >= 75 THEN 'Passed' "
                    + "    WHEN e.grade IS NULL THEN 'In Progress' "
                    + "    ELSE 'Failed' "
                    + "END AS status "
                    + "FROM enrollment e "
                    + "JOIN course c ON e.course_code = c.course_code "
                    + "WHERE e.student_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, studentId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                CourseProgress cp = new CourseProgress();
                cp.setCourseCode(rs.getString("course_code"));
                cp.setCourseTitle(rs.getString("course_title"));
//            cp.setGrade(rs.getObject("grade") != null ? rs.getInt("grade") : null);
                cp.setStatus(rs.getString("status"));
                progressList.add(cp);
            }

        } catch (SQLException ex) {
            System.out.println("Exception: " + ex.getMessage());
        } finally {
//        DatabaseUtil.close(conn, stmt, rs);
        }

        return progressList;
    }

    public List<Integer> getCourseIdsForAssignment(int programId, String semester, int yearLevel) {
        List<Integer> courseIds = new ArrayList<>();
        String sql = "SELECT course_id FROM course WHERE program_id = ? AND semester = ? AND year_level = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, programId);
            ps.setString(2, semester);
            ps.setInt(3, yearLevel);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    courseIds.add(rs.getInt("course_id"));
                }
            }

        } catch (SQLException ex) {
            System.out.println("Error in getCourseIdsForAssignment: " + ex.getMessage());
        }

        return courseIds;
    }

    public boolean assignCoursesToStudent(String studno, List<Integer> courseIds) {
        String sql = "INSERT INTO course_progress (studno, course_id, status) VALUES (?, ?, 'Not Started')";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            for (Integer courseId : courseIds) {
                ps.setString(1, studno);
                ps.setInt(2, courseId);
                ps.addBatch();
            }

            int[] batchResults = ps.executeBatch();
            for (int result : batchResults) {
                if (result == PreparedStatement.EXECUTE_FAILED) {
                    return false; // At least one insert failed
                }
            }

            return true;

        } catch (SQLException ex) {
            System.out.println("Error in assignCoursesToStudent: " + ex.getMessage());
            return false;
        }
    }

    public List<Integer> getCourseIdsByStudent(String studno) {
        List<Integer> courseIds = new ArrayList<>();
        String sql = "SELECT course_id FROM course_progress WHERE studno = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studno);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                courseIds.add(rs.getInt("course_id"));
            }

        } catch (SQLException ex) {
            System.out.println("Error in getCourseIdsByStudent: " + ex.getMessage());
        }

        return courseIds;
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

    public boolean updateStudentCourseStatus(String studno, int courseId, String status) {
        String sql = "UPDATE course_progress SET status = ? WHERE studno = ? AND course_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setString(2, studno);
            ps.setInt(3, courseId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating course status: " + e.getMessage());
            return false;
        }
    }

    public boolean isStudentCurrentlyEnrolled(String studentNo) {
        try (Connection con = DatabaseUtil.getConnection()) {
            String sql = "SELECT COUNT(*) FROM enrollment WHERE studno = ? AND status = 'Enrolled' AND is_approved = 1";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, studentNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error updating course status: " + e.getMessage());
        }
        return false;
    }

}
/*
public List<Enrollment> findByStudentNo(String studNo) {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT e.*, s.first_name, s.last_name, s.middle_name "
                + "        , p.program_name FROM enrollment e "
                + "        JOIN student s ON e.student_id = s.sid "
                + "        JOIN program p ON s.program_id = p.program_id "
                + "        WHERE s.studno = "
                + "                ? ORDER  BY e.date_enrolled DESC";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studNo);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Enrollment e = new Enrollment();
                e.setEnrollment_id(rs.getInt("enrollment_id"));
                e.setAcademic_year(rs.getString("academic_year"));
                e.setSemester(rs.getString("semester"));
                e.setDate_enrolled(rs.getDate("date_enrolled").toString());
                e.setStatus(rs.getString("status"));
                e.setPayment_status(rs.getString("payment_status"));
                e.setStudno(studNo);
                e.setStudentName(rs.getString("last_name") + ", " + rs.getString("first_name") + " " + rs.getString("middle_name"));
                e.setProgramName(rs.getString("program_name"));

                list.add(e);
            }

        } catch (SQLException ex) {
            System.out.println("Error fetching enrollments for student: " + ex.getMessage());
        }

        return list;
    }
 */
