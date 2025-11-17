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
public class CourseStat {

    private String courseTitle;
    private int enrolledCount;

    public CourseStat() {
    }

    public CourseStat(String courseTitle, int enrolledCount) {
        this.courseTitle = courseTitle;
        this.enrolledCount = enrolledCount;
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
