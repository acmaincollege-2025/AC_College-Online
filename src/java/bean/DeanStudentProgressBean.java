/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.EnrollmentDAO;
import dao.StudentDAO;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import model.EnrolledCourse;
import model.Enrollment;
import model.Student;
import util.DeanReportUtil;

/**
 *
 * @author hrkas
 */
@ManagedBean
@ViewScoped
public class DeanStudentProgressBean implements Serializable {

    /**
     * Creates a new instance of DeanStudentProgressBean
     */
    private List<Student> students;
    private Student selectedStudent;
    private List<Enrollment> enrollmentHistory;
    private List<EnrolledCourse> enrolledCourses;
    private double gpa;
    
    public DeanStudentProgressBean() {
    }

    /**
     * @return the students
     */
    public List<Student> getStudents() {
        return students;
    }

    /**
     * @param students the students to set
     */
    public void setStudents(List<Student> students) {
        this.students = students;
    }

    /**
     * @return the selectedStudent
     */
    public Student getSelectedStudent() {
        return selectedStudent;
    }

    /**
     * @param selectedStudent the selectedStudent to set
     */
    public void setSelectedStudent(Student selectedStudent) {
        this.selectedStudent = selectedStudent;
    }

    /**
     * @return the enrollmentHistory
     */
    public List<Enrollment> getEnrollmentHistory() {
        return enrollmentHistory;
    }

    /**
     * @param enrollmentHistory the enrollmentHistory to set
     */
    public void setEnrollmentHistory(List<Enrollment> enrollmentHistory) {
        this.enrollmentHistory = enrollmentHistory;
    }

    /**
     * @return the enrolledCourses
     */
    public List<EnrolledCourse> getEnrolledCourses() {
        return enrolledCourses;
    }

    /**
     * @param enrolledCourses the enrolledCourses to set
     */
    public void setEnrolledCourses(List<EnrolledCourse> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    /**
     * @return the gpa
     */
    public double getGpa() {
        return gpa;
    }

    /**
     * @param gpa the gpa to set
     */
    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    @PostConstruct
    public void init() {
        students = new StudentDAO().listAllStudents();
    }

    public void loadStudentProgress() {
        if (selectedStudent != null) {
            EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
            enrollmentHistory = enrollmentDAO.getEnrollmentsByStudentNo(selectedStudent.getStudno());
        }
    }

    public void loadEnrolledCourses(int enrollmentId) {
        enrolledCourses = new EnrollmentDAO().getDetailedEnrolledCourses(enrollmentId);
        computeGPA(); // Call GPA computation when course list is loaded
    }

    public void computeGPA() {
        double totalUnits = 0;
        double weightedSum = 0;

        if (enrolledCourses != null && !enrolledCourses.isEmpty()) {
            for (EnrolledCourse ec : enrolledCourses) {
                if (ec.getGrade() != null && ec.getUnits() > 0) {
                    try {
                        double grade = Double.parseDouble(ec.getGrade());
                        weightedSum += grade * ec.getUnits();
                        totalUnits += ec.getUnits();
                    } catch (NumberFormatException ex) {
                        // Ignore invalid grade
                    }
                }
            }
            gpa = (totalUnits > 0) ? (weightedSum / totalUnits) : 0;
        } else {
            gpa = 0;
        }
    }

    public void downloadPDF() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();

            response.reset();
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=student_progress_report.pdf");

            String studentName = selectedStudent.getLastname() + ", " + selectedStudent.getFirstname();
            DeanReportUtil.generatePDF(response.getOutputStream(), studentName, enrolledCourses);

            context.responseComplete();
        } catch (IOException ex) {
            System.out.println("PDF Generation Error: " + ex);
        }
    }
}
