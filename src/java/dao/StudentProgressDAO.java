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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.Course;
import model.CourseProgress;
import model.Student;
import util.DatabaseUtil;

/**
 *
 * @author hrkas
 */
public class StudentProgressDAO {

    private Connection conn;

    public StudentProgressDAO() {
        try {
            conn = DatabaseUtil.getConnection(); // Use your own DB connection method
        } catch (SQLException ex) {
            ex.getMessage();
        }
    }

    // Get all students under a program
    public List<Student> getStudentsByProgram(String programCode) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM student WHERE program_code = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, programCode);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Student s = new Student();
                s.setSid(rs.getInt("studRecId"));
                s.setStudno(rs.getString("studNo"));
                s.setLastname(rs.getString("last_name"));
                s.setFirstname(rs.getString("first_name"));
                s.setMiddlename(rs.getString("middle_name"));
                students.add(s);
            }

        } catch (SQLException ex) {
            ex.getMessage();
        }

        return students;
    }

    // Get all courses in a program
    public List<Course> getCoursesByProgram(String programCode) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM course WHERE program_code = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, programCode);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Course c = new Course();
                c.setCourseCode(rs.getString("course_code"));
                c.setCourseTitle(rs.getString("course_title"));
                courses.add(c);
            }

        } catch (SQLException ex) {
            ex.getMessage();
        }

        return courses;
    }

    // Get list of course codes the student is enrolled in
    public Set<String> getEnrolledCourseCodesByStudent(String studRecId) {
        Set<String> courseCodes = new HashSet<>();
        String sql = "SELECT course_code FROM enrollment_course WHERE enrollment_id IN "
                + "(SELECT id FROM enrollment WHERE studRecId = ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studRecId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                courseCodes.add(rs.getString("course_code"));
            }

        } catch (SQLException ex) {
            ex.getMessage();
        }

        return courseCodes;
    }

    // Combine everything: student, courses, progress
    public Map<Student, List<CourseProgress>> getStudentProgressWithGrades(String programCode) {
        Map<Student, List<CourseProgress>> progressMap = new LinkedHashMap<>();

        List<Student> students = getStudentsByProgram(programCode);
        List<Course> courses = getCoursesByProgram(programCode);

        for (Student student : students) {
            List<CourseProgress> progressList = new ArrayList<>();
            double totalGrade = 0.0;
            int gradedCourses = 0;

            for (Course course : courses) {
                String status = "Not Taken";
                Double grade = getGrade(student.getStudno(), course.getCourseCode());

                if (grade != null) {
                    status = "Completed";
                    totalGrade += grade;
                    gradedCourses++;
                } else if (isEnrolled(student.getStudno(), course.getCourseCode())) {
                    status = "Enrolled";
                }

                progressList.add(new CourseProgress(course.getCourseCode(), course.getCourseTitle(), status, grade));
            }

            // Optional: attach GPA to Student (if your Student model supports it)
            if (gradedCourses > 0) {
                double gpa = totalGrade / gradedCourses;
                student.setGpa(gpa); // You must add `gpa` field in your Student model
            }

            progressMap.put(student, progressList);
        }

        return progressMap;
    }
// Get grade if exists

    private Double getGrade(String studNo, String courseCode) {
        String sql = "SELECT final_grade FROM grades WHERE studNo = ? AND course_code = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studNo);
            ps.setString(2, courseCode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("final_grade");
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }

// Check enrollment (if grade is null)
    private boolean isEnrolled(String studNo, String courseCode) {
        String sql = "SELECT 1 FROM enrollment_course ec "
                + "JOIN enrollment e ON ec.enrollment_id = e.id "
                + "WHERE e.studRecId = ? AND ec.course_code = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studNo);
            ps.setString(2, courseCode);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return false;
    }

}
