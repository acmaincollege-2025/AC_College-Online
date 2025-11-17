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
public class EnrollmentStat {
    private String courseCode;
    private String courseTitle;
    private int enrolledCount;

    public EnrollmentStat() {
    }

    public EnrollmentStat(String courseCode, String courseTitle, int enrolledCount) {
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.enrolledCount = enrolledCount;
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
    
    
}
