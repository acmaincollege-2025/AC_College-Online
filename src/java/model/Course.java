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
public class Course {

    private int courseId;
    private String courseCode;
    private String courseTitle;
    private double units;
    private int programId;
    private String semester;
    private int yearLevel;
    private int enrolledCount;
    private String currYear;

    public Course() {
    }

    public Course(int courseId, String courseCode, String courseTitle, double units, int programId, String semester, int yearLevel) {
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.units = units;
        this.programId = programId;
        this.semester = semester;
        this.yearLevel = yearLevel;
    }

    /**
     * @return the courseId
     */
    public int getCourseId() {
        return courseId;
    }

    /**
     * @param courseId the courseId to set
     */
    public void setCourseId(int courseId) {
        this.courseId = courseId;
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
     * @return the units
     */
    public double getUnits() {
        return units;
    }

    /**
     * @param units the units to set
     */
    public void setUnits(double units) {
        this.units = units;
    }

    /**
     * @return the programId
     */
    public int getProgramId() {
        return programId;
    }

    /**
     * @param programId the programId to set
     */
    public void setProgramId(int programId) {
        this.programId = programId;
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
     * @return the currYear
     */
    public String getCurrYear() {
        return currYear;
    }

    /**
     * @param currYear the currYear to set
     */
    public void setCurrYear(String currYear) {
        this.currYear = currYear;
    }
}
