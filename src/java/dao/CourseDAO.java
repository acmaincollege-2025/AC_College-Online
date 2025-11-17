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
import model.Course;
import util.DatabaseUtil;

/**
 *
 * @author hrkas
 */
public class CourseDAO {

    public boolean addCourse(Course course) {
        String sql = "INSERT INTO course (course_code, course_title, units, program_id, semester, year_level) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            setParams(ps, course);
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return false;
    }

    public boolean addCoursesFromExcel(List<Course> courseList) {
        for (Course course : courseList) {
            if (!addCourse(course)) {
                return false;
            }
        }
        return true;
    }

    public boolean updateCourse(Course course) {
        String sql = "UPDATE course SET course_code = ?, course_title = ?, units = ?, program_id = ?, semester = ?, year_level = ? WHERE course_id = ?";
        try (Connection con = DatabaseUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            setParams(ps, course);
            ps.setInt(7, course.getCourseId());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return false;
    }

    public boolean deleteCourse(int id) {
        String sql = "DELETE FROM course WHERE course_id = ?";
        try (Connection con = DatabaseUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return false;
    }

    public List<Course> listCourses() {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM course";
        try (Connection con = DatabaseUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Course c = new Course();
                c.setCourseId(rs.getInt("course_id"));
                c.setCourseCode(rs.getString("course_code"));
                c.setCourseTitle(rs.getString("course_title"));
                c.setUnits(rs.getDouble("units"));
                c.setProgramId(rs.getInt("program_id"));
                c.setSemester(rs.getString("semester"));
                c.setYearLevel(rs.getInt("year_level"));
                list.add(c);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return list;
    }

    public List<Course> getAvailableCoursesForStudent(String studNo) {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM course "
                + "        WHERE course_id NOT IN ("
                + "            SELECT course_id FROM enrolled_courses WHERE student_no = ?)";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studNo);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Course c = new Course();
                c.setCourseId(rs.getInt("course_id"));
                c.setCourseCode(rs.getString("course_code"));
                c.setCourseTitle(rs.getString("description"));
                c.setUnits(rs.getInt("units"));
                // set other fields as needed
                list.add(c);
            }

        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return list;
    }

    private void setParams(PreparedStatement ps, Course c) throws SQLException {
        ps.setString(1, c.getCourseCode());
        ps.setString(2, c.getCourseTitle());
        ps.setDouble(3, c.getUnits());
        ps.setInt(4, c.getProgramId());
        ps.setString(5, c.getSemester());
        ps.setInt(6, c.getYearLevel());
        ps.setString(7, c.getCurrYear());
    }

    public List<Course> findRegularCourses(int programId, String semester) {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM course WHERE program_id = ? AND semester = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, programId);
            ps.setString(2, semester);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Course c = new Course();
                c.setCourseCode(rs.getString("course_code"));
                c.setCourseTitle(rs.getString("course_title"));
                c.setUnits(rs.getInt("units"));
                list.add(c);
            }
        } catch (SQLException ex) {
            System.out.println("Error fetching regular courses: " + ex.getMessage());
        }
        return list;
    }

    public List<Course> getCoursesByEnrollmentId(int enrollmentId) {
        List<Course> courses = new ArrayList<>();
        String sql = " SELECT c.course_id, c.course_code, c.course_name "
                + "        , c.units FROM enrollment_course ec "
                + "        JOIN course c ON ec.course_id = c.course_id "
                + "        WHERE ec.enrollment_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, enrollmentId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Course c = new Course();
                c.setCourseId(rs.getInt("course_id"));
                c.setCourseCode(rs.getString("course_code"));
                c.setCourseTitle(rs.getString("course_name"));
                c.setUnits(rs.getInt("units"));
                courses.add(c);
            }

        } catch (SQLException ex) {
            System.out.println("Error fetching courses for enrollment: " + ex.getMessage());
        }

        return courses;
    }

    public List<Course> getAllCourses() {
        List<Course> courseList = new ArrayList<>();
        String sql = "SELECT course_id, course_code, course_title, units FROM course";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Course c = new Course();
                c.setCourseId(rs.getInt("course_id"));
                c.setCourseCode(rs.getString("course_code"));
                c.setCourseTitle(rs.getString("course_title"));
                c.setUnits(rs.getInt("units"));
                courseList.add(c);
            }
        } catch (SQLException ex) {
            System.out.println("Error fetching courses: " + ex.getMessage()); // You can also log this
        }

        return courseList;

    }

    public List<Course> getCoursesByProgram(int programId) {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM course WHERE program_id = ?";

        try (Connection con = DatabaseUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, programId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Course c = new Course();
                c.setCourseId(rs.getInt("course_id"));
                c.setCourseCode(rs.getString("course_code"));
                c.setCourseTitle(rs.getString("course_title"));
                c.setUnits(rs.getDouble("units"));
                c.setProgramId(rs.getInt("program_id"));
                c.setSemester(rs.getString("semester"));
                c.setYearLevel(rs.getInt("year_level"));
                c.setCurrYear(rs.getString("curr_year"));
                list.add(c);
            }
        } catch (Exception ex) {
            System.out.println("Error fetching courses by program: " + ex);
        }
        return list;
    }

    public List<String> getDistinctCurrYears() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT curr_year FROM course ORDER BY curr_year DESC";

        try (Connection con = DatabaseUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("curr_year"));
            }
        } catch (Exception ex) {
            System.out.println("Error fetching curriculum years: " + ex);
        }
        return list;
    }

    public List<Course> getCoursesByProgramAndCurrYear(Integer programId, String currYear) {
        List<Course> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM course WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (programId != null) {
            sql.append(" AND program_id = ?");
            params.add(programId);
        }

        if (currYear != null) {
            sql.append(" AND curr_year = ?");
            params.add(currYear);
        }

        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Course c = new Course();
                c.setCourseId(rs.getInt("course_id"));
                c.setCourseCode(rs.getString("course_code"));
                c.setCourseTitle(rs.getString("course_title"));
                c.setUnits(rs.getDouble("units"));
                c.setProgramId(rs.getInt("program_id"));
                c.setSemester(rs.getString("semester"));
                c.setYearLevel(rs.getInt("year_level"));
                c.setCurrYear(rs.getString("curr_year"));
                list.add(c);
            }

        } catch (Exception ex) {
            System.out.println("Error filtering courses: " + ex);
        }

        return list;
    }
}
