/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.List;

/**
 *
 * @author hrkas
 */
public class Enrollment {
    private int enrollment_id;
    private int selectedStudentId;
    private String studno;
    private String courseCode;
    private String academic_year;
    private String semester;
    private String date_enrolled;
    private String status;
    private String studentName;
    private String programName;
    private String payment_status;
    private List<Course> enrolledCourses;
    private Assessment assessment;
    private Payment payment;
    private boolean isApproved;
    private int yearLevel;

    private Student student;

    public Enrollment() {
    }

    /**
     * @return the selectedStudentId
     */
    public int getSelectedStudentId() {
        return selectedStudentId;
    }

    /**
     * @param selectedStudentId the selectedStudentId to set
     */
    public void setSelectedStudentId(int selectedStudentId) {
        this.selectedStudentId = selectedStudentId;
    }

    public Enrollment(int enrollment_id, String studno, String academic_year, String semester, String date_enrolled, String status) {
        this.enrollment_id = enrollment_id;
        this.studno = studno;
        this.academic_year = academic_year;
        this.semester = semester;
        this.date_enrolled = date_enrolled;
        this.status = status;
    }

    /**
     * @return the enrollment_id
     */
    public int getEnrollment_id() {
        return enrollment_id;
    }

    /**
     * @param enrollment_id the enrollment_id to set
     */
    public void setEnrollment_id(int enrollment_id) {
        this.enrollment_id = enrollment_id;
    }

    /**
     * @return the studno
     */
    public String getStudno() {
        return studno;
    }

    /**
     * @param studno the studno to set
     */
    public void setStudno(String studno) {
        this.studno = studno;
    }

    /**
     * @return the academic_year
     */
    public String getAcademic_year() {
        return academic_year;
    }

    /**
     * @param academic_year the academic_year to set
     */
    public void setAcademic_year(String academic_year) {
        this.academic_year = academic_year;
    }

    /**
     * @return the semester
     */
    public String getSemester() {
        return semester;
    }

    /**
     * @param semester the semester to set
     */
    public void setSemester(String semester) {
        this.semester = semester;
    }

    /**
     * @return the date_enrolled
     */
    public String getDate_enrolled() {
        return date_enrolled;
    }

    /**
     * @param date_enrolled the date_enrolled to set
     */
    public void setDate_enrolled(String date_enrolled) {
        this.date_enrolled = date_enrolled;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the student
     */
    public Student getStudent() {
        return student;
    }

    /**
     * @param student the student to set
     */
    public void setStudent(Student student) {
        this.student = student;
    }

    /**
     * @return the studentName
     */
    public String getStudentName() {
        return studentName;
    }

    /**
     * @param studentName the studentName to set
     */
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    /**
     * @return the programName
     */
    public String getProgramName() {
        return programName;
    }

    /**
     * @param programName the programName to set
     */
    public void setProgramName(String programName) {
        this.programName = programName;
    }

    /**
     * @return the payment_status
     */
    public String getPayment_status() {
        return payment_status;
    }

    /**
     * @param payment_status the payment_status to set
     */
    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    /**
     * @return the enrolledCourses
     */
    public List<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    /**
     * @param enrolledCourses the enrolledCourses to set
     */
    public void setEnrolledCourses(List<Course> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    /**
     * @return the assessment
     */
    public Assessment getAssessment() {
        return assessment;
    }

    /**
     * @param assessment the assessment to set
     */
    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    /**
     * @return the payment
     */
    public Payment getPayment() {
        return payment;
    }

    /**
     * @param payment the payment to set
     */
    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    /**
     * @return the isApproved
     */
    public boolean isIsApproved() {
        return isApproved;
    }

    /**
     * @param isApproved the isApproved to set
     */
    public void setIsApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }

    /**
     * @return the yearLevel
     */
    public int getYearLevel() {
        return yearLevel;
    }

    /**
     * @param yearLevel the yearLevel to set
     */
    public void setYearLevel(int yearLevel) {
        this.yearLevel = yearLevel;
    }

    /**
     * @return the courseCode
     */
    public String getCourseCode() {
        return courseCode;
    }

    /**
     * @param courseCode the courseCode to set
     */
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
    
    
}
