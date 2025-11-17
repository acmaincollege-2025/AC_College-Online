/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author hrkas
 */
public class EnrollmentStats {

    private String programName;
    private String semester;
    private String courseTitle;
    private int year;
    private int totalEnrolled;
    private int enrolledCount;
    private Course course;

    public EnrollmentStats() {
    }

    public EnrollmentStats(String programName, String semester, int year, int totalEnrolled) {
        this.programName = programName;
        this.semester = semester;
        this.year = year;
        this.totalEnrolled = totalEnrolled;
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
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * @return the totalEnrolled
     */
    public int getTotalEnrolled() {
        return totalEnrolled;
    }

    /**
     * @param totalEnrolled the totalEnrolled to set
     */
    public void setTotalEnrolled(int totalEnrolled) {
        this.totalEnrolled = totalEnrolled;
    }

    /**
     * @return the courseTitle
     */
    public String getCourseTitle() {
        return courseTitle;
    }

    /**
     * @param courseTitle the courseTitle to set
     */
    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    /**
     * @return the enrolledCount
     */
    public int getEnrolledCount() {
        return enrolledCount;
    }

    /**
     * @param enrolledCount the enrolledCount to set
     */
    public void setEnrolledCount(int enrolledCount) {
        this.enrolledCount = enrolledCount;
    }

    /**
     * @return the course
     */
    public Course getCourse() {
        return course;
    }

    /**
     * @param course the course to set
     */
    public void setCourse(Course course) {
        this.course = course;
    }

}
